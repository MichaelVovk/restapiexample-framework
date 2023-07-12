package com.restapiexample.dummy.implementations.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapiexample.dummy.CustomHttpClient;
import com.restapiexample.dummy.Services;
import com.restapiexample.dummy.models.employee.request.CreateEmployeeRequestDto;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

import static com.restapiexample.dummy.Services.*;

public class EmployeeApiService {
    private static final Config config = ConfigFactory.load("config.properties");
    public static final String BASE_URL = config.getString("baseUrl");
    private final CloseableHttpClient httpClient;
    protected final ObjectMapper objectMapper;

    public EmployeeApiService() {
        httpClient = new CustomHttpClient().getHttpClient();
        objectMapper = new ObjectMapper();
    }

    public CloseableHttpResponse getEmployeeById(int id) throws IOException {
        HttpGet httpGet = new HttpGet(BASE_URL + EMPLOYEE.getService() + id);
        return executeRequest(httpGet);
    }
    public CloseableHttpResponse getEmployees() throws IOException {
        HttpGet httpGet = new HttpGet(BASE_URL + EMPLOYEES.getService());
        return executeRequest(httpGet);
    }

    public CloseableHttpResponse deleteEmployeeById(int id) throws IOException {
        HttpDelete httpDelete = new HttpDelete(BASE_URL + DELETE_EMPLOYEE.getService() + id);
        return executeRequest(httpDelete);
    }

    public CloseableHttpResponse createEmployee(CreateEmployeeRequestDto employeeDto) throws IOException {
        HttpPost httpPost = new HttpPost(BASE_URL + CREATE_EMPLOYEE.getService());
        setRequestBody(httpPost, employeeDto);
        return executeRequest(httpPost);
    }

    private void setRequestBody(HttpEntityEnclosingRequestBase request, Object requestBody) throws IOException {
        StringEntity requestEntity = new StringEntity(toJson(requestBody), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);
    }

    public CloseableHttpResponse executeRequest(HttpUriRequest request) throws IOException {
        return httpClient.execute(request);
    }

    private String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public void close() throws IOException {
        httpClient.close();
    }
}
