package com.example.managesystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManageSystemApplicationTests {

	@Resource
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void checkAccessTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject header = new JSONObject();
		header.put("userId","123456");
		header.put("accountName","test");
		header.put("role","admin");
		headers.set("Authorization", Base64.getEncoder().encodeToString(JSON.toJSONString(header).getBytes()));

		JSONObject body = new JSONObject();
		body.put("resource","resource A");

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/resource").headers(headers).contentType(MediaType.APPLICATION_JSON).param("data",JSON.toJSONString(body))).andReturn();
		String res = result.getResponse().getContentAsString();
		assert res.equals("success");
		System.out.println(res);
	}

	@Test
	public void addUserTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject header = new JSONObject();
		header.put("userId","123456");
		header.put("accountName","test");
		header.put("role","admin");
		headers.set("Authorization", Base64.getEncoder().encodeToString(JSON.toJSONString(header).getBytes()));

		JSONObject body = new JSONObject();
		body.put("userId","115");
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource A");
		endpoint.add("resource B");
		endpoint.add("resource D");
		body.put("endpoint", endpoint);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser").headers(headers).contentType(MediaType.APPLICATION_JSON).param("data",JSON.toJSONString(body))).andReturn();
		String res = result.getResponse().getContentAsString();
		assert res != null;
		System.out.println(res);
	}

}
