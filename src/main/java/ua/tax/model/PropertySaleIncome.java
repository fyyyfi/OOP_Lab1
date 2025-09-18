package ua.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PropertySaleIncome extends Income {
    private final BigDecimal purchasePrice;
    private final LocalDate purchaseDate;

    public PropertySaleIncome(BigDecimal amount, String description, LocalDate date,
                             BigDecimal purchasePrice, LocalDate purchaseDate) {
        super(amount, description, date);
        
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("ціна не може бути нулем або нетивним числом");
        }
        if (purchaseDate == null || purchaseDate.isAfter(date)) {
            throw new IllegalArgumentException("дата купівлі не може бути пізніше дати продажу");
        }
        
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    @Override
    public BigDecimal calculateTax() {
        // Розрахунок прибутку
        BigDecimal profit = amount.subtract(purchasePrice);
        
        // Якщо прибутку немає, податок не нараховується
        if (profit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Перевіряємо пільгу за термін володіння
        long yearsOwned = purchaseDate.until(date).getYears();
        if (yearsOwned >= 3) {
            return BigDecimal.ZERO; 
        }
        
        // ПДФО 18% + Військовий збір 1.5%
        BigDecimal incomeTaxRate = BigDecimal.valueOf(TaxType.INCOME_TAX.getDefaultRate());
        BigDecimal militaryTaxRate = BigDecimal.valueOf(TaxType.MILITARY_TAX.getDefaultRate());
        BigDecimal totalTaxRate = incomeTaxRate.add(militaryTaxRate);
        
        return profit.multiply(totalTaxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public TaxType getTaxType() {
        return TaxType.INCOME_TAX;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public long getOwnershipYears() {
        return purchaseDate.until(date).getYears();
    }

    @Override
    public String toString() {
        return String.format("Property sale: %s UAH, owned for %d years (%s)", 
            amount, getOwnershipYears(), date);
    }
}