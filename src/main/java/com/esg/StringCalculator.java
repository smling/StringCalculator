package com.esg;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * String calculator.
 */
public class StringCalculator {
    private static final String DEFAULT_DELIMITER = ",";
    private static final String DEFAULT_DELIMITER_PREFIX = "//";
    private static final String DEFAULT_DELIMITER_SUFFIX = "\n";
    private static final String DEFAULT_DELIMITER_SPLIT_REGEX = "\\[(.*?)";
    private static final int DEFAULT_ALLOW_NUMBER_RANGE_START = 0;
    private static final int DEFAULT_ALLOW_NUMBER_RANGE_END = 1000;

    public int add(String numbers) {
        AtomicInteger result = new AtomicInteger();
        if(isNullOrEmpty(numbers)) {
            return result.get();
        }
        String convertedNumberWithDelimiter = convertWithDelimiter(numbers);
        List<Integer> convertedNumbers = convertToIntegers(convertedNumberWithDelimiter);
        List<Integer> negativeNumbers = getNegativeNumbers(convertedNumbers);
        if(negativeNumbers.size() > 0) {
            throw new NegativeNumberFoundException(negativeNumbers);
        }
        List<Integer> convertedNumbersInRange = getInRangeNumbers(convertedNumbers);
        convertedNumbersInRange.forEach(result::addAndGet);
        return result.get();
    }
    private String convertWithDelimiter(String numbers) {
        List<String> delimiters = getDelimiter(numbers);
        String convertedNumbers = numbers;
        for(String delimiter : delimiters) {
            convertedNumbers = convertedNumbers.replace(delimiter, DEFAULT_DELIMITER);
        }
        return convertedNumbers;
    }
    private List<Integer> convertToIntegers(String numbers) {
        return Arrays.stream(numbers.replace(DEFAULT_DELIMITER_SUFFIX,DEFAULT_DELIMITER).split(DEFAULT_DELIMITER)).map(o-> {
            try {
                return Integer.parseInt(o);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }).collect(Collectors.toList());
    }

    private boolean requiredCustomDelimiter(String numbers) {
        if(isNullOrEmpty(numbers) || numbers.length() < 3) {
            return false;
        }
        return numbers.startsWith(DEFAULT_DELIMITER_PREFIX);
    }

    private List<String> getDelimiter(String numbers) {
        if(requiredCustomDelimiter(numbers)) {
            int delimiterEndIndex = numbers.indexOf(DEFAULT_DELIMITER_SUFFIX);
            String delimiterString = numbers.substring(DEFAULT_DELIMITER_PREFIX.length(),delimiterEndIndex);
            List<String> result = new ArrayList<>();
            Pattern pattern = Pattern.compile(DEFAULT_DELIMITER_SPLIT_REGEX);
            Matcher matcher = pattern.matcher(delimiterString);
            while(matcher.find()) {
                String delimiter = matcher.group(1);
                result.add(delimiter);
            }
            if(result.size() > 0) {
                return result;
            }
            return List.of(delimiterString);
        }
        return List.of(DEFAULT_DELIMITER);
    }

    private boolean isNullOrEmpty(String numbers) {
        return (Objects.isNull(numbers) || numbers.isEmpty() || numbers.isBlank());
    }

    private List<Integer> getNegativeNumbers(List<Integer> numbers) {
        return numbers.stream().filter(o->o < 0).collect(Collectors.toList());
    }

    private List<Integer> getInRangeNumbers(List<Integer> source) {
        return source.stream().filter(o-> (o >= StringCalculator.DEFAULT_ALLOW_NUMBER_RANGE_START) && (o <= StringCalculator.DEFAULT_ALLOW_NUMBER_RANGE_END)).collect(Collectors.toList());
    }
}
