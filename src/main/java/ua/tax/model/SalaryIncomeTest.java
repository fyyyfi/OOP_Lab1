package ua.tax.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SalaryIncomeTest {

    @Test
    void shouldCalculateTaxCorrectly() {
        
        BigDecimal amount = BigDecimal.valueOf(10000);
        SalaryIncome salary = new SalaryIncome(amount, "Monthly salary", 
            LocalDate.of(2024, 1, 1), true, "IT Company");

        
        BigDecimal tax = salary.calculateTax();

         
        //18% + 1.5% = 19.5% of 10000 = 1950
        BigDecimal expected = BigDecimal.valueOf(1950.00);
        assertEquals(0, expected.compareTo(tax));
    }

    @Test
    void shouldReturnCorrectTaxType() {
        
        SalaryIncome salary = new SalaryIncome(BigDecimal.valueOf(5000), 
            "Salary", LocalDate.now(), true, "Company");

        
        assertEquals(TaxType.INCOME_TAX, salary.getTaxType());
    }

    @Test
    void shouldThrowExceptionForNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SalaryIncome(null, "Description", LocalDate.now(), true, "Company");
        });
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SalaryIncome(BigDecimal.valueOf(-100), "Description", 
                LocalDate.now(), true, "Company");
        });
    }
}