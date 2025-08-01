package ru.asteises.ozonhelper.enums;

import java.util.Arrays;


public enum CallbackType implements Type<CallbackType> {

    REGISTER("register"),
    SENT_OZON_CLIENT_ID("Client ID"),
    SENT_OZON_SELLER_API_KEY("Seller API Key");

    private final String name;

    CallbackType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static boolean isExist(String callbackText) {
        return Arrays.stream(CallbackType.values()).anyMatch(callback -> callback.name.equalsIgnoreCase(callbackText));
    }


    public static CallbackType getCallback(String callbackText) {
        return Arrays.stream(values())
                .filter(callback -> callback.name.equalsIgnoreCase(callbackText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown callback: [ %s ]", callbackText
                        )));
    }
}
