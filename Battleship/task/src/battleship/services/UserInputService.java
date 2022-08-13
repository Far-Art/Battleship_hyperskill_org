package battleship.services;

import java.io.FilterInputStream;
import java.util.Scanner;

public class UserInputService {

    public String getInput() {
        // System.in wrapped in FilterInputStream to prevent closing when scanner is closed
        // System.in cannot be reopened after being closed
        try (Scanner scanner = new Scanner(new FilterInputStream(System.in) {public void close() {}})) {
            return scanner.nextLine();
        }
    }
}
