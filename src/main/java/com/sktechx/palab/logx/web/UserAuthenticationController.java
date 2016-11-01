package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.service.UserAuthenticationService;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 1100449 on 2016. 10. 27..
 */
@RestController
@RequestMapping("/user")
public class UserAuthenticationController {

	Logger logger = LoggerFactory.getLogger(UserAuthenticationController.class);

	@Autowired
	UserAuthenticationService userAuthenticationService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> getUserDetail(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String username = null;
		String password = null;
		if (authorization != null && authorization.startsWith("Basic")) {
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
			String[] values = credentials.split(":", 2);
			username = values[0];
			password = values[1];
		}

		User user = new User();
		try {
			user = userAuthenticationService.getUserDetail(username, password);
			if (user.getToken() == null) {
				return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> validateToken(HttpServletRequest request,
			@RequestParam(required = true) String username) {
		String token = request.getHeader("Authorization");
		List<String> roles = userAuthenticationService.validateToken(username, token);
		
		if (roles == null) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<Object>(roles, HttpStatus.OK);
	}

}
