package com.example.classes;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Coachs {
    private List<Coach> coaches;
    private List<Coach> coachForRelease;
    private Finance finance;

    public Coachs(Finance finance) {
        coaches = new ArrayList<>();
        this.finance = finance;
        initializeCoachsForSale();
    }

    private void initializeCoachsForSale() {
        coachForRelease = new ArrayList<>(Arrays.asList(
                new Coach("Богдан", "Проданик", 10,18000),
                new Coach("Олег", "Хома", 3,20000),
                new Coach("Олександр", "Придатко", 7,28000),
                new Coach("Альона", "Товт", 4,1000),
                new Coach("Арнольд", "Шварцнегрик", 6,19999)
        ));
    }

    public Coach getFirstAvailableCoachs() {
        for (Coach coach : coaches) {
            if (coach.getAssignedCoach() == null) {
                return coach;
            }
        }
        return null;
    }

    public String getCoachsList() {
        StringBuilder sb = new StringBuilder("Список тренерів:\n");
        for (Coach coach : coaches) {
            sb.append(coach.toString()).append("\n");
        }
        return sb.toString();
    }

    public String buyCoachsByText(String Coach, Specialtys specialtys) {
        Iterator<Coach> iterator = coachForRelease.iterator();
        while (iterator.hasNext()) {
            Coach coach = iterator.next();
            if (coach.toString().equals(Coach)) {
                int price = coach.getPrice();
                if (finance.getBudget() < price) {
                    return "Недостатньо коштів для найму тренера.";
                }
                coaches.add(coach);
                iterator.remove();

                Specialty availableSpecialty = specialtys.getFirstAvailableCoach();
                if (availableSpecialty != null) {
                    availableSpecialty.setAssignedCoach(coach);
                    coach.setAssignedSpecialtys(availableSpecialty);
                }

                finance.addExpense("Підписання контракту з тренером: " + coach, price);
                return "Контракт з тренером" + coach.toString() + " успішно підписанно!";
            }
        }
        return "Контракт не знайдено. Спробуйте ще раз.";
    }

    public String sellCoachsByText(String Coach) {
        Iterator<Coach> iterator = coaches.iterator();
        while (iterator.hasNext()) {
            Coach coach = iterator.next();
            if (coach.toString().equals(Coach)) {
                Specialty assignedSpecialty = coach.getAssignedCoach();
                if (assignedSpecialty != null) {
                    assignedSpecialty.setAssignedCoach(null);
                    coach.setAssignedSpecialtys(null);
                }
                iterator.remove();
                coachForRelease.add(coach);
                int refundAmount = (int) (coach.getPrice() * 0.75);
                finance.addIncome(refundAmount, "Скорочення тренера: " + coach.getLastname());
                return "Контракт " + coach.toString() + " успішно скороченно! Повернуто до бюджету: "
                        + refundAmount + "грн";
            }
        }
        return "Контракт не знайдено. Спробуйте ще раз.";
    }

    // Метод для створення клавіатури управління
    public ReplyKeyboardMarkup getCoachsParkKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Список контрактів");
        row1.add("Підписати контракт");
        row1.add("Скоротити контракт");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Назад");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getBuyCoachsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Coach coach : coachForRelease) {
            KeyboardRow row = new KeyboardRow();
            row.add("Купити " + coach.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління тренерами");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getSellCoachsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Coach coach : coaches) {
            KeyboardRow row = new KeyboardRow();
            row.add("Продати " + coach.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління тренерами");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}