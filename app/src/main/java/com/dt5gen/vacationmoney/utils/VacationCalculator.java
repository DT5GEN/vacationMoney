package com.dt5gen.vacationmoney.utils;

import com.dt5gen.vacationmoney.api.VacationApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacationCalculator {

    private VacationApi vacationApi;

    public VacationCalculator(VacationApi vacationApi) {
        this.vacationApi = vacationApi;
    }

    public void calculateVacationPay(double averageSalary, int vacationDays, VacationCalculationCallback callback) {
        Call<Double> call = vacationApi.calculateVacationPay(averageSalary, vacationDays);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double vacationPay = response.body();
                    callback.onSuccess(vacationPay);
                } else {
                    callback.onError("Ошибка расчета");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                callback.onError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    public interface VacationCalculationCallback {
        void onSuccess(double vacationPay);

        void onError(String errorMessage);
    }
}