# spring-security-oauth
security form 로그인과 oauth2.0 (네이버, 구글) 로그인 구현

## 1. spring security
#### 기존에 사용하던 WebSecurityConfigurerAdapter가 deprecated 되어 상속이 아닌 Bean 주입받아 구현
``` java
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()  //authorizeRequest : 요청 URL에 따라 접근 권한을 설정 
		.antMatchers("/", "/member/**").permitAll()  //antMatchers : URL 경로 패턴을 지정, permitAll : 모두 접근 허용
		.antMatchers("/admin/**").hasRole("ADMIN") //hasRole : 권한에 따른 접근 허용
		.anyRequest().authenticated(); //나머지는 권한 있어야 접근 가능
		
		http.formLogin()
		.loginPage("/member/login") //로그인 페이지
		.usernameParameter("id")
		.passwordParameter("password")
		.loginProcessingUrl("/login") //로그인 action
		.defaultSuccessUrl("/")  //로그인 성공시 url
		.failureHandler(loginExceptionHandler);  //로그인 실패시

		http.oauth2Login()
			.userInfoEndpoint()
			.userService(userService);

		http.logout()
		.logoutSuccessUrl("/")  //로그아웃 후 url
		.invalidateHttpSession(true);  //로그아웃 후 세션 제거

		http.csrf().disable(); //csrf disable
		
		return http.build();
	}
  
  	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
	}
```
#### 로그인 서비스 UserDetailService 상속받아 구현
``` java
  @Service
public class MemberServiceImpl implements MemberService, UserDetailsService{

	@Autowired
	MemberDao memberDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
  //UserDetailsService method
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO vo = memberDao.login(username);
		
		return User.builder()
					.username(vo.getId())
					.password(vo.getPassword())
					.roles(vo.getRole())
					.build();
	}
  
  //MemberSerivce method
	@Override 
	public void signup(MemberVO vo) {
		vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		vo.setRole(Role.ROLE_USER.getTitle());
		memberDao.signup(vo);
	}

}
```
## 2. security oauth2.0 구글, 네이버 로그인 구현
#### DefaultOAuth2UserService 상속받아 구현, registrationId로 소셜 타입 구분해 분기처리
``` java
@Service
public class SocailOAuth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest); //oauth 로그인 response 값
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId(); //oauth 서비스 이름naver, google 등
		
		Map<String, Object> attributes = oauth2User.getAttributes(); // oauth 유저 정보
		
        	String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
		String email = null;
		String name = null;
		if(registrationId.equals("naver")) {
			Map<String, String> info = (Map<String,String>)oauth2User.getAttribute("response");
			email = info.get("email");
			name = info.get("name");
		}else {
			email = oauth2User.getAttribute("email");
			name = oauth2User.getAttribute("name");
		}
		
		MemberVO vo = MemberVO.builder()
								.id(email)
							   .name(name)
							   .email(email)
							   .password("social"+ System.nanoTime())
							 //  .password(passwordEncoder.encode("social" + System.nanoTime()))
							   .provider(registrationId)
							   .role(Role.ROLE_USER.getTitle())
							   .build();
		//처음 로그인 사용자 가입처리
		if(memberDao.loginCheck(email) == 0) {
			memberDao.oauthSignup(vo);
		}
		
		 return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attributes, userNameAttributeName);
	}
}
```
