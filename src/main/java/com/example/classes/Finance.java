package com.example.classes;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Finance {
    private int totalIncome;
    private int totalExpenses;
    private int budget;
    private Map<String, Integer> expensesDetails;
    private Map<String, Integer> incomeDetails;

    public Finance(int initialBudget) {
        this.totalIncome = 0;
        this.totalExpenses = 0;
        this.budget = initialBudget;
        this.expensesDetails = new HashMap<>();
        this.incomeDetails = new HashMap<>();
    }

    public void addIncome(int income, String item) {
        this.totalIncome += income;
        this.budget += income;
        this.incomeDetails.merge(item, income, Integer::sum);
    }

    public String getIncomeDetails() {
        StringBuilder sb = new StringBuilder("Деталі доходів:\n");
        incomeDetails.forEach((item, amount) -> {
            sb.append(formatDetails(item, amount));
        });
        return sb.toString();
    }

    public String getFullIncomeReport() {
        return "Загальні доходи: " + getTotalIncome() + "грн\n" + getIncomeDetails();
    }

    public void addExpense(String item, int expense) {
        this.totalExpenses += expense;
        this.budget -= expense;
        this.expensesDetails.merge(item, expense, Integer::sum);
    }

    public String getExpensesDetails() {
        StringBuilder sb = new StringBuilder("Деталі витрат:\n");
        expensesDetails.forEach((item, amount) -> {
            sb.append(formatDetails(item, amount));
        });
        return sb.toString();
    }

    public String getFullExpenseReport() {
        return "Загальні витрати: " + getTotalExpenses() + "грн\n" + getExpensesDetails();
    }

    private String formatDetails(String item, Integer amount) {
        if (item.startsWith("Вибір спеціалізації")) {

            int index = item.indexOf(" - ");
            if (index > -1) {
                item = item.substring(0, index); // Зберігаємо лише частину до " - "
            }
            return item + ": " + amount + "грн\n";
        } else if (item.startsWith("Купівля контракту з тренером")) {
            int index = item.lastIndexOf(" - ");
            if (index > -1) {
                item = item.substring(0, index);
            }
            return item + ": " + amount + "грн\n";
        } else if (item.startsWith("Підписання контракту")) {
            int index = item.indexOf('(');
            if (index > -1) {
                item = item.substring(0, index).trim(); // Зберігаємо лише назву
            }
            return item + ": " + amount + "грн\n";
        } else {
            // Загальний формат для інших типів витрат
            return item + ": " + amount + "грн\n";
        }
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public int getBudget() {
        return budget;
    }

    public ReplyKeyboardMarkup getFinanceKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Доходи");
        row1.add("Витрати");
        row1.add("Бюджет");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Назад");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
