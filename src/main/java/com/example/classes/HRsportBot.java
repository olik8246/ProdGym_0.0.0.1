package com.example.classes;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class HRsportBot extends TelegramLongPollingBot {
    private Coach coach;
    private Coachs coachs;
    private Specialtys specialtys;
    private SportServises sportServises;
    private Finance finance;
    private boolean isRunning;

    public HRsportBot() {
        this.finance = new Finance(500000); // тут вказуємо бюджет для старту
        coach = new Coach();
        coachs = new Coachs(finance);
        specialtys = new Specialtys(finance);
        sportServises = new SportServises(finance);
        isRunning = true;
    }
    private String getWelcomeMessage() {
        return "Ласкаво просимо до системи управління HR Спортивного напрямку тренерів! \n\n" +
                "1. **Контракти тренерів**: Підпішіть контракт з тренером у розділі 'Контракти тренерів'.\n" +
                "2. **Обрання спеціалізації якою буде займатися тренер**: Оберіть спеціалізацію у розділі 'Спеціалізації тренерів'.\n" +
                "3. **Підписання контрактів з Спортивними клубами Львова**: Перейдіть у 'Контракти з Спорт-клубами' та підпишіть контракти для подальшої співпраці з клубом\n" +
                "4. **Фінанси**: Для перевірки 'Доходів', 'Витрати' та 'Бюджет' для загального управління.\n\n";
    }

    @Override
    public String getBotUsername() {
        return "ProdGym_bot";
    }

    @Override
    public String getBotToken() {
        return "7062085182:AAH1nuue3IhuF26X_s_VlzlfYXjGG5vMG-8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            String chatId = update.getMessage().getChatId().toString();
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            String commandmentText = null;

            try {
                switch (text) {
                    case "/start":
                        restartApplication();
                        commandmentText = getWelcomeMessage();
                        response.setReplyMarkup(getMainMenuKeyboard());
                        break;
                    case "Тренера":
                        commandmentText = "Управління тренерами. Оберіть опцію:";
                        response.setReplyMarkup(coachs.getCoachsParkKeyboard());
                        break;
                    case "Список контрактів":
                        commandmentText = coachs.getCoachsList();
                        break;
                    case "Підписати контракт":
                        commandmentText = "Оберіть контракт для підписання:";
                        response.setReplyMarkup(coachs.getBuyCoachsKeyboard());
                        break;
                    case "Скоротити контракт":
                        commandmentText = "Оберіть контракт для скорочення:";
                        response.setReplyMarkup(coachs.getSellCoachsKeyboard());
                        break;
                    case "Спеціалізації":
                        commandmentText = "Управління спеціалізацій. Оберіть опцію:";
                        response.setReplyMarkup(specialtys.getCoachKeyboard());
                        break;
                    case "Список Спеціалізацій":
                        commandmentText = specialtys.getCoachList();
                        break;
                    case "Пройти курс спеціалізації":
                        commandmentText = "Оберіть курс для проходження спеціалізації:";
                        response.setReplyMarkup(specialtys.getHireCoachKeyboard());
                        break;
                    case "Очистити спеціалізацію":
                        commandmentText = "Оберіть спеціалізацію для очистки";
                        response.setReplyMarkup(specialtys.getFireCoachKeyboard());
                        break;
                    case "Контракти з клубами":
                        commandmentText = "Для найманя тренерів зі спецілізацією та підписання їх з спорт-клубамио.\n\n"
                                + "Sportlife (Контракт: 8000грн)\n"
                                + "Starfit (Контракт: 7000грн)\n"
                                + "PowerClub (Контракт: 7500грн)\n"
                                + "Totalfitnes (Контракт: 4000грн)\n"
                                + "Фітнес клуб Liga (Контракт: 3600грн)\n\n"
                                + "Виберіть опцію:";
                        response.setReplyMarkup(sportServises.getSportServicesKeyboard());
                        break;
                    case "Контракти":
                        commandmentText = sportServises.getSignedContractsList();
                        break;
                    case "Підписати контракт з клубом":
                        commandmentText = "Оберіть сорт-клуб для підписання контракту:";
                        response.setReplyMarkup(sportServises.getAvailableContractsKeyboard());
                        break;
                    case "Розірвати контракт з клубом":
                        commandmentText = "Оберіть сорт-клуб для розірвання контракту:";
                        response.setReplyMarkup(sportServises.getSignedContractsKeyboard());
                        break;
                    case "Сейф":
                        commandmentText = "Керування фінансами. Оберіть опцію:";
                        response.setReplyMarkup(finance.getFinanceKeyboard());
                        break;
                    case "Доходи":
                        commandmentText = finance.getFullIncomeReport();
                        break;
                    case "Витрати":
                        commandmentText = finance.getFullExpenseReport();
                        break;
                    case "Бюджет":
                        commandmentText = "Бюджет: " + finance.getBudget() + "грн";
                        break;
                    case "Назад":
                        commandmentText = "Основне меню:";
                        response.setReplyMarkup(getMainMenuKeyboard());
                        break;
                    case "Назад до управління тренерами":
                        commandmentText = "Управління контрактами тренерів. Оберіть опцію:";
                        response.setReplyMarkup(coachs.getCoachsParkKeyboard());
                        break;
                    case "Назад до управління спеціалізаціями":
                        commandmentText = "Управління спеціалізаціями. Оберіть опцію:";
                        response.setReplyMarkup(specialtys.getCoachKeyboard());
                        break;
                    case "Назад до управління контрактів з клубами":
                        commandmentText = "Для  найманя тренерів зі спецілізацією та підписання їх з спорт-клубамио.\n\n"
                                + "Sportlife (Контракт: 8000грн)\n"
                                + "Starfit (Контракт: 7000грн)\n"
                                + "PowerClub (Контракт: 7500грн)\n"
                                + "Totalfitnes (Контракт: 4000грн)\n"
                                + "Фітнес клуб Liga (Контракт: 3600грн)\n\n"
                                + "Виберіть опцію:";
                        response.setReplyMarkup(sportServises.getSportServicesKeyboard());
                        break;
                    case "Назад до управління фінансами":
                        commandmentText = "Керування фінансами. Оберіть опцію:";
                        response.setReplyMarkup(finance.getFinanceKeyboard());
                        break;
                    case "Вийти з програми":
                        commandmentText = "Натисніть 'Почати роботу', щоб запустити програму знову.";
                        response.setReplyMarkup(getRestartKeyboard());
                        break;
                    case "Почати роботу":
                        restartApplication(); // Перезапуск програми
                        commandmentText = getWelcomeMessage();
                        response.setReplyMarkup(getMainMenuKeyboard());
                        break;

                    default:
                        if (text.startsWith("Купити ")) {
                            commandmentText = coachs.buyCoachsByText(text.replace("Купити ",
                                    ""), specialtys);
                        } else if (text.startsWith("Продати ")) {
                            commandmentText = coachs.sellCoachsByText(text.replace("Продати ",
                                    ""));
                        } else if (text.startsWith("Найняти ")) {
                            commandmentText = specialtys.hireCoachByText(text.replace("Найняти ",
                                    ""), coachs);
                        } else if (text.startsWith("Звільнити ")) {
                            commandmentText = specialtys.fireCoachByText(text.replace("Звільнити ",
                                    ""));
                        } else if (text.startsWith("Підписати ")) {
                            commandmentText = sportServises.signContractByText(text.replace("Підписати ",
                                    ""));
                        } else if (text.startsWith("Розірвати ")) {
                            commandmentText = sportServises.terminateContractByText(text.replace("Розірвати ",
                                    ""));
                        } else if (text.contains(" - ")) {
                            String[] parts = text.split(" - ");

                        } else {
                            commandmentText = "Невідома команда, спробуйте ще раз.";
                        }
                        break;
                }
            } catch (Exception e) {
                commandmentText = "Виникла помилка: " + e.getMessage();
            }

            response.setText(commandmentText);
            try {
                execute(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Тренера");
        row1.add("Спеціалізації");
        row1.add("Контракти з клубами");
        KeyboardRow row2 = new KeyboardRow();

        row2.add("Сейф");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Вийти з програми");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
    private void restartApplication() {

        this.finance = new Finance(500000); // Початковий бюджет
        this.coach = new Coach();
        this.coachs = new Coachs(finance);
        this.specialtys = new Specialtys(finance);
        this.sportServises = new SportServises(finance);

        this.isRunning = true;
    }

    private ReplyKeyboardMarkup getRestartKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Почати роботу");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true); // Клавіатура зникає після вибору
        return keyboardMarkup;
    }
}