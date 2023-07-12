package com.restapiexample.dummy.tests;

import com.restapiexample.dummy.implementations.backend.EmployeeApiService;
import com.restapiexample.dummy.models.employee.request.CreateEmployeeRequestDto;
import com.restapiexample.dummy.models.employee.response.CreateEmployeeResponseDto;
import com.restapiexample.dummy.models.employee.response.GetEmployeesResponseDto;
import io.qameta.allure.Step;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.*;

class CreateEmployeeTest extends BaseTest {
    private static EmployeeApiService apiService;

    static ArrayList<Integer> employeeIds = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        apiService = new EmployeeApiService();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        employeeIds.forEach(id -> {
            try {
                apiService.deleteEmployeeById(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        apiService.close();
    }

    @Test()
    void testCreateEmployeeWithValidRequestReturns200() throws IOException {
        CreateEmployeeRequestDto requestDto = createEmployeeRequestDto();
        CreateEmployeeResponseDto createEmployeeResponseDto = createEmployee(requestDto);

        assertThat(createEmployeeResponseDto.getStatus())
                .as("Response status should be 'success'")
                .isEqualTo("success");
        assertThat(createEmployeeResponseDto.getData().getName())
                .as("Employee name should match the request")
                .isEqualTo(requestDto.getName());
        assertThat(createEmployeeResponseDto.getData().getAge())
                .as("Employee age should match the request")
                .isEqualTo(requestDto.getAge());
        assertThat(createEmployeeResponseDto.getData().getSalary())
                .as("Employee salary should match the request")
                .isEqualTo(requestDto.getSalary());

        CreateEmployeeResponseDto getEmployeeResponseDto = getEmployeeById(createEmployeeResponseDto.getData().getId());

        assertThat(getEmployeeResponseDto.getData().getName())
                .as("Retrieved employee name should match the created employee")
                .isEqualTo(createEmployeeResponseDto.getData().getName());
        assertThat(getEmployeeResponseDto.getData().getAge())
                .as("Retrieved employee age should match the created employee")
                .isEqualTo(createEmployeeResponseDto.getData().getAge());
        assertThat(getEmployeeResponseDto.getData().getSalary())
                .as("Retrieved employee salary should match the created employee")
                .isEqualTo(createEmployeeResponseDto.getData().getSalary());
    }

    @Test
    void testCreateEmployeeNotAllowedWithInvalidRequest() throws IOException {
        CreateEmployeeRequestDto negativeAgeRequest = createEmployeeRequestDto();
        negativeAgeRequest.setAge(-1000000000);
        CloseableHttpResponse errorResponse = apiService.createEmployee(negativeAgeRequest);
        assertThat(errorResponse.getStatusLine().getStatusCode())
                .as("Response status should be 'error'")
                .isEqualTo(SC_BAD_REQUEST);
    }

    @Test()
    void testGetEmployeesReturns200() throws IOException {
        CreateEmployeeRequestDto requestDto = createEmployeeRequestDto();
        CreateEmployeeResponseDto createEmployeeResponseDto = createEmployee(requestDto);

        CloseableHttpResponse response = apiService.getEmployees();
        GetEmployeesResponseDto getEmployeesResponseDto = objectMapper.
                readValue(getResponseBody(response), GetEmployeesResponseDto.class);
        GetEmployeesResponseDto.Datum data= getEmployeesResponseDto.getData().stream().filter(datum ->
            datum.getId()==createEmployeeResponseDto.getData().getId()).findFirst().orElse(null);
        assertThat(getEmployeesResponseDto.getData())
                .as("We receive more than 1 result")
                .hasSizeGreaterThan(1);
        assertThat(getEmployeesResponseDto.getStatus())
                .as("Response status should be 'success'")
                .isEqualTo("success");
        assertThat(Objects.requireNonNull(data).getEmployee_name())
                .as("Employee name should match created in the request")
                .isEqualTo(requestDto.getName());
        assertThat(data.getEmployee_age())
                .as("Employee age should match created in the request")
                .isEqualTo(requestDto.getAge());
        assertThat(data.getEmployee_salary())
                .as("Employee salary should match created in the request")
                .isEqualTo(requestDto.getSalary());
    }

    @Step
    CreateEmployeeRequestDto createEmployeeRequestDto() {
        return CreateEmployeeRequestDto.builder()
                .name(faker.name().firstName())
                .salary(faker.number().numberBetween(0, 100000000))
                .age(faker.number().numberBetween(0, 200))
                .build();
    }

    @Step
    private CreateEmployeeResponseDto createEmployee(CreateEmployeeRequestDto requestDto) throws IOException {
        try (CloseableHttpResponse response = apiService.createEmployee(requestDto)) {
            assertThat(response.getStatusLine().getStatusCode())
                    .as("Response status code should be 200")
                    .isEqualTo(SC_OK);
            CreateEmployeeResponseDto createEmployeeResponseDto = objectMapper.
                    readValue(getResponseBody(response), CreateEmployeeResponseDto.class);
            employeeIds.add(createEmployeeResponseDto.getData().getId());
            return createEmployeeResponseDto;
        }
    }

    @Step
    private CreateEmployeeResponseDto getEmployeeById(int employeeId) throws IOException {
        try (CloseableHttpResponse response = apiService.getEmployeeById(employeeId)) {
            assertThat(response.getStatusLine().getStatusCode())
                    .as("Response status code should be 200")
                    .isEqualTo(SC_OK);
            return objectMapper.readValue(getResponseBody(response), CreateEmployeeResponseDto.class);
        }
    }
}
