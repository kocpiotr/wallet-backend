package com.example.demo.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.WaletBackendApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.hamcrest.Matchers.*;

@SpringBootTest(
		classes = WaletBackendApplication.class)
		@AutoConfigureMockMvc
		@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
class UserRestIntegrationTests {
	

	Gson gsonBuilder = new GsonBuilder().create();

	@Autowired
    private MockMvc mvc;    
	
	@Test
	void create_user_works() throws Exception {
		//GIVEN
		final String jsonString = gsonBuilder.toJson(new User("Antos"));
		
		//WHEN-THEN
		final MvcResult result = mvc.perform(post("/users").content(jsonString).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is("Antos")))
		.andExpect(jsonPath("$._links.self.href", endsWith("/users")))
		.andExpect(jsonPath("$._links.users.href", endsWith("/users"))).andReturn();
		
		//CLEAN-UP
		final MockHttpServletResponse asyncResult = (MockHttpServletResponse) result.getResponse();
		final User fromJson = gsonBuilder.fromJson(asyncResult.getContentAsString(), User.class);
		mvc.perform(delete("/users/" + fromJson.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}
	
	@Test
	void get_single_user_worsk() throws Exception {
		//GIVEN
		final String jsonString = gsonBuilder.toJson(new User("Antos"));
		final MvcResult result = mvc.perform(post("/users").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();
		final User fromJson = gsonBuilder.fromJson(result.getResponse().getContentAsString(), User.class);
		mvc.perform(get("/users/" + fromJson.getId()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		//WHEN-THEN
		mvc.perform(delete("/users/" + fromJson.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}
	
	@Test
	void delete_single_user_worsk() throws Exception {
		//GIVEN
		final String jsonString = gsonBuilder.toJson(new User("Antos"));
		final MvcResult result = mvc.perform(post("/users").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();
		final User fromJson = gsonBuilder.fromJson(result.getResponse().getContentAsString(), User.class);

		//WHEN-THEN
		mvc.perform(get("/users/" + fromJson.getId()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("Antos")))
		.andExpect(jsonPath("$._links.self.href", endsWith("/users")));
		
		//CLEAN-UP
		mvc.perform(delete("/users/" + fromJson.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}	
	
	@Test
	void get_all_users_works() throws Exception {
		mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$._embedded.userList", hasSize(2)))
		.andExpect(jsonPath("$._embedded.userList[0].name", is("Malgosia")))
		.andExpect(jsonPath("$._embedded.userList[0]._links.self.href", endsWith("/users")))
		.andExpect(jsonPath("$._embedded.userList[1].name", is("Piotr")))
		.andExpect(jsonPath("$._embedded.userList[1]._links.self.href", endsWith("/users")))
		.andExpect(jsonPath("$._links.self.href", endsWith("/users")));
	}

}
