package ua.tax.model;

public enum TaxType {
    INCOME_TAX("Податок на доходи фізичних осіб", 18.0),
    MILITARY_TAX("Військовий збір", 1.5);

    private final String description;
    private final double defaultRate;

    TaxType(String description, double defaultRate) {
        this.description = description;
        this.defaultRate = defaultRate;
    }

    public String getDescription() {
        return description;
    }

    public double getDefaultRate() {
        return defaultRate;
    }
}