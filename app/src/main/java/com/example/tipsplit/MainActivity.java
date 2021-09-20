package com.example.tipsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText totalBillWithTax;
    TextView tipAmount;
    TextView totalWithTip;
    EditText numberOfPeople;
    TextView totalPerPerson;
    TextView overage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBillWithTax = findViewById(R.id.totalBillWithTax);
        tipAmount = findViewById(R.id.tipAmount);
        totalWithTip = findViewById(R.id.totalWithTip);
        numberOfPeople = findViewById(R.id.numberOfPeople);
        totalPerPerson = findViewById(R.id.totalPerPerson);
        overage = findViewById(R.id.overage);

        onRadioButtonPress();
        onGoPress();
        onClearPress();
    }

    private int getTipPercent(int id) {
        Map<Integer, Integer> tipsPercent = new HashMap<>();

        tipsPercent.put(R.id.percent12, 12);
        tipsPercent.put(R.id.percent15, 15);
        tipsPercent.put(R.id.percent18, 18);
        tipsPercent.put(R.id.percent20, 20);

        return tipsPercent.get(id);
    }

    private void clearCheck() {
        ((RadioButton) findViewById(R.id.percent12)).setChecked(false);
        ((RadioButton) findViewById(R.id.percent15)).setChecked(false);
        ((RadioButton) findViewById(R.id.percent18)).setChecked(false);
        ((RadioButton) findViewById(R.id.percent20)).setChecked(false);
    }

    private String getTwoDecimalPlace(double value) {
        return new DecimalFormat("0.00").format(value);
    }

    private void onRadioButtonPress() {
        ((RadioGroup) findViewById(R.id.tipPercent)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (!totalBillWithTax.getText().toString().isEmpty()) {
                    Double totalBill = Double.parseDouble(totalBillWithTax.getText().toString());

                    double tipAmountByPercent = (totalBill * getTipPercent(checkedId)) / 100.00;
                    tipAmount.setText("$" + getTwoDecimalPlace(tipAmountByPercent));

                    double totalAmountWithTip = totalBill + tipAmountByPercent;
                    totalWithTip.setText("$" + getTwoDecimalPlace(totalAmountWithTip));
                } else {
                    showToast("Please enter total bill value");
                    clearCheck();
                }
            }
        });
    }

    private void onGoPress() {
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String people = numberOfPeople.getText().toString();

                if (people.isEmpty()) {
                    showToast("Please enter number of people");
                } else if (people.equals("0")) {
                    showToast("Please enter minimum 1");
                } else {
                    int totalPeople = Integer.parseInt(people);

                    double totalBill = Double.parseDouble(totalWithTip.getText().toString().substring(1));
                    double individualBill = Math.ceil((totalBill * 100) / totalPeople) / 100.00;

                    totalPerPerson.setText("$" + getTwoDecimalPlace(individualBill));
                    overage.setText("$" + getTwoDecimalPlace((individualBill * totalPeople) - totalBill));
                }
            }
        });
    }

    private void onClearPress() {
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalBillWithTax.setText("");
                tipAmount.setText("$0");
                numberOfPeople.setText("");
                totalPerPerson.setText("$0");
                overage.setText("$0");
                totalWithTip.setText("$0");

                clearCheck();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}