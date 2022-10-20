package com.esg;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * String calculator.
 * Details see READMD.md for more details.
 */
public class StringCalculator {
    private static final String DEFAULT_DELIMITER = ",";
    private static final String DEFAULT_DELIMITER_PREFIX = "//";
    private static final String DEFAULT_DELIMITER_SUFFIX = "\n";
    private static final String DEFAULT_DELIMITER_SPLIT_REGEX = "\\[(.*?)";
    private static final int DEFAULT_ALLOW_NUMBER_RANGE_START = 0;
    private static final int DEFAULT_ALLOW_NUMBER_RANGE_END = 1000;

    /**
     * Add numeric value with predefined delimiter(s).
     * @param numbers numeric value expression.
     * @return Summary of value.
     */
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

    /**
     * Get all custom delimiters and convert to default delimiter.
     * @param numbers numeric value expressing in custom delimiter(s).
     * @return numeric value expressing in default delimiter.
     */
    private String convertWithDelimiter(String numbers) {
        List<String> delimiters = getDelimiter(numbers);
        String convertedNumbers = numbers;
        for(String delimiter : delimiters) {
            convertedNumbers = convertedNumbers.replace(delimiter, DEFAULT_DELIMITER);
        }
        return convertedNumbers;
    }

    /**
     * Convert integer into list with default delimiter.
     * @param numbers Numeric value expressing in default delimiter.
     * @return List of integer.
     */
    private List<Integer> convertToIntegers(String numbers) {
        return Arrays.stream(numbers.replace(DEFAULT_DELIMITER_SUFFIX,DEFAULT_DELIMITER).split(DEFAULT_DELIMITER)).map(o-> {
            try {
                return Integer.parseInt(o);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Indicate numeric value express contains custom delimiter or not.
     * @param numbers numeric value express.
     * @return Boolean to indicate numeric value express contains custom delimiter or not.
     */
    private boolean requiredCustomDelimiter(String numbers) {
        if(isNullOrEmpty(numbers) || numbers.length() <= DEFAULT_DELIMITER_PREFIX.length()) {
            return false;
        }
        return numbers.startsWith(DEFAULT_DELIMITER_PREFIX);
    }

    /**
     * Get delimiter(s) into list.
     * @param numbers numeric value express.
     * @return List of delimiter(s).
     */
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

    /**
     * Indicate string is null or empty.
     * @param numbers string value.
     * @return boolean to indicate string is null or empty or not.
     */
    private boolean isNullOrEmpty(String numbers) {
        return (Objects.isNull(numbers) || numbers.isEmpty() || numbers.isBlank());
    }

    /**
     * Get list of negative number(s).
     * @param numbers List of integer.
     * @return List of negative number.
     */
    private List<Integer> getNegativeNumbers(List<Integer> numbers) {
        return numbers.stream().filter(o->o < 0).collect(Collectors.toList());
    }

    /**
     * Get list of number with in predefined range.
     * @param source List of integer.
     * @return List of integer in range.
     */
    private List<Integer> getInRangeNumbers(List<Integer> source) {
        return source.stream().filter(o-> (o >= StringCalculator.DEFAULT_ALLOW_NUMBER_RANGE_START) && (o <= StringCalculator.DEFAULT_ALLOW_NUMBER_RANGE_END)).collect(Collectors.toList());
    }
}
