package com.restapiexample.dummy.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BaseTest {

    protected final Faker faker = new Faker();

    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getResponseBody(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        logger.info("Response -> {}", body);
        return body;
    }
}
