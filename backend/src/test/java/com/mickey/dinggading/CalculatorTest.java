package com.mickey.dinggading;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculatorTest {
    @Test
    public void add() {
        Calculator calc = new Calculator();
        int add = calc.add(1, 2);
        assertEquals(3, add);
    }
}