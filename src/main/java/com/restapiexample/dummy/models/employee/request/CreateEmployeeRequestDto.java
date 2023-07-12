package com.restapiexample.dummy.models.employee.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
public class CreateEmployeeRequestDto {
    @JsonProperty("name")
    public String name;

    @JsonProperty("salary")
    public int salary;

    @JsonProperty("age")
    public int age;
}
