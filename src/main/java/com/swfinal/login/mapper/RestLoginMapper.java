package com.swfinal.login.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestLoginMapper {
	
	//로그인 처리를 위한 회원 정보 조회
	Map<String, Object> selectMemberInfo(Map<String, Object>param);
	
}
