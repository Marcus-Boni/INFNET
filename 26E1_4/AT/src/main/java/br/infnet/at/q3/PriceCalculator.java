package br.infnet.at.q3;

public class PriceCalculator {

    public double calculatePrice(double basePrice, int customerTypeCode, boolean holidayPurchase) {
        CustomerType customerType = CustomerType.fromCode(customerTypeCode);
        return calculatePrice(basePrice, customerType, holidayPurchase);
    }

    public double calculatePrice(double basePrice, CustomerType customerType, boolean holidayPurchase) {
        double customerDiscountRate = getCustomerDiscountRate(customerType);
        double holidayDiscountRate = getHolidayDiscountRate(holidayPurchase);
        double totalDiscountRate = customerDiscountRate + holidayDiscountRate;
        double discountMultiplier = 1 - totalDiscountRate;

        return basePrice * discountMultiplier;
    }

    private double getCustomerDiscountRate(CustomerType customerType) {
        return customerType.baseDiscount();
    }

    private double getHolidayDiscountRate(boolean holidayPurchase) {
        return holidayPurchase ? 0.05 : 0.00;
    }
}
