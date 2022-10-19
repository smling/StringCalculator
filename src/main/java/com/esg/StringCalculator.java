package com.esg;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * String calculator.
 */
public class StringCalculator {
    public int add(String number) {
        AtomicInteger result = new AtomicInteger();
        if(Objects.isNull(number) || number.isEmpty() || number.isBlank()) {
            return result.get();
        }
        List<Integer> convertedIntegers = convertToIntegers(number);
        convertedIntegers.forEach(result::addAndGet);
        return result.get();
    }

    private List<Integer> convertToIntegers(String numbers) {
        return Arrays.stream(numbers.split(",")).map(o-> {
            try {
                return Integer.parseInt(o);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }).collect(Collectors.toList());
    }
}
