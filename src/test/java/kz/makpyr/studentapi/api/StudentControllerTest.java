package kz.makpyr.studentapi.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

	private static final String BASE = "/api/makpyr-marua/students";

	@Autowired
	private MockMvc mvc;

	private static String json(String name, String email) {
		return """
				{"name":"%s","surname":"Makpyr","email":"%s","studyGroup":"IT2-2306"}
				""".formatted(name, email);
	}

	private long create(String name, String email) throws Exception {
		String body = mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json(name, email)))
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();
		return ((Number) JsonPath.read(body, "$.id")).longValue();
	}

	@Test
	void infoShowsStudent() throws Exception {
		mvc.perform(get("/api/makpyr-marua/info"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.author").value("Marua Makpyr"))
			.andExpect(jsonPath("$.group").value("IT2-2306"))
			.andExpect(jsonPath("$.studentId").value("37578"));
	}

	@Test
	void createAndRead() throws Exception {
		mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("Marua", "create@makpyr.kz")))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE + "/")))
			.andExpect(jsonPath("$.name").value("Marua"));
		long id = create("Aru", "read@makpyr.kz");
		mvc.perform(get(BASE + "/" + id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("read@makpyr.kz"));
	}

	@Test
	void update() throws Exception {
		long id = create("Marua", "update@makpyr.kz");
		mvc.perform(put(BASE + "/" + id).contentType(MediaType.APPLICATION_JSON)
			.content(json("Marua Updated", "update@makpyr.kz")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Marua Updated"));
	}

	@Test
	void deleteStudent() throws Exception {
		long id = create("Marua", "delete@makpyr.kz");
		mvc.perform(delete(BASE + "/" + id)).andExpect(status().isNoContent());
		mvc.perform(get(BASE + "/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void unknownStudentIs404() throws Exception {
		mvc.perform(get(BASE + "/999999"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.detail").value("No student with id 999999"));
	}

	@Test
	void blankFieldsAre400() throws Exception {
		mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("", "")))
			.andExpect(status().isBadRequest());
	}

}
