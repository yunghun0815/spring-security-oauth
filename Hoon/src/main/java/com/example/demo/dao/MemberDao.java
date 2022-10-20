package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.vo.MemberVO;

@Repository
@Mapper
public interface MemberDao {
	public void signup(MemberVO vo);
	public MemberVO login(String id);
	public void oauthSignup(MemberVO vo);
	public int loginCheck(String id);
}
