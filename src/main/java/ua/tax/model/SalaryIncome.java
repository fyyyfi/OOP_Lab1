package ua.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

// основна зп 
 
public class SalaryIncome extends Income {
    private final boolean isMainWorkplace;
    private final String employerName;

    public SalaryIncome(BigDecimal amount, String description, LocalDate date, 
                       boolean isMainWorkplace, String employerName) {
        super(amount, description, date);
        this.isMainWorkplace = isMainWorkplace;
        this.employerName = employerName != null ? employerName : "Unknown employer";
    }

    @Override
    public BigDecimal calculateTax() {
        // пдфо + військовий збір 
        BigDecimal incomeTaxRate = BigDecimal.valueOf(TaxType.INCOME_TAX.getDefaultRate());
        BigDecimal militaryTaxRate = BigDecimal.valueOf(TaxType.MILITARY_TAX.getDefaultRate());
        
        BigDecimal totalTaxRate = incomeTaxRate.add(militaryTaxRate);
        
        return amount.multiply(totalTaxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public TaxType getTaxType() {
        return TaxType.INCOME_TAX;
    }

    public boolean isMainWorkplace() {
        return isMainWorkplace;
    }

    public String getEmployerName() {
        return employerName;
    }

    @Override
    public String toString() {
        return String.format("Salary from %s (%s): %s UAH (%s)", 
            employerName,
            isMainWorkplace ? "main" : "additional",
            amount, 
            date);
    }
}