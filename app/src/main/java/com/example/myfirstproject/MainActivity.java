package com.example.myfirstproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private double firstNumber = 0.0;
    private double secondNumber = 0.0;
    private String operator = "";
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.textViewResult);
        result.setText("0"); // התחלת המחשבון עם המספר 0

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void numFunction(View view) {
        Button button = (Button) view;

        if (isNewOperation || result.getText().toString().equals("0")) {
            clearDisplay();
            isNewOperation = false;
        }

        appendToDisplay(button.getText().toString());
    }

    public void operatorFunction(View view) {
        Button button = (Button) view;

        if (isDisplayNotEmpty()) {
            // אם כבר נבחר אופרטור, מבצעים חישוב אוטומטי
            if (!operator.isEmpty()) {
                secondNumber = parseDisplayValue();
                firstNumber = calculateResult(firstNumber, secondNumber, operator);
                displayResult(firstNumber);
            } else {
                firstNumber = parseDisplayValue();
            }
        } else {
            setError("Error: Enter a number first");
            return;
        }

        operator = button.getText().toString(); // שמירת האופרטור החדש
        isNewOperation = true; // מאפשר להתחיל להקליד מספר חדש
    }

    public void equalFunction(View view) {
        if (!isOperatorSelected()) return;

        if (isDisplayNotEmpty()) {
            secondNumber = parseDisplayValue();
            firstNumber = calculateResult(firstNumber, secondNumber, operator);
            displayResult(firstNumber);
            operator = ""; // איפוס האופרטור
            isNewOperation = true;
        } else {
            setError("Error: Missing second number");
        }
    }

    public void clearFunction(View view) {
        resetCalculator();
        result.setText("0"); // הצגת 0 כברירת מחדל לאחר ניקוי
    }

    // פונקציות עזר

    private boolean isDisplayNotEmpty() {
        return !result.getText().toString().isEmpty();
    }

    private boolean isOperatorSelected() {
        if (operator.isEmpty()) {
            setError("Error: No operator selected");
            return false;
        }
        return true;
    }

    private double parseDisplayValue() {
        try {
            return Double.parseDouble(result.getText().toString());
        } catch (NumberFormatException e) {
            setError("Error: Invalid input");
            return 0.0;
        }
    }

    private double calculateResult(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    setError("Error: Division by zero");
                    return 0.0;
                }
                return num1 / num2;
            default:
                setError("Error: Invalid operator");
                return 0.0;
        }
    }

    private void displayResult(double value) {
        if (value == (int) value) {
            result.setText(String.valueOf((int) value)); // תוצאה שלמה
        } else {
            result.setText(String.valueOf(value)); // תוצאה עם נקודה עשרונית
        }
    }

    private void setError(String message) {
        result.setText(message);
    }

    private void clearDisplay() {
        result.setText("");
    }

    private void appendToDisplay(String value) {
        result.append(value);
    }

    private void resetCalculator() {
        clearDisplay();
        firstNumber = 0.0;
        secondNumber = 0.0;
        operator = "";
        isNewOperation = true;
    }
}
