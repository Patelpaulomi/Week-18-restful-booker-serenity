package com.restful.booker.herokuappinfo;

import com.restful.booker.model.HeroKuappPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class HeroKuappCRUDTest extends TestBase {
    static String username = "admin";
    static String password = "password123";
    static String firstname = "Paula" + TestUtils.getRandomValue();
    static String lastname = "Patel" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static boolean depositpaid = true;
    static String additionalneeds = "lunch";
    static String token="c8fa4af5449aff5c104930482cc994318f2ae1a097c32bb596799dae3b2f6f3b";
    static int id;

    @Steps
    HeroKuappSteps heroKuappSteps;

    @Title("This method will create a Token")
    @Test
    public void test001() {
        ValidatableResponse response =heroKuappSteps.getToken(username, password).statusCode(200);
        token = response.extract().path("token");
    }

    @Title("This method will create a booking")
    @Test
    public void test002() {
        HeroKuappPojo.BookingDates bookingdates = new HeroKuappPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        ValidatableResponse response = heroKuappSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds).statusCode(200);
        id = response.extract().path("bookingid");
    }

    @Title("This method will verify new Booking ID creation")
    @Test
    public void test003() {
        ValidatableResponse response = heroKuappSteps.getBookingByID();
        ArrayList<?> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(id));
    }

    @Title("This method will get booking with Id")
    @Test
    public void test004()
    {
       heroKuappSteps.getSingleBookingIDs(id).statusCode(200);
    }

    @Title("This method will update a booking with ID")
    @Test
    public void test005() {
        additionalneeds = "lunch";
        HeroKuappPojo.BookingDates bookingdates = new HeroKuappPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        heroKuappSteps.updateBookingWithID(id, token, firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        ValidatableResponse response = heroKuappSteps.getSingleBookingIDs(id);
        HashMap<String, ?> update = response.extract().path("");
        Assert.assertThat(update, hasValue("lunch"));
    }

    @Title("This method will delete a booking with ID")
    @Test
    public void test006() {
        heroKuappSteps.deleteABookingID(id, token).statusCode(201);
       heroKuappSteps.getSingleBookingIDs(id).statusCode(404);
    }


}

