package org.example;

import lombok.Getter;

@Getter
public enum AnsiColour {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    CYAN("\u001B[36m"),
    BRIGHT_YELLOW("\u001B[93m"),
    MAGENTA("\u001B[35m");

    private final String code;

    AnsiColour(String code) {
        this.code = code;
    }

}
