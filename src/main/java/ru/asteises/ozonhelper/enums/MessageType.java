package ru.asteises.ozonhelper.enums;

import java.util.Arrays;

public enum MessageType implements Type<MessageType> {

    TETS("test");


    private final String text;


    MessageType(String text) {
        this.text = text;
    }

    @Override
    public String getName() {
        return "";
    }

    public static boolean isExist(String messageText) {
        return Arrays.stream(MessageType.values()).anyMatch(mT -> mT.text.equalsIgnoreCase(messageText));
    }

    public static MessageType getMessage(String messageText) {
        return Arrays.stream(values())
                .filter(mT -> mT.text.equalsIgnoreCase(messageText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown message text: [ %s ]", messageText
                        )));
    }

}
