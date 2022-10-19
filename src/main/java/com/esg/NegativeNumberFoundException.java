package com.esg;

import java.util.List;
import java.util.stream.Collectors;

public class NegativeNumberFoundException extends RuntimeException{
    public NegativeNumberFoundException(List<Integer> values) {
        super("Negatives not allowed: "+values.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
}
