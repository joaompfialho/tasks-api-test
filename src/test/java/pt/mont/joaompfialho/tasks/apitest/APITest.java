package pt.mont.joaompfialho.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend"; 
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{\r\n"
					+ "	\"task\": \"Test via API\",\r\n"
					+ "	\"dueDate\": \"2022-02-20\"\r\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(201)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{\r\n"
				+ "	\"task\": \"Test via API with invalid date\",\r\n"
				+ "	\"dueDate\": \"2020-01-01\"\r\n"
				+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaSemDescricao() {
		RestAssured.given()
			.body("{\r\n"
				+ "	\"task\": \"\",\r\n"
				+ "	\"dueDate\": \"2030-01-01\"\r\n"
				+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Fill the task description"))
		;
	}
	
}


