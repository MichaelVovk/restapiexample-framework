package com.restapiexample.dummy;

public enum Services {
    EMPLOYEE("employee/"),
    EMPLOYEES("employees"),
    CREATE_EMPLOYEE("create"),
    DELETE_EMPLOYEE("delete/");
    private final String service;

    Services(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }
}
