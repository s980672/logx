package com.sktechx.palab.logx.test;


import com.google.gson.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	ServletTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class
})
//@ContextConfiguration(classes=TestConfig.class)
@WebAppConfiguration
public abstract class AbstractJUnit4SpringMvcTests extends AbstractTransactionalJUnit4SpringContextTests {

	protected MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext applicationContext;

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}

	JsonSerializer<Date> ser = new JsonSerializer<Date>() {
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
				context) {
			return src == null ? null : new JsonPrimitive(src.getTime());
		}
	};

	JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return json == null ? null : new Date(json.getAsLong());
		}
	};

	protected Gson gson = new GsonBuilder()
	.registerTypeAdapter(Date.class, ser)
	.registerTypeAdapter(Date.class, deser).create();
}

