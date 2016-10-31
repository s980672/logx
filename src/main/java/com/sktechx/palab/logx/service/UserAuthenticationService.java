package com.sktechx.palab.logx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sktechx.palab.logx.model.AdminUser;
import com.sktechx.palab.logx.model.User;
import com.sktechx.palab.logx.repository.AdminUserRepository;

/**
 * Created by 1100449 on 2016. 10. 28..
 */
@Service
public class UserAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

	@Value("${crowd.url}")
	String crowdUrl;
	@Value("${crowd.app.name}")
	String crowdAppName;
	@Value("${crowd.app.password}")
	String crowdAppPassword;
	
	@Autowired
	AdminUserRepository adminUserRepository;

	public User getUserDetail(String username, String password)
			throws JsonSyntaxException, ClientProtocolException, IOException {
		String url = crowdUrl + "/authentication?username=" + username;
		String data = "{\"value\":\"" + password + "\"}";

		Gson gson = new GsonBuilder().create();
		User user = gson.fromJson(sendPost(url, data), User.class);
		
		if (user != null) {
			url = crowdUrl + "/session";
			data = "{\"username\":\"" + username + "\",\"password\":\"" + password
					+ "\",\"validation-factors\":{\"validationFactors\":[{\"name\":\"remote_address\",\"value\":\"127.0.0.1\"}]}}";
	
			Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Map<String, Object> session = gson.fromJson(sendPost(url, data), type);
			user.setToken((String) session.get("token"));
		}

		return user;
	}
	
	public List<String> validateToken(String username, String token) {
		String url = crowdUrl + "/session/" + token;
		String data = "{\"validationFactors\":[{\"name\":\"remote_address\",\"value\":\"127.0.0.1\"}]}}";
		
		String response = sendPost(url, data);
		if (response.indexOf("token") != -1) {
			List<AdminUser> adminUsers = adminUserRepository.findByUsername(username);
			List<String> roles = new ArrayList<String>();
			for (AdminUser adminUser : adminUsers) {
				roles.add(adminUser.getRole());
			}
			return roles;
		}
		
		return null;
	}

	public String sendPost(String url, String data) {
		StringBuffer result = new StringBuffer();

		CloseableHttpClient client = HttpClients.createDefault();
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		} 
		HttpPost post = new HttpPost(uri);

		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		String auth = "Basic " + Base64.getEncoder().encodeToString((crowdAppName + ":" + crowdAppPassword).getBytes());
		post.setHeader("Authorization", auth);
		post.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));

		try {
			HttpResponse response = client.execute(post);
			int responseCode = response.getStatusLine().getStatusCode();

			logger.debug("Sending 'POST' request to URL : " + url);
			logger.debug("Response Code : " + responseCode);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		return result.toString();
	}
	
}
