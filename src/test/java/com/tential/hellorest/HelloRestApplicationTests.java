package com.tential.hellorest;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class HelloRestApplicationTests {

	@Autowired
	private MockMvc mvc;

	//Positive Test cases.
	@Test
	@WithMockUser(username = "ppaluru", password = "test1234")
	@Order(1)
	void postDepartmentWithNoId_whenMockMVC_thenResponseOK_thenId() throws Exception {

		String newDepartment = "{\"departmentName\":\"test 102\",\"employeeCount\":121}";
		this.mvc.perform(post("/api/v1/department").content(newDepartment).contentType(MediaType.APPLICATION_JSON))
//				.andDo(print())
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.departmentId").isNumber())
				.andExpect(jsonPath("$.departmentName").value("test 102"))
				.andExpect(jsonPath("$.employeeCount").value(121))
				.andExpect(jsonPath("$.departmentCreateDt").isNotEmpty())
				.andExpect(jsonPath("$.departmentUpdateDt").isNotEmpty())
				.andDo(mvcResult -> {
					String json = mvcResult.getResponse().getContentAsString();
					String departmentCreateDt= JsonPath.parse(json).read("$.departmentCreateDt").toString();
					String departmentUpdateDt = JsonPath.parse(json).read("$.departmentUpdateDt").toString();
					Assert.isTrue(departmentCreateDt.equals(departmentUpdateDt),"departmentCreateDt is Not same as departmentUpdateDt");
				});
	}

	@Test
	@Order(2)
	void getDepartmentWithId_thenResponseOK() throws Exception {
		this.mvc.perform(get("/api/v1/department/1").contentType(MediaType.APPLICATION_JSON))
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.departmentId").isNumber())
				.andExpect(jsonPath("$.departmentName").value("test 102"))
				.andExpect(jsonPath("$.employeeCount").value(121))
				.andExpect(jsonPath("$.departmentCreateDt").isNotEmpty())
				.andExpect(jsonPath("$.departmentUpdateDt").isNotEmpty());
	}

	@Test
	@WithMockUser(username = "ppaluru", password = "test1234")
	@Order(3)
	void putDepartmentWithDesc_whenMockMVC_thenResponseOK() throws Exception {

		String newDepartment = "{\"departmentId\":1,\"departmentName\":\"test put 102\",\"employeeCount\":12}";
		this.mvc.perform(put("/api/v1/department/1").content(newDepartment).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.departmentId").isNumber())
				.andExpect(jsonPath("$.departmentName").value("test put 102"))
				.andExpect(jsonPath("$.employeeCount").value(12))
				.andExpect(jsonPath("$.departmentCreateDt").isNotEmpty())
				.andExpect(jsonPath("$.departmentUpdateDt").isNotEmpty())
				.andDo(mvcResult -> {
					String json = mvcResult.getResponse().getContentAsString();
					String departmentCreateDt= JsonPath.parse(json).read("$.departmentCreateDt").toString();
					String departmentUpdateDt = JsonPath.parse(json).read("$.departmentUpdateDt").toString();
					Assert.isTrue(!departmentCreateDt.equals(departmentUpdateDt),"departmentCreateDt is same as departmentUpdateDt");
				})
//				.andDo(print())
				;
	}

	@Test
	@Order(4)
	void getAllDepartments_thenResponseOK() throws Exception {
		this.mvc.perform(get("/api/v1/departments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].departmentId").isNumber())
				.andExpect(jsonPath("$.[0].departmentName").value("test put 102"))
				.andExpect(jsonPath("$.[0].employeeCount").value(12))
				.andExpect(jsonPath("$.[0].departmentCreateDt").isNotEmpty())
				.andExpect(jsonPath("$.[0].departmentUpdateDt").isNotEmpty())
//				.andDo(print())
				;
	}

	//Bad test scenarios starts from 100. No Order is required so given same order.
	@Test
	@Order(100)
	void postDepartmentWithInvalidCred_whenMockMVC_thenUnAuthorized() throws Exception {
		String newDepartment = "{\"departmentName\":\"test 102\",\"employeeCount\":121}";
		this.mvc.perform(post("/api/v1/department").content(newDepartment).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	@Order(100)
	@WithMockUser(username = "ppaluru", password = "test1234")
	void postDepartmentWithNoData_whenMockMVC_thenResponseBad() throws Exception {
		this.mvc.perform(post("/api/v1/department").content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(100)
	void getDepartmentWithInvalidId_thenResponse404() throws Exception {
		this.mvc.perform(get("/api/v1/department/123").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	@Order(100)
	void getInvalidEndPoint_thenResponse404() throws Exception {
		this.mvc.perform(get("/api/v1/sub/department/123").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
//	@WithMockUser(username = "ppaluru", password = "test1234")
	@Order(100)
	void putDepartmentWithInvalidCred_whenMockMVC_thenUnAuthorized() throws Exception {

		String newDepartment = "{\"departmentId\":1,\"departmentName\":\"test put 102\",\"employeeCount\":12}";
		this.mvc.perform(put("/api/v1/department/1").content(newDepartment).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
//				.andDo(print())
		;
	}

	@Test
	@WithMockUser(username = "ppaluru", password = "test1234")
	@Order(100)
	void putDepartmentWithNoData_whenMockMVC_thenBad() throws Exception {
		this.mvc.perform(put("/api/v1/department/1").content("").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
//				.andDo(print())
		;
	}

	@Test
	@Order(100)
	void getInvalidUri_AllDepartments_thenBad() throws Exception {
		this.mvc.perform(get("/api/v1/sub/departments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
//				.andDo(print())
		;
	}

	@Test
	@Order(100)
	void getInvalidUri_thenBad() throws Exception {
		this.mvc.perform(get("/api/v1/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
//				.andDo(print())
		;
	}
}