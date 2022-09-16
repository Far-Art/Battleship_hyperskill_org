package org.hyperskill.Battleship.services.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class RetryService {

    public RetryService retryWhile(Supplier<Boolean> supplier) {
        return retry(supplier, -1, null);
    }

    public RetryService retryFor(Supplier<Boolean> supplier, int retryTimes) {
        return retry(supplier, retryTimes, null);
    }

    public RetryService retryFor(Supplier<Boolean> supplier, int retryTimes, Supplier<Void> defaultCase) {
        return retry(supplier, retryTimes, defaultCase);
    }

    private RetryService retry(Supplier<Boolean> supplier, int retryTimes, Supplier<Void> defaultCase) {
        boolean retry;
        boolean stop;
        int retries = 1;
        do {
            try {
                retry = !supplier.get();
            } catch (Exception e) {
                retry = true;
                System.out.println("Error! " + e.getMessage());
            }

            stop = retryTimes >= 0 && retries == retryTimes;

            if (retry && !stop) {
                System.out.printf("Please try again%s%n", retryTimes >= 0 ? String.format(" (%s %s left)", (retryTimes - retries), retryTimes - retries == 1 ? "try" : "tries") : "");
            } else if (stop && defaultCase != null) {
                defaultCase.get();
            } else if (stop) {
                throw new RejectedExecutionException("Retries has reached its maximum of " + retryTimes);
            }
            retries++;
        } while (retry && !stop);
        return this;
    }

    public RetryService then(Consumer<Void> consumer) {
        consumer.accept(null);
        return this;
    }
}
