package com.sktechx.palab.logx.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.AdminUser;
import com.sktechx.palab.logx.model.User;
import com.sktechx.palab.logx.repository.AdminUserRepository;
import com.sktechx.palab.logx.service.UserAuthenticationService;

/**
 * Created by 1100449 on 2016. 10. 28..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserAuthenticationTest {
	
	Logger logger = LoggerFactory.getLogger(UserAuthenticationTest.class);
	
	@Autowired
	UserAuthenticationService userAuthenticationService;
	
	@Autowired
	AdminUserRepository adminUserRepository;
	
	@Before
    public void init(){
		adminUserRepository.save(new AdminUser(1, "1100449", "wheatherpong"));
		adminUserRepository.save(new AdminUser(2, "1100449", "tmap"));
	}
	
	@Test
	public void getAuthetication() throws Exception {
		String username = "1100449";
		String password = "wjdtjr12#$";
		User user = userAuthenticationService.getUserDetail(username, password);
		assertNotNull(user.getToken());
	}
	
	@Test
	public void validateToken() throws Exception {
		String username = "1100449";
		String password = "wjdtjr12#$";
		User user = userAuthenticationService.getUserDetail(username, password);
		String token = user.getToken();
		List<String> roles = userAuthenticationService.validateToken(username, token);
		assertNotNull(roles);
	}
	
}
