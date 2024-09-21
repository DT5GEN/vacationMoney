package com.dt5gen.vacationmoney.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VacationApi {
    @GET("/calculate")
    Call<Double> calculateVacationPay(
            @Query("averageSalary") double averageSalary,
            @Query("vacationDays") int vacationDays
    );
}