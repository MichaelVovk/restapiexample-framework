package com.restapiexample.dummy;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomHttpClient {
    private final CloseableHttpClient httpClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpClient.class);

    public CustomHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                    LOGGER.info("--> Sending {} request to {}", request.getRequestLine().getMethod(),
                            request.getRequestLine().getUri());
                    logRequestBody(request);
                })
                .build();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    private void logRequestBody(HttpRequest request) {
        if (request instanceof HttpEntityEnclosingRequest entityEnclosingRequest) {
            HttpEntity entity = entityEnclosingRequest.getEntity();
            if (entity != null) {
                try {
                    String requestBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    LOGGER.info("Request body: {}", requestBody);
                } catch (IOException e) {
                    LOGGER.info("Failed to read request body", e);
                }
            }
        }
    }
}
