package com.restful.booker.herokuappinfo;

import com.restful.booker.constants.EndPoints;
import com.restful.booker.model.AuthorisationPojo;
import com.restful.booker.model.HeroKuappPojo;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class HeroKuappSteps {
    @Step("Getting Access Token with username : {0}, password: {1}")
    public ValidatableResponse getToken(String username, String password) {
        AuthorisationPojo authPojo = new AuthorisationPojo();
        authPojo.setUsername(username);
        authPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .body(authPojo)
                .post("https://restful-booker.herokuapp.com/auth")
                .then().log().all();

    }

    @Step("Creating new booking with firstName : {0}, lastName: {1}, email: {2}, totalprice: {3} depositpaid: {4}, bookingdates: {5} and additonalneeds: {6}")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, HeroKuappPojo.BookingDates bookingdates, String additionalneeds) {

        HeroKuappPojo heroKuappPojo = new HeroKuappPojo();
        heroKuappPojo.setFirstname(firstname);
        heroKuappPojo.setLastname(lastname);
        heroKuappPojo.setTotalprice(totalprice);
        heroKuappPojo.setDepositpaid(depositpaid);
        heroKuappPojo.setBookingdates(bookingdates);
        heroKuappPojo.setAdditionalneeds(additionalneeds);
        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .body(heroKuappPojo)
                .post()
                .then().log().all();

    }

    @Step("Getting existing single booking with id: {0}")
    public ValidatableResponse getSingleBookingIDs(int id) {
        return SerenityRest.given()
                .pathParam("id", id)
                .when().
                get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().log().all();
    }

    @Step("Updating record with id:{0}, token{1}, firstName: {2}, lastName: {3}, email: {4}, totalprice: {5} depositpaid: {6}, bookingdates: {7} and additonalneeds: {8} ")
    public ValidatableResponse updateBookingWithID(int id, String token, String firstname, String lastname, int totalprice, boolean depositpaid, HeroKuappPojo.BookingDates bookingdates, String additionalneeds) {

        HeroKuappPojo heroKuappPojo = new HeroKuappPojo();
        heroKuappPojo.setFirstname(firstname);
        heroKuappPojo.setLastname(lastname);
        heroKuappPojo.setTotalprice(totalprice);
        heroKuappPojo.setDepositpaid(depositpaid);
        heroKuappPojo.setBookingdates(bookingdates);
        heroKuappPojo.setAdditionalneeds(additionalneeds);
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .pathParam("id", id)
                .body(heroKuappPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all().statusCode(200);
    }

    @Step("Deleting existing booking with id: {0} and token: {1}")
    public ValidatableResponse deleteABookingID(int id, String token) {
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .pathParam("id", id)
                .when().delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then().log().all();

    }

    @Step("Getting booking info by ID")
    public ValidatableResponse getBookingByID() {
        return SerenityRest.given()
                .when()
                .get()
                .then().statusCode(200);
    }
}
