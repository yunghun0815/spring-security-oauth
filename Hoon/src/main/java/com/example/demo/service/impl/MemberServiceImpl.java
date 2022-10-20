package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.service.MemberService;
import com.example.demo.vo.MemberVO;
import com.example.demo.vo.Role;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService{

	@Autowired
	MemberDao memberDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO vo = memberDao.login(username);
		
		return User.builder()
					.username(vo.getId())
					.password(vo.getPassword())
					.roles(vo.getRole())
					.build();
	}

	@Override
	public void signup(MemberVO vo) {
		vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		vo.setRole(Role.ROLE_USER.getTitle());
		memberDao.signup(vo);
	}

}
