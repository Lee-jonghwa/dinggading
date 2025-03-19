package com.mickey.dinggading;

import static org.junit.jupiter.api.Assertions.*;

import com.mickey.dinggading.model.Owner;
import com.mickey.dinggading.model.Visit;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CalculatorTest {
    @Test
    public void add() {
        Calculator calc = new Calculator();
        int add = calc.add(1, 2);
        assertEquals(3, add);
        Visit asd1 = Visit.builder()
                .date(LocalDate.MIN)
                .description("asd1")
                .id(1)
                .petId(2)
                .build();

    }
}