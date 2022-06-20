package dev.lucaswolford.springrestendpoint

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(
	classes = arrayOf(SpringRestEndpointApplication::class),
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringRestEndpointApplicationTests {

	@Autowired
	lateinit var testRestTemplate: TestRestTemplate

	@Autowired
	lateinit var messageRepo: MessageRepository

	@Test
	fun contextLoads() {
	}

	@Test
	fun getMessagesWithEmpty() {
		messageRepo.deleteAll()
		val result = testRestTemplate.getForEntity("/", String::class.java)
		assertNotNull(result)
		assertEquals(HttpStatus.OK, result.statusCode)
		assertEquals("[]", result.body)
	}

	@Test
	fun getMessages() {
		messageRepo.save(Message(null, "Yo!"))
		val result = testRestTemplate.getForEntity("/", String::class.java)
		assertNotNull(result)
		assertEquals(HttpStatus.OK, result.statusCode)
		result.body?.let { assertTrue(it.contains("Yo!")) }
	}

	@Test
	fun postMessages() {
		val result = testRestTemplate.postForEntity("/", Message(null,"Hello!"), String::class.java)
		assertNotNull(result)
		assertEquals(HttpStatus.OK, result.statusCode)
		result.body?.let { assertTrue(it.contains("Hello!")) }
	}
}
