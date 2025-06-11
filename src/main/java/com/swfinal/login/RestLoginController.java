package com.swfinal.login;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/rest")
public class RestLoginController {
	
	// 생성자 주입 방식으로 Service 객체 생성 및 주입
	RestLoginService restLoginService;
	
	public RestLoginController(RestLoginService restLoginService) {
		this.restLoginService = restLoginService;
	}
	
	@GetMapping("login")
	public String login() {
		log.info("로그인 페이지 요청");

		return "login"; // login.html 파일 resolving
	}

	@PostMapping(value = "/request-login", produces = "application/json")
	@ResponseBody
	public Map<String, Object> requestLoginRest(
			@RequestBody Map<String, Object> param, 
			HttpSession session) {
		
		log.info("login 정보 : {}", param);
		
		Map<String, Object> result = restLoginService.requestLogin(param);
		
		if ("0000".equals((String) result.get("REPL_CD"))) {
			// 세션 정보 추가
			session.setAttribute("userId", param.get("userPw"));
		}
		
		return result;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		log.info("로그아웃 요청");
		session.invalidate(); // 현재 세션 정보를 비우는 메서드
		return "redirect:/rest/login";
		
		
	}

}