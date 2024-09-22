package com.dt5gen.vacationmoney.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.dt5gen.vacationmoney.R;
import com.dt5gen.vacationmoney.api.VacationApi;
import com.dt5gen.vacationmoney.utils.HolidayChecker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.sql.Date;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView resultMoneyTextView;
    private EditText salaryInput;
    private Button calculateButton;

    // Инициализируем экземпляр API
    private VacationApi vacationApi;

    // Хранение выбранных дат
    private Pair<Long, Long> selectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultMoneyTextView = findViewById(R.id.resultMoneyTextView);
        salaryInput = findViewById(R.id.salaryInput);
        calculateButton = findViewById(R.id.calculateButton);
        Button datePickerButton = findViewById(R.id.datePickerButton);

        // Настройка кнопки выбора дат
        datePickerButton.setOnClickListener(v -> showDatePicker());

        // Настройка Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")  // check, что сервер работает на этом адресе
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        vacationApi = retrofit.create(VacationApi.class);

        // Добавляем обработку кнопки расчета
        calculateButton.setOnClickListener(v -> calculateVacationPay());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Сохраняем выбранные даты
        if (selectedDates != null) {
            outState.putLong("startDate", selectedDates.first);
            outState.putLong("endDate", selectedDates.second);
        }

        // Сохраняем введённую зарплату
        outState.putString("salaryInput", salaryInput.getText().toString());
    }

    private void showDatePicker() {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();

        // Устанавливаем диапазон от 3 лет назад до 2 лет вперед
        long today = calendar.getTimeInMillis();

        // 3 года назад
        calendar.add(Calendar.YEAR, -3);
        long startDate = calendar.getTimeInMillis();

        // Возвращаем текущее время
        calendar.setTimeInMillis(today);

        // 2 года вперед
        calendar.add(Calendar.YEAR, 2);
        long endDate = calendar.getTimeInMillis();

        // Настройка ограничения для календаря (от 3 лет назад до 2 лет вперёд)
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(startDate);
        constraintsBuilder.setEnd(endDate);

        // Создание Date Range Picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Выберите период");
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

        // Обработка выбранных дат
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDates = selection;
            resultMoneyTextView.setText(datePicker.getHeaderText());

            // Если зарплата уже введена, делаем пересчет отпускных
            String salaryStr = salaryInput.getText().toString();
            if (!salaryStr.isEmpty()) {
                calculateVacationPay();
            }
        });

        // Показываем DatePicker
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    // Метод для расчета отпускных
    private void calculateVacationPay() {
        String salaryStr = salaryInput.getText().toString();
        if (salaryStr.isEmpty()) {
            resultMoneyTextView.setText("Введите зарплату!");
            return;
        }

        if (selectedDates == null) {
            resultMoneyTextView.setText("Выберите период!");
            return;
        }

        double averageSalary = Double.parseDouble(salaryStr);
        int vacationDays = getSelectedVacationDays(); // Получаем количество дней из выбранных дат

        // Учитываем праздничные дни
        HolidayChecker holidayChecker = new HolidayChecker();
        int holidayCount = holidayChecker.countHolidaysInRange(new Date(selectedDates.first), new Date(selectedDates.second));

        // Вычитаем количество праздничных дней
        vacationDays -= holidayCount;

        // Отправляем запрос к API с учетом вычета праздничных дней
        Call<Double> call = vacationApi.calculateVacationPay(averageSalary, vacationDays);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double vacationPay = response.body();
                    resultMoneyTextView.setText("Отпускные: " + String.format("%.2f у.е.", vacationPay));
                } else {
                    resultMoneyTextView.setText("Ошибка расчета");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                resultMoneyTextView.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }

    // Метод для получения количества дней между выбранными датами
    private int getSelectedVacationDays() {
        if (selectedDates != null) {
            long startMillis = selectedDates.first;
            long endMillis = selectedDates.second;
            int days = (int) ((endMillis - startMillis) / (1000 * 60 * 60 * 24)) + 1; // Включаем последний день
            return days;
        }
        return 0;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем выбранные даты
        if (savedInstanceState != null) {
            long startDate = savedInstanceState.getLong("startDate");
            long endDate = savedInstanceState.getLong("endDate");

            if (startDate != 0 && endDate != 0) {
                selectedDates = new Pair<>(startDate, endDate);
                resultMoneyTextView.setText("Выбранный период: " + new Date(startDate) + " - " + new Date(endDate));
            }

            // Восстанавливаем введённую зарплату
            String savedSalary = savedInstanceState.getString("salaryInput");
            if (savedSalary != null) {
                salaryInput.setText(savedSalary);
            }
        }
    }
}