package org.hyperskill.Battleship.services.impl;

import org.springframework.stereotype.Service;

import java.io.FilterInputStream;
import java.util.Scanner;

@Service
public class UserInputService {

    public final static String userInputPlaceholder = "> ";

    public final static String lineSeparator = "---------------------------------------------------------------------------------";

    public String getInput() {
        // System.in wrapped in FilterInputStream to prevent closing when scanner is closed
        // System.in cannot be reopened after being closed
        try (Scanner scanner = new Scanner(new FilterInputStream(System.in) {
            public void close() {
            }
        })) {
            return scanner.nextLine();
        }
    }
}
