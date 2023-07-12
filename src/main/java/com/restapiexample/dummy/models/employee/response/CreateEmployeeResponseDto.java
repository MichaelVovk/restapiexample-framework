package com.restapiexample.dummy.models.employee.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class CreateEmployeeResponseDto {
    private String status;
    private EmployeeData data;

    @Data
    public static class EmployeeData {
        private String name;
        private int salary;
        private int age;
        private int id;
    }
}
