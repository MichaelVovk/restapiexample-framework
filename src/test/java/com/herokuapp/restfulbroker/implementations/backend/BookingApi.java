package com.herokuapp.restfulbroker.implementations.backend;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.herokuapp.restfulbroker.models.booking.BookingRequestDto;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static com.herokuapp.restfulbroker.Services.BOOKING;

public final class BookingApi {
    private static final String MEDIA_TYPE_JSON = "application/json";

    private RequestSpecification requestSender() {
        return given(new RequestSpecBuilder()
                .setBaseUri(AuthApi.BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .setBasePath(BOOKING.getService())
                .addHeader("Accept", BookingApi.MEDIA_TYPE_JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build());
    }
    @Step("get booking id")
    public Response getBookingById(int id) {
        return requestSender().get("/" + id);
    }
    @Step("create booking")
    public Response createBooking(BookingRequestDto bookingRequestDto) {
        return requestSender().body(bookingRequestDto).post();
    }
    @Step("update booking")
    public Response partialUpdateBooking(BookingRequestDto bookingRequestDto, int id, String tokenValue) {
        return requestSender().header("Cookie", "token=" + tokenValue).body(bookingRequestDto).patch("/" + id);
    }

    @Step("get booking request")
    public Response getBookings(Map<String, ?> params) {
        return requestSender().params(params).get();
    }

    @Step("delete booking request")
    public Response deleteBooking(int id, String authToken) {
        return requestSender().header("Cookie", "token=" + authToken)
                .delete("/" + id);
    }
}
