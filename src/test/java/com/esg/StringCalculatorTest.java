package com.esg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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
                Arguments.of("apple,v,a", 0),
                Arguments.of("1\n2,3", 6),
                Arguments.of("1,\n", 1),
                Arguments.of("//;\n1;2", 3),
                Arguments.of("1001,2", 2),
                Arguments.of("//[|||]\n1|||2|||3", 6),
                Arguments.of("//[|][%]\n1|2%3", 6)
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

    static Stream<Arguments> mockAddUnhappyCaseParameter() {
        return Stream.of(
                Arguments.of("-1,2", new NegativeNumberFoundException(List.of(-1))),
                Arguments.of("2,-4,3,-5", new NegativeNumberFoundException(List.of(-4,-5)))
        );
    }

    @ParameterizedTest(name="Given number [{0}], when run add(), then get {1} will be thrown.")
    @DisplayName("Unhappy cases for method add()")
    @MethodSource("mockAddUnhappyCaseParameter")
    void givenInvalidData_whenCallAdd_thenExceptionThrown(String numbers, Exception exception) {
        Exception targetException = Assertions.assertThrows(NegativeNumberFoundException.class, ()-> stringCalculator.add(numbers));
        assertEquals(exception.getMessage(), targetException.getMessage());
    }
}