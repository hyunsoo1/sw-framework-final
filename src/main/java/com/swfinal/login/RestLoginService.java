package com.swfinal.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swfinal.login.mapper.RestLoginMapper;
import com.swfinal.util.EncryptUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestLoginService {
	
	RestLoginMapper restLoginMapper;
	EncryptUtil encryptUtil;
		
	public RestLoginService(RestLoginMapper restLoginMapper, EncryptUtil encryptUtil) {
		this.restLoginMapper = restLoginMapper;
		this.encryptUtil = encryptUtil;
		}
	
	//로그인 처리 Service 메서드
	@Transactional(readOnly = true)
	public Map<String, Object> requestLogin(Map<String, Object> param) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			resultMap.put("REPL_CD", "0000");
			resultMap.put("REPL_MSG", "로그인 정상");
			resultMap.put("REPL_PAGE_MSG", "로그인이 완료되었습니다");
			
			//id, pw가 제대로 왔는지 확인
			String userId = (String) param.get("userId");
			String userPw = (String) param.get("userPw");
			
			//아이다가 정상인지 확인
			if(userId == null || userId.isEmpty()) {
				//Exception 발생(REPL_CD, REPL_MSG, REPL_PAGE_MSG)
				throw new RestLoginException("1001","아이디 페러미터 누락", "아이디를 입력해주세요");
			}
			//비밀번호가 정상인지 확인
			if(userPw == null || userPw.isEmpty()) {
				//Exception 발생
				throw new RestLoginException("1002","비밀번호 페러미터 누락", "비밀번호를 입력해주세요");
			}
			
			Map<String, Object> memberInfo = restLoginMapper.selectMemberInfo(param);
			log.info("회원정보 확인 : {}");
			
			if(memberInfo == null) {
				throw new RestLoginException("1003","회원정보 없음", "회원정보가 없습니다");
			}
			

			String dbpw = (String) memberInfo.get("PW"); //sha256으로 암호화되어 저장된 비밀번호

			String encPw = (String) encryptUtil.encryptSha256(userPw);  //sha256으로 java에서 해싱
			if(!dbpw.equals(encPw)) {
				throw new RestLoginException("1004","비밀번호 틀림", "비밀번호가 일치하지 않습니다");
			}
			

			resultMap.put("memberInfo", memberInfo);

		}
		catch(RestLoginException rex) {
			resultMap.put("REPL_CD", rex.getReplCd());
			resultMap.put("REPL_MSG", rex.getReplMsg());
			resultMap.put("REPL_PAGE_MSG", rex.getReplMsg());
		}
		
		catch(Exception ex) {
			resultMap.put("REPL_CD", "9999");
			resultMap.put("REPL_MSG", "알 수 없는 에러");
			resultMap.put("REPL_PAGE_MSG", "로그인에 실패하였습니다");
		}
		return resultMap;
	}
}
