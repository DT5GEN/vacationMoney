//package com.dt5gen.vacationmoney.ui;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.util.Pair;
//
//import com.dt5gen.vacationmoney.R;
//import com.dt5gen.vacationmoney.api.VacationApi;
//import com.google.android.material.datepicker.MaterialDatePicker;
//import com.google.android.material.datepicker.CalendarConstraints;
//import com.google.android.material.datepicker.DateValidatorPointForward;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import java.util.Calendar;
//
//public class ManActivity extends AppCompatActivity {
//
//    private TextView resultMoneyTextView;
//    private EditText salaryInput;
//    private Button calculateButton;
//
//    // Инициализируем экземпляр API
//    private VacationApi vacationApi;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        resultMoneyTextView = findViewById(R.id.resultMoneyTextView);
//        salaryInput = findViewById(R.id.salaryInput);
//        calculateButton = findViewById(R.id.calculateButton);
//
//        // Инициализируем Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.example.com")  // Заменить на твой URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        vacationApi = retrofit.create(VacationApi.class);
//
//        // Добавляем обработку кнопки расчета
//        calculateButton.setOnClickListener(v -> calculateVacationPay());
//    }
//
//    // Метод для расчета отпускных
//    private void calculateVacationPay() {
//        String salaryStr = salaryInput.getText().toString();
//        if (salaryStr.isEmpty()) {
//            resultMoneyTextView.setText("Введите зарплату!");
//            return;
//        }
//
//        double averageSalary = Double.parseDouble(salaryStr);
//        int vacationDays = 14; // Можно добавить выбор дней отпуска через календарь
//
//        // Отправляем запрос к API
//        Call<Double> call = vacationApi.calculateVacationPay(averageSalary, vacationDays);
//        call.enqueue(new Callback<Double>() {
//            @Override
//            public void onResponse(Call<Double> call, Response<Double> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    double vacationPay = response.body();
//                    resultMoneyTextView.setText("Отпускные: " + String.format("%.2f у.е.", vacationPay));
//                } else {
//                    resultMoneyTextView.setText("Ошибка расчета");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Double> call, Throwable t) {
//                resultMoneyTextView.setText("Ошибка сети: " + t.getMessage());
//            }
//        });
//    }
//}