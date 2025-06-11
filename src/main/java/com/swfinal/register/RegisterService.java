package com.swfinal.register;

import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.swfinal.register.mapper.RegisterMapper;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegisterService {

	RegisterMapper registerMapper;
	
	public RegisterService(RegisterMapper registerMapper) {
		this.registerMapper = registerMapper;
	}
	
	@Transactional(readOnly=false)
	public Map<String, Object> requestRegister(Map<String, Object> params) {
		
		Map<String, Object> resultMap= new HashMap<String, Object>();
		
		try {
			//응답 코드 초가화
			resultMap.put("REPL_CD", "0000");
			resultMap.put("REPL_MSG", "정상");
			resultMap.put("REPL_PAGE_MSG", "정상처리되었습니다");
			
			//파라미터 확인
			log.info("파라미터 확인 : {}", params);
			
			//회원가입을 위해 필요한 파라미터가 들어왔는지 검증(id, pw, name, email)
			//이름검증만
			String userId = (String) params.get("userId");
			if(userId == null || userId.isEmpty()) {
				throw new RegisterException("2001", "아이디 입력값 누락","아이디를 입력해주세요");
			}
			
			//중복 아이디 확인
			int duplCnt = registerMapper.selectMemberDuplicateCount(params);
			if(duplCnt > 0) {
				throw new RegisterException("2002", "아이디 중복", "중복된 아이디가 있습니다");
			}
			
			//회원가입 처리(회원정보 등록)
			if(registerMapper.insertMember(params) < 0) {
				throw new RegisterException("2003", "회원 정보 등록 오류", "중복된 아이디가 있습니다");

			}
			
			//회원정보 정상 등록 여부를 확인휘해 DB에서 조회하여 설정 (불필요)
			Map<String, Object> memberInfo = registerMapper.selectMemberInfo(params);
			resultMap.put("MEMBER_INFO", memberInfo);

		}
		catch(RegisterException rex) {
			resultMap.put("REPL_CD", rex.getReplCd());
			resultMap.put("REPL_MSG", rex.getReplMsg());
			resultMap.put("REPL_PAGE_MSG", rex.getReplPageMsg());
			log.error("회원가입 중 오류 발생: {}", rex.getReplMsg());
		}
		catch(Exception ex) {
			resultMap.put("REPL_CD", "9999");
			resultMap.put("REPL_MSG", "알 수 없는 에러발생");
			resultMap.put("REPL_PAGE_MSG", "알 수 없는 에러가 발생하였씁니다");
		}
		
		return resultMap;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getMember(String userId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// 응답코드 초기화
			resultMap.put("REPL_CD", "0000");
			resultMap.put("REPL_MSG", "정상");
			resultMap.put("REPL_PAGE_MSG", "정상처리되었습니다.");
			
			//파라미터 초기화
			Map<String, Object>paramMap = new HashMap<String, Object>();
			paramMap.put("userId", userId);
			
			//회원정도 조회
			Map<String, Object> memberInfo = registerMapper.selectMemberInfo(paramMap);
			resultMap.put("MEMBER_INFO", memberInfo);
		}
		catch(RegisterException rex) {
			resultMap.put("REPL_CD", rex.getReplCd());
			resultMap.put("REPL_MSG", rex.getReplMsg());
			resultMap.put("REPL_PAGE_MSG", rex.getReplPageMsg());
			log.error("회원 상세 조회중 오류 발생: {}", rex.getReplMsg());
		}
		catch(Exception ex) {
			resultMap.put("REPL_CD", "9999");
			resultMap.put("REPL_MSG", "알 수 없는 에러 발생");
			resultMap.put("REPL_PAGE_MSG", "알 수 없는 에러가 발생하였습니다.");
		}
		return resultMap;
	}
	
	//수정 메서드
	@Transactional(readOnly = false)
	public Map<String, Object> reqeustModify(Map<String, Object>params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// 응답코드 초기화
			resultMap.put("REPL_CD", "0000");
			resultMap.put("REPL_MSG", "정상");
			resultMap.put("REPL_PAGE_MSG", "정상처리되었습니다.");
						
			// 파라미터 확인
			log.info("파라미터 확인 : {}", params);
						
			// 회원 정보 수정
			if (registerMapper.updateMember(params) < 0) {
				throw new RegisterException("2004", "회원 정보 수정 오류", "회원 정보 수정 중 오류가 발생하였습니다.\n관리자에게 문의해주세요.");		
			}
		}
		catch(RegisterException rex) {
			resultMap.put("REPL_CD", rex.getReplCd());
			resultMap.put("REPL_MSG", rex.getReplMsg());
			resultMap.put("REPL_PAGE_MSG", rex.getReplPageMsg());
			log.error("회원 정보 수집중 오류 발생: {}", rex.getReplMsg());
		}
		catch(Exception ex) {
			resultMap.put("REPL_CD", "9999");
			resultMap.put("REPL_MSG", "알 수 없는 에러 발생");
			resultMap.put("REPL_PAGE_MSG", "알 수 없는 에러가 발생하였습니다.");
		}
		return resultMap;
	}
	//수정 메서드
	@Transactional(readOnly = false)
	public Map<String, Object> reqeustRemove(Map<String, Object>params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
			
		try {
			// 응답코드 초기화
			resultMap.put("REPL_CD", "0000");
			resultMap.put("REPL_MSG", "정상");
			resultMap.put("REPL_PAGE_MSG", "정상처리되었습니다.");
						
			// 파라미터 확인
			log.info("파라미터 확인 : {}", params);
						
			// 회원 정보 삭제
			if (registerMapper.deleteMember(params) < 0) {
				throw new RegisterException("2005", "회원 정보 삭제 오류", "회원 정보 삭제 중 오류가 발생하였습니다.\n관리자에게 문의해주세요.");		
			}
		}
		catch(RegisterException rex) {
			resultMap.put("REPL_CD", rex.getReplCd());
			resultMap.put("REPL_MSG", rex.getReplMsg());
			resultMap.put("REPL_PAGE_MSG", rex.getReplPageMsg());
			log.error("회원 정보 삭제중 오류 발생: {}", rex.getReplMsg());
		}
		catch(Exception ex) {
			resultMap.put("REPL_CD", "9999");
			resultMap.put("REPL_MSG", "알 수 없는 에러 발생");
			resultMap.put("REPL_PAGE_MSG", "알 수 없는 에러가 발생하였습니다.");
		}
		return resultMap;	
	}
}