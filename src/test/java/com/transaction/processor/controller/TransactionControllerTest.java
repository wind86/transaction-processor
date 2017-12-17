package com.transaction.processor.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.processor.dto.TransactionDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class TransactionControllerTest {

	@Value("${server.context-path}")
	private String contextPath;

	@Value("${server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private String url;

	@PostConstruct
	public void init() {
		this.url = String.format("http://localhost:%d/%s/", port, contextPath);
	}

	@Test
	public void saveTransaction() throws Exception {
		ResponseEntity<String> response = postTransaction(createTransaction());

		assertEquals(200, response.getStatusCodeValue());

		@SuppressWarnings("unchecked")
		Map<String, String> responseBody = (Map<String, String>) parseResponseBody(response);
		assertNotNull(responseBody.get("transactionId"));
	}

	@Test
	public void invalidTransaction() throws Exception {
		TransactionDto transactionDto = createTransaction();
		transactionDto.setValue(null);

		ResponseEntity<String> response = postTransaction(transactionDto);

		assertEquals(400, response.getStatusCodeValue());

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> responseBody = (Map<String, Map<String, String>>) parseResponseBody(response);
		Map<String, String> validationErrors = responseBody.get("errors");
		
		assertNotNull(validationErrors);
		assertTrue(validationErrors.size() > 0);
		
		String message = validationErrors.get("value");
		assertNotNull(message);
		assertEquals("may not be null", message);
	}
	
	private Map<String, ?> parseResponseBody(ResponseEntity<String> response) throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(response.getBody(), new TypeReference<Map<String, ?>>() {});
	}
	
	private ResponseEntity<String> postTransaction(TransactionDto transaction) throws RestClientException, JsonProcessingException {
		return restTemplate.exchange(url, HttpMethod.POST, createHttpEntity(jsonMapper, transaction), String.class);
	}
	
	private static HttpEntity<String> createHttpEntity(ObjectMapper jsonMapper, TransactionDto transaction) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestJson = jsonMapper.writeValueAsString(transaction);
		return new HttpEntity<String>(requestJson, headers);
	}
	
	private static TransactionDto createTransaction() {
		TransactionDto transactionDto = new TransactionDto();

		transactionDto.setIssuerAccountId(1L);
		transactionDto.setRecipientAccountId(2L);
		transactionDto.setTime(new Date());
		transactionDto.setValue(BigDecimal.TEN);
		
		return transactionDto;
	}
}