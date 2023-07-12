package com.restapiexample.dummy.models.employee.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class GetEmployeesResponseDto {
    public String status;
    public List<Datum> data;
    public String message;

    @Data
    public static class Datum {
        public int id;
        public String employee_name;
        public int employee_salary;
        public int employee_age;
        public String profile_image;
    }
}
