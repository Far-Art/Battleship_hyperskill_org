package org.hyperskill.Battleship.services.impl;

import org.springframework.stereotype.Service;

import java.io.FilterInputStream;
import java.util.Scanner;

@Service
public class UserInputService {

    public final static String newLine = System.lineSeparator();
    public final static String lineSeparator = "---------------------------------------------------------------------------------";
    public final static String verticalDots;
    private final static String userInputPlaceholder = "> ";

    static {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(newLine + ".");
        }
        verticalDots = builder.toString();
    }

    public String getInput() {
        // System.in wrapped in FilterInputStream to prevent closing when scanner is closed
        // System.in cannot be reopened after being closed
        try (Scanner scanner = new Scanner(new FilterInputStream(System.in) {
            public void close() {
            }
        })) {
            System.out.print(userInputPlaceholder);
            String input = scanner.nextLine();
            return input;
        }
    }
}
