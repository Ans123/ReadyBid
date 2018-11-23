package net.readybid.app.core.entities.entity;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public enum EntityIndustry {

    ADVERTISING_AND_MARKETING("Advertising & Marketing"),
    BUSINESS_PRODUCTS_AND_SERVICES("Business Products & Services"),
    COMPUTER_HARDWARE("Computer Hardware"),
    CONSTRUCTION("Construction"),
    CONSUMER_PRODUCTS_AND_SERVICES("Consumer Products & Services"),
    EDUCATION("Education"),
    ENERGY("Energy"),
    ENGINEERING("Engineering"),
    ENVIRONMENTAL_SERVICES("Environmental Services"),
    FINANCIAL_SERVICES("Financial Services"),
    FOOD_AND_BEVERAGE("Food & Beverage"),
    GOVERNMENT_SERVICES("Government Services"),
    HEALTH("Health"),
    TRAVEL_AND_HOSPITALITY("Hospitality & Travel"),
    HUMAN_RESOURCES("Human Resources"),
    INSURANCE("Insurance"),
    IT_SERVICES("IT Services"),
    LOGISTICS_AND_TRANSPORTATION("Logistics & Transportation"),
    MANUFACTURING("Manufacturing"),
    MEDIA("Media"),
    NON_PROFIT("Non Profit"),
    REAL_ESTATE("Real Estate"),
    RETAIL("Retail"),
    SECURITY("Security"),
    SOFTWARE("Software"),
    TELECOMMUNICATIONS("Telecommunications"),
    OTHER("Other");

    private final String label;

    EntityIndustry(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public static EntityIndustry fromString(String text) {
        if (text != null) {
            for (EntityIndustry entityIndustry : EntityIndustry.values()) {
                if (text.equalsIgnoreCase(entityIndustry.toString())) {
                    return entityIndustry;
                }
            }
        }
        return null;
    }
}