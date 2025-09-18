package ua.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class GiftIncome extends Income {
    private final String giftType;
    private final String donorName;
    private final RelationshipDegree relationshipDegree;
    
    // Неоподатковуваний мінімум для подарунків (17 прожиткових мінімумів)
    private static final BigDecimal TAX_FREE_LIMIT = BigDecimal.valueOf(35700); // 2100 * 17

    public enum RelationshipDegree {
        CLOSE_RELATIVE(0.0),      // Близькі родичі не оподатковуються
        NON_RELATIVE(18.0);     

        private final double taxRate;

        RelationshipDegree(double taxRate) {
            this.taxRate = taxRate;
        }

        public double getTaxRate() {
            return taxRate;
        }
    }

    public GiftIncome(BigDecimal amount, String description, LocalDate date,
                     String giftType, String donorName, RelationshipDegree relationshipDegree) {
        super(amount, description, date);
        
        this.giftType = giftType != null ? giftType : "Unknown gift";
        this.donorName = donorName != null ? donorName : "Unknown donor";
        this.relationshipDegree = relationshipDegree != null ? relationshipDegree : RelationshipDegree.NON_RELATIVE;
    }

    @Override
    public BigDecimal calculateTax() {
        
        if (relationshipDegree == RelationshipDegree.CLOSE_RELATIVE) {
            return BigDecimal.ZERO;
        }
        
        
        BigDecimal taxableAmount = amount.subtract(TAX_FREE_LIMIT);
        if (taxableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Розрахунок податку залежно від степеня рідства
        BigDecimal taxRate = BigDecimal.valueOf(relationshipDegree.getTaxRate());
        BigDecimal incomeTax = taxableAmount.multiply(taxRate)
                                          .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        // Військовий збір додається тільки якщо є ПДФО
        if (incomeTax.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal militaryTaxRate = BigDecimal.valueOf(TaxType.MILITARY_TAX.getDefaultRate());
            BigDecimal militaryTax = taxableAmount.multiply(militaryTaxRate)
                                                 .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return incomeTax.add(militaryTax);
        }
        
        return BigDecimal.ZERO;
    }

    @Override
    public TaxType getTaxType() {
        return TaxType.INCOME_TAX;
    }

    public BigDecimal getTaxableAmount() {
        if (relationshipDegree == RelationshipDegree.CLOSE_RELATIVE) {
            return BigDecimal.ZERO;
        }
        return amount.subtract(TAX_FREE_LIMIT).max(BigDecimal.ZERO);
    }

    
    public String getGiftType() {
        return giftType;
    }

    public String getDonorName() {
        return donorName;
    }

    public RelationshipDegree getRelationshipDegree() {
        return relationshipDegree;
    }

    public static BigDecimal getTaxFreeLimit() {
        return TAX_FREE_LIMIT;
    }

    @Override
    public String toString() {
        return String.format("Gift (%s) from %s (%s): %s UAH (%s)", 
            giftType, donorName, relationshipDegree.name().toLowerCase(), amount, date);
    }
}