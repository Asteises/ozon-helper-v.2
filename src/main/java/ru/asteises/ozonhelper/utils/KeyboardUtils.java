package ru.asteises.ozonhelper.utils;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.model.ButtonDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardUtils {

    public static List<List<ButtonDto>> getButtons(List<ButtonDto> buttons) {
        ArrayList<List<ButtonDto>> buttonsList = new ArrayList<>();
        for (ButtonDto button : buttons) {
            buttonsList.add(Collections.singletonList(button));
        }
        return buttonsList;
    }

    public static ButtonDto getButton(String text, CallbackType callbackType) {
        return ButtonDto.builder().text(text).callback(callbackType).build();
    }

    public static ButtonDto getButton(String text, String webAppUrl) {
        return ButtonDto.builder().text(text).webAppUrl(webAppUrl).build();
    }

    public static InlineKeyboardButton getInlineKeyboardButton(String text, CallbackType callbackType) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackType.getName())
                .build();
    }

    public static InlineKeyboardButton getInlineKeyboardButton(ButtonDto buttonDto) {
        if (buttonDto.getCallback() == null) {
            WebAppInfo webAppInfo = WebAppInfo.builder().url(buttonDto.getWebAppUrl()).build();
            return InlineKeyboardButton.builder()
                    .text(buttonDto.getText())
                    .webApp(webAppInfo)
                    .build();
        }
        return InlineKeyboardButton.builder()
                .text(buttonDto.getText())
                .callbackData(buttonDto.getCallback().getName())
                .build();
    }

    /* ======================================
       REPLY KEYBOARD (обычные кнопки)
    ====================================== */

    /**
     * Создаёт клавиатуру из списка строк (однострочная клавиатура).
     */
    public static ReplyKeyboardMarkup getSimpleKeyboard(List<String> buttons) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (String buttonText : buttons) {
            row.add(new KeyboardButton(buttonText));
        }

        keyboard.add(row);

        return new ReplyKeyboardMarkup(keyboard, true, false, false, null, false);
    }

    /**
     * Создаёт многострочную клавиатуру из списка списков.
     */
    public static ReplyKeyboardMarkup getMultiRowKeyboard(List<List<String>> rows) {
        List<KeyboardRow> keyboard = getKeyboardRows(rows);

        return new ReplyKeyboardMarkup(keyboard, true, false, false, null, false);
    }

    /**
     * Создаёт кастомную клавиатуру с настройками.
     */
    public static ReplyKeyboardMarkup getCustomKeyboard(
            List<List<String>> rows,
            boolean resize,
            boolean oneTime,
            boolean selective,
            String placeholder,
            boolean persistent
    ) {
        List<KeyboardRow> keyboard = getKeyboardRows(rows);

        return new ReplyKeyboardMarkup(keyboard, resize, oneTime, selective, placeholder, persistent);
    }

    /* ======================================
       INLINE KEYBOARD (callbackData, URL)
    ====================================== */


    /**
     * Простой inline keyboard (одна строка).
     */
    public static InlineKeyboardMarkup getSimpleInlineKeyboard(String text, CallbackType callbackType) {
        ButtonDto button = getButton(text, callbackType);
        InlineKeyboardButton inlineKeyboardButton = getInlineKeyboardButton(button);
        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(inlineKeyboardButton);
        return new InlineKeyboardMarkup(Collections.singletonList(row));
    }

    public static InlineKeyboardMarkup getSimpleInlineKeyboard(String text, String webAppUrl) {
        ButtonDto button = getButton(text, webAppUrl);
        InlineKeyboardButton inlineKeyboardButton = getInlineKeyboardButton(button);
        InlineKeyboardRow row = new InlineKeyboardRow();

        row.add(inlineKeyboardButton);
        return new InlineKeyboardMarkup(Collections.singletonList(row));
    }

    public static InlineKeyboardMarkup getMultiRowInlineKeyboard(List<List<ButtonDto>> buttons) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (List<ButtonDto> btns : buttons) {
            InlineKeyboardRow row = new InlineKeyboardRow();
            for (ButtonDto btn : btns) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(btn.getText())
                        .callbackData(btn.getCallback().getName())
                        .build();
                row.add(button);
            }
            rows.add(row);
        }

        return new InlineKeyboardMarkup(rows);
    }

//    /**
//     * Многострочный inline keyboard (каждый вложенный список = отдельная строка).
//     */
//    public static InlineKeyboardMarkup multiRowInlineKeyboard(List<List<InlineKeyboardButton>> rows) {
//        return new InlineKeyboardMarkup(rows);
//    }
//
//    /**
//     * Быстрое создание кнопок callback из текста (автоматическое совпадение текста и callbackData).
//     */
//    public static InlineKeyboardMarkup inlineKeyboardWithSameData(List<List<String>> rows) {
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//
//        for (List<String> rowTexts : rows) {
//            List<InlineKeyboardButton> row = new ArrayList<>();
//            for (String text : rowTexts) {
//                row.add(InlineKeyboardButton.builder()
//                        .text(text)
//                        .callbackData(text) // callback = текст
//                        .build());
//            }
//            keyboard.add(row);
//        }
//
//        return new InlineKeyboardMarkup(keyboard);
//    }
//
//    /**
//     * Inline keyboard с URL-кнопками.
//     */
//    public static InlineKeyboardMarkup inlineKeyboardWithUrls(List<List<String>> texts, List<List<String>> urls) {
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//
//        for (int i = 0; i < texts.size(); i++) {
//            List<InlineKeyboardButton> row = new ArrayList<>();
//            for (int j = 0; j < texts.get(i).size(); j++) {
//                row.add(InlineKeyboardButton.builder()
//                        .text(texts.get(i).get(j))
//                        .url(urls.get(i).get(j))
//                        .build());
//            }
//            keyboard.add(row);
//        }
//
//        return new InlineKeyboardMarkup(keyboard);
//    }

    @NotNull
    private static List<KeyboardRow> getKeyboardRows(List<List<String>> rows) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (List<String> rowTexts : rows) {
            KeyboardRow row = new KeyboardRow();
            for (String buttonText : rowTexts) {
                row.add(new KeyboardButton(buttonText));
            }
            keyboard.add(row);
        }
        return keyboard;
    }

}
