package com.dt5gen.vacationmoney.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.dt5gen.vacationmoney.R;
import com.dt5gen.vacationmoney.api.VacationApi;
import com.dt5gen.vacationmoney.utils.DateRangeHelper;
import com.dt5gen.vacationmoney.utils.HolidayChecker;
import com.dt5gen.vacationmoney.utils.VacationCalculator;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_SELECTED_DATES = "selected_dates";

    private TextView resultMoneyTextView;
    private EditText salaryInput;
    private Button calculateButton;
    private TextView selectedDatesTextView;  // TextView для отображения выбранных дат
    public Pair<Long, Long> selectedDates;
    private VacationCalculator vacationCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultMoneyTextView = findViewById(R.id.resultMoneyTextView);
        salaryInput = findViewById(R.id.salaryInput);
        calculateButton = findViewById(R.id.calculateButton);
        Button datePickerButton = findViewById(R.id.datePickerButton);
        selectedDatesTextView = findViewById(R.id.resultMoneyTextView); // Текстовое поле для отображения выбранных дат

        // Настройка API и Calculator
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")  // Убедись, что сервер работает на этом адресе
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VacationApi vacationApi = retrofit.create(VacationApi.class);
        vacationCalculator = new VacationCalculator(vacationApi);

        // Восстановление состояния, если оно было сохранено
        if (savedInstanceState != null) {
            selectedDates = savedInstanceState.getParcelable(KEY_SELECTED_DATES);
            if (selectedDates != null) {
                // Обновляем TextView с выбранными датами
                updateSelectedDatesText();
            }
        }

        // Настройка кнопки выбора дат
        datePickerButton.setOnClickListener(v -> showDatePicker());

        // Добавляем обработку кнопки расчета
        calculateButton.setOnClickListener(v -> calculateVacationPay());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем среднюю зарплату
        if (savedInstanceState != null) {
            String savedSalary = savedInstanceState.getString("salaryInput");
            if (savedSalary != null) {
                salaryInput.setText(savedSalary);
            }

            // Восстанавливаем выбранные даты
            if (savedInstanceState.containsKey("startDate") && savedInstanceState.containsKey("endDate")) {
                long startDate = savedInstanceState.getLong("startDate");
                long endDate = savedInstanceState.getLong("endDate");
                selectedDates = new Pair<>(startDate, endDate);

                // Обновляем отображение выбранных дат
                updateSelectedDatesText();
            }

            // Восстанавливаем рассчитанную сумму отпускных
            String calculatedResult = savedInstanceState.getString("calculatedResult");
            if (calculatedResult != null && calculatedResult.startsWith("Отпускные:")) {
                resultMoneyTextView.setText(calculatedResult);
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();

        calendar.add(Calendar.YEAR, -3);
        long startDate = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.add(Calendar.YEAR, 2);
        long endDate = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(startDate);
        constraintsBuilder.setEnd(endDate);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Выберите период");
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<?> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDates = (Pair<Long, Long>) selection;
            updateSelectedDatesText();  // Обновляем TextView с выбранными датами
        });

        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

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
        int vacationDays = DateRangeHelper.getSelectedVacationDays(selectedDates);

        // Учитываем праздничные дни
        HolidayChecker holidayChecker = new HolidayChecker();
        int holidayCount = holidayChecker.countHolidaysInRange(selectedDates.first, selectedDates.second);

        vacationDays -= holidayCount;
        vacationCalculator.calculateVacationPay(averageSalary, vacationDays, new VacationCalculator.VacationCalculationCallback() {
            @Override
            public void onSuccess(double vacationPay) {
                resultMoneyTextView.setText("Отпускные: " + String.format("%.2f у.е.", vacationPay));
            }

            @Override
            public void onError(String errorMessage) {
                resultMoneyTextView.setText(errorMessage);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Сохраняем среднюю зарплату
        outState.putString("salaryInput", salaryInput.getText().toString());

        // Сохраняем выбранные даты (преобразуем их в long)
        if (selectedDates != null) {
            outState.putLong("startDate", selectedDates.first);
            outState.putLong("endDate", selectedDates.second);
        }

        // Сохраняем рассчитанную сумму отпускных
        String resultText = resultMoneyTextView.getText().toString();
        outState.putString("calculatedResult", resultText);
    }


    // Метод для обновления TextView с выбранными датами
    private void updateSelectedDatesText() {
        if (selectedDates != null && !resultMoneyTextView.getText().toString().startsWith("Отпускные:")) {
            String selectedDatesStr = "Выбрано с " + DateRangeHelper.formatDate(selectedDates.first)
                    + " по " + DateRangeHelper.formatDate(selectedDates.second);
            resultMoneyTextView.setText(selectedDatesStr);
        }
    }
}