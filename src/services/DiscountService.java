package services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import models.Order;

public class DiscountService {
    public void apply(Order order) {
        if(!order.canApplyDiscount()) {
            throw new IllegalStateException(
                "Pedido #" + order.getId() + " não é elegível para desconto. " + 
                "Status: " + order.getStatus().getDisplayName()
            );
        }

        BigDecimal total = order.getTotal();
        double percentage = calculatePercentage(order);
        BigDecimal discountedTotal = order.applyDiscount(percentage);
        BigDecimal percentualDiscount = total.subtract(discountedTotal);

        System.out.println("\n=== Pedido #" + order.getId() + " ===");
        System.out.println("Total original: R$ " + total);
        System.out.println("Desconto " + percentage + "%: -R$ " + percentualDiscount);

        BigDecimal finalTotal = discountedTotal;
        BigDecimal fixedDiscount = BigDecimal.ZERO;
        if(isChristmasWeek(order.getMoment())) {
            fixedDiscount = calculateFixed(discountedTotal);
            finalTotal = discountedTotal.subtract(fixedDiscount);
            System.out.println("Desconto de Natal: -R$ " + fixedDiscount);
        }

        BigDecimal totalDiscount = total.subtract(finalTotal);
        System.out.println("Total de desconto: R$ " + totalDiscount);
        System.out.println("Total final: R$ " + finalTotal);
    }

    private double calculatePercentage(Order order) {
        BigDecimal total = order.getTotal();

        if(total.compareTo(new BigDecimal("200.00")) >= 0) {
            return 15.0;
        }

        if(total.compareTo(new BigDecimal("150.00")) >= 0) {
            return 12.0;
        }

        if(total.compareTo(new BigDecimal("100.00")) >= 0) {
            return 10.0;
        }

        return 5.0;
    }

    private BigDecimal calculateFixed(BigDecimal total) {
        if(total.compareTo(new BigDecimal("120.00")) >= 0) {
            return new BigDecimal("25.00");
        }

        return BigDecimal.ZERO;
    }

    private boolean isChristmasWeek(Instant moment) {
        LocalDate date = moment.atZone(ZoneId.systemDefault()).toLocalDate();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return (month == 12 && day >= 21 && day <= 25);
    }
}

