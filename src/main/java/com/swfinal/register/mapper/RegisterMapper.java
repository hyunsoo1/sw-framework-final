package com.swfinal.register.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterMapper {

	//중복회웡ㄴ 확인용
	int selectMemberDuplicateCount(Map<String, Object>params);
	
	//회원 정보 등록
	int insertMember(Map<String, Object>params);
	
	//회원 정보 조회
	Map<String, Object> selectMemberInfo(Map<String, Object>params);
	
	//회원 정보 수정
	int updateMember(Map<String, Object>params);
	
	//회원 정보 삭제
	int deleteMember(Map<String, Object>params);
}
