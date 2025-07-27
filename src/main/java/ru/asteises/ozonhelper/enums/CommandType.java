package ru.asteises.ozonhelper.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CommandType {
    START("start");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public static boolean isExist(String commandText) {
        return Arrays.stream(CommandType.values()).anyMatch(command -> command.name.equalsIgnoreCase(commandText));
    }

    public static CommandType getCommand(String commandText) {
        return Arrays.stream(values())
                .filter(command -> command.name.equalsIgnoreCase(commandText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown command: [ %s ]", commandText
                        )));
    }
}
