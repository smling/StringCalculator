package com.esg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringCalculatorTest {
    StringCalculator stringCalculator = new StringCalculator();

    static Stream<Arguments> mockAddHappyCaseParameter() {
        return Stream.of(
                Arguments.of("", 0),
                Arguments.of("1", 1),
                Arguments.of("1,2", 3),
                Arguments.of("1,2,a", 3),
                Arguments.of("apple,v,a", 0)
        );
    }
    @ParameterizedTest(name="Given number [{0}], when run add(), then get {1}.")
    @DisplayName("Happy cases for method add()")
    @MethodSource("mockAddHappyCaseParameter")
    void givenValidData_whenCallAdd_thenGetCorrectResult(String numbers, int expectedValue) {
        assertDoesNotThrow(()-> {
            int actualValue = stringCalculator.add(numbers);
            assertEquals(expectedValue, actualValue);
        });
    }
}