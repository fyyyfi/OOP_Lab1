package ua.tax.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Income {
    protected BigDecimal amount;
    protected String description;
    protected LocalDate date

    protected Income(BigDecimal amount, String description, LocalDate date) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        
        this.amount = amount
        this.description = description
        this.date = date
    }

    
    public abstract BigDecimal calculateTax();

   
    public abstract TaxType getTaxType();

    
    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Income income = (Income) obj;
        return Objects.equals(amount, income.amount) &&
               Objects.equals(description, income.description) &&
               Objects.equals(date, income.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, description, dae);
    }

    @Override
    public String toString() {
        return String.format("%s: %s UAH (%s)", 
            description, amount, date);
    }
}