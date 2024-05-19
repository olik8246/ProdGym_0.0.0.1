package com.example.classes;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Specialtys {
    private List<Specialty> specialties; // Список водіїв
    private List<Specialty> coachForHire; // Список водіїв доступних для найму
    private Finance finance;

    public Specialtys(Finance finance) {
        specialties = new ArrayList<>();
        this.finance = finance;
        initializeCoachForHire();
    }

    private void initializeCoachForHire() {
        coachForHire = new ArrayList<>(Arrays.asList(
                new Specialty("Волейбол", "1 розряд", 5),
                new Specialty("Футбол", "Кандидат в майсти спорту", 3),
                new Specialty("Баскетбол", "Любитель", 7),
                new Specialty("Плавання", "2 розряд", 4),
                new Specialty("Легка атлетика", "Майстер спорту", 6)
        ));
    }

    public Specialty getFirstAvailableCoach() {
        for (Specialty specialty : specialties) {
            if (specialty.getAssignedCoachs() != null && "Вільний".equals(specialty.getStatus())) {
                return specialty;
            }
        }
        return null;
    }

    public String getCoachList() {
        StringBuilder sb = new StringBuilder("Список тренерів:\n");
        for (Specialty specialty : specialties) {
            sb.append(specialty.toString()).append("\n");
        }
        return sb.toString();
    }

    public String hireCoachByText(String specialtysDetails, Coachs coachs) {
        Coach availableCoach = coachs.getFirstAvailableCoachs();
        if (availableCoach == null) {
            return "Немає вільних Спеціальностей для їх призначення.";
        }
        Iterator<Specialty> iterator = coachForHire.iterator();
        while (iterator.hasNext()) {
            Specialty specialty = iterator.next();
            if (specialty.toString().equals(specialtysDetails)) {
                int hiringCost = 10000; // Припустимо вартість найму
                if (finance.getBudget() < hiringCost) {
                    return "Недостатньо коштів для призначення спеціалізації.";
                }
                specialties.add(specialty);
                iterator.remove();

                availableCoach.setAssignedSpecialtys(specialty);
                specialty.setAssignedCoach(availableCoach);

                finance.addExpense("Проходження спеціалізації: " + specialty, hiringCost);
                return "Успішно пройденно курс спеціалізації " + availableCoach.toString() + "!";
            }
        }
        return "Спеціалізцію не знайденно. Спробуйте ще раз.";
    }

    public String fireCoachByText(String driverDetails) {
        Iterator<Specialty> iterator = specialties.iterator();
        while (iterator.hasNext()) {
            Specialty specialty = iterator.next();
            if (specialty.toString().equals(driverDetails)) {
                Coach assignedCoach = specialty.getAssignedCoachs();
                if (assignedCoach != null) {
                    assignedCoach.setAssignedSpecialtys(null);
                    specialty.setAssignedCoach(null);
                }
                iterator.remove();
                coachForHire.add(specialty);
                int refundAmount = (int) (10000 * 1);
                finance.addIncome(refundAmount, "Очищенння спеціалізації: " + specialty.getName());
                return "Водія " + specialty.toString() + " успішно очищенно! Повернуто до бюджету: " + refundAmount + "грн";
            }
        }
        return "Спеціалізацію не знайдено. Спробуйте ще раз.";
    }

    public ReplyKeyboardMarkup getCoachKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Список Спеціалізацій");
        row1.add("Пройти курс спеціалізації");
        row1.add("Очистити спеціалізацію");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Назад");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
    public ReplyKeyboardMarkup getHireCoachKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Specialty specialty : coachForHire) {
            KeyboardRow row = new KeyboardRow();
            row.add("Найняти " + specialty.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління спеціалізаціями");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getFireCoachKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Specialty specialty : specialties) {
            KeyboardRow row = new KeyboardRow();
            row.add("Звільнити " + specialty.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління спеціалізаціями");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
