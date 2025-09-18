package ua.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class ForeignTransferIncome extends Income {
    private final String senderName;
    private final String sourceCountry;
    private final TransferPurpose purpose;
    
    public enum TransferPurpose {
        SALARY("Заробітна плата", 18.0, true),
        FAMILY_SUPPORT("Підтримка сім'ї", 0.0, false),      // Не оподатковується до ліміту
        BUSINESS_INCOME("Підприємницький дохід", 18.0, true),
        OTHER("Інше", 18.0, true);

        private final String description;
        private final double taxRate;
        private final boolean isTaxable;

        TransferPurpose(String description, double taxRate, boolean isTaxable) {
            this.description = description;
            this.taxRate = taxRate;
            this.isTaxable = isTaxable;
        }

        public String getDescription() { return description; }
        public double getTaxRate() { return taxRate; }
        public boolean isTaxable() { return isTaxable; }
    }

    public ForeignTransferIncome(BigDecimal amount, String description, LocalDate date,
                                String senderName, String sourceCountry, 
                                TransferPurpose purpose) {
        super(amount, description, date);
        
        this.senderName = senderName != null ? senderName : "Unknown sender";
        this.sourceCountry = sourceCountry != null ? sourceCountry : "Unknown country";
        this.purpose = purpose != null ? purpose : TransferPurpose.OTHER;
    }

    @Override
    public BigDecimal calculateTax() {
        // Підтримка сім'ї не оподатковується
        if (!purpose.isTaxable()) {
            return BigDecimal.ZERO;
        }
        
        // Для оподатковуваних переказів: ПДФО 18% + Військовий збір 1.5%
        BigDecimal incomeTaxRate = BigDecimal.valueOf(purpose.getTaxRate());
        BigDecimal militaryTaxRate = BigDecimal.valueOf(TaxType.MILITARY_TAX.getDefaultRate());
        BigDecimal totalTaxRate = incomeTaxRate.add(militaryTaxRate);
        
        return amount.multiply(totalTaxRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public TaxType getTaxType() {
        return TaxType.INCOME_TAX;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }

    public TransferPurpose getPurpose() {
        return purpose;
    }

    @Override
    public String toString() {
        return String.format("Foreign transfer from %s (%s): %s UAH, %s (%s)", 
            senderName, sourceCountry, amount, purpose.getDescription(), date);
    }
}