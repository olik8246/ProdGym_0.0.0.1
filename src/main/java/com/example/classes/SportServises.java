package com.example.classes;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SportServises {
    private List<Sportservis> signedContracts; // Підписані контракти
    private List<Sportservis> availableContracts; // Доступні контракти для підписання
    private Finance finance;

    public SportServises(Finance finance) {
        signedContracts = new ArrayList<>();
        this.finance = finance;
        initializeAvailableContracts();
    }

    private void initializeAvailableContracts() {
        availableContracts = new ArrayList<>(Arrays.asList(
                new Sportservis("Sportlife", 8000),
                new Sportservis("Starfit", 7000),
                new Sportservis("PowerClub", 7500),
                new Sportservis("Totalfitnes", 4000),
                new Sportservis("Фітнес клуб Liga", 3600)
        ));
    }

    public String getSignedContractsList() {
        StringBuilder sb = new StringBuilder("Підписані контракти:\n");
        for (Sportservis service : signedContracts) {
            sb.append(service.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<String> getSignedContractsNames() {
        return signedContracts.stream()
                .map(Sportservis::getName)
                .collect(Collectors.toList());
    }


    public String signContractByText(String serviceDetails) {
        Iterator<Sportservis> iterator = availableContracts.iterator();
        while (iterator.hasNext()) {
            Sportservis service = iterator.next();
            if (service.toString().equals(serviceDetails)) {
                int price = service.getContractPrice();
                if (finance.getBudget() < price) {
                    return "Недостатньо коштів для підписання контракту.";
                }
                signedContracts.add(service);
                iterator.remove();

                finance.addExpense("Підписання контракту: " + service, price);
                return "Контракт з " + service.toString() + " успішно підписано!";
            }
        }
        return "Контракт не знайдено. Спробуйте ще раз.";
    }

    public String terminateContractByText(String serviceDetails) {
        Iterator<Sportservis> iterator = signedContracts.iterator();
        while (iterator.hasNext()) {
            Sportservis service = iterator.next();
            if (service.toString().equals(serviceDetails)) {
                iterator.remove();
                availableContracts.add(service);
                int refundAmount = (int) (service.getContractPrice() * 0.75);
                finance.addIncome(refundAmount, "Розірвання контракту: " + service.getName());
                return "Контракт з " + service.toString() + " успішно розірвано! Повернуто до бюджету: "
                        + refundAmount + "грн";
            }
        }
        return "Контракт не знайдено. Спробуйте ще раз.";
    }

    public ReplyKeyboardMarkup getSportServicesKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Контракти");
        row1.add("Підписати контракт з клубом");
        row1.add("Розірвати контракт з клубом");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Назад");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    // Метод для створення клавіатури з доступними контрактами
    public ReplyKeyboardMarkup getAvailableContractsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Sportservis service : availableContracts) {
            KeyboardRow row = new KeyboardRow();
            row.add("Підписати " + service.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління контрактів з клубами");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    // Метод для створення клавіатури з підписаними контрактами
    public ReplyKeyboardMarkup getSignedContractsKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (Sportservis service : signedContracts) {
            KeyboardRow row = new KeyboardRow();
            row.add("Розірвати " + service.toString());
            keyboard.add(row);
        }

        KeyboardRow backRow = new KeyboardRow();
        backRow.add("Назад до управління контрактів з клубами");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}