package se.sveaekonomi.webpay.integration.hosted.helper;

import java.util.ArrayList;

import se.sveaekonomi.webpay.integration.hosted.HostedOrderRowBuilder;
import se.sveaekonomi.webpay.integration.order.OrderBuilder;
import se.sveaekonomi.webpay.integration.order.create.CreateOrderBuilder;
import se.sveaekonomi.webpay.integration.order.row.FixedDiscountBuilder;
import se.sveaekonomi.webpay.integration.order.row.OrderRowBuilder;
import se.sveaekonomi.webpay.integration.order.row.RelativeDiscountBuilder;
import se.sveaekonomi.webpay.integration.order.row.ShippingFeeBuilder;

public class HostedRowFormatter {

    private Long totalAmount;
    private Long totalVat;
    private ArrayList<HostedOrderRowBuilder> newRows;
    
    public HostedRowFormatter() {
        totalAmount = 0L;
        totalVat = 0L;
        newRows = new ArrayList<HostedOrderRowBuilder>();
    }
    
    public ArrayList<HostedOrderRowBuilder> formatRows(CreateOrderBuilder order) {
        formatOrderRows(order);
        formatShippingFeeRows(order);
        formatFixedDiscountRows(order);
        formatRelativeDiscountRows(order);
        
        return newRows;
    }

    private <T extends OrderBuilder<T>> void formatOrderRows(OrderBuilder<T> orderBuilder) {
        for (OrderRowBuilder row : orderBuilder.getOrderRows()) {
            HostedOrderRowBuilder tempRow = new HostedOrderRowBuilder();
            
            double vatFactor = row.getVatPercent() != null ? (row.getVatPercent() * 0.01) + 1 : 0;
            
            if (row.getName() != null) {
                tempRow.setName(row.getName());
            }
            
            if (row.getDescription() != null) {
                tempRow.setDescription(row.getDescription());
            }
            
            double amountExVat =  row.getAmountExVat()!= null ? row.getAmountExVat() : 0;
            tempRow.setAmount(new Double((amountExVat * 100) * vatFactor).longValue());
            
            if (row.getAmountExVat() != null && row.getVatPercent() != null) {
                tempRow.setAmount(new Double((row.getAmountExVat() *100) * vatFactor).longValue());
                tempRow.setVat(new Double(tempRow.getAmount() - (row.getAmountExVat() * 100)).longValue());
            } else if (row.getAmountIncVat() != null && row.getVatPercent() != null) {
                tempRow.setAmount(new Double((row.getAmountIncVat() * 100)).longValue());
                tempRow.setVat(new Double(tempRow.getAmount() - (tempRow.getAmount() / vatFactor)).longValue());
            } else {
                tempRow.setAmount(new Double(row.getAmountIncVat() * 100).longValue());
                tempRow.setVat(new Double((row.getAmountIncVat() - row.getAmountExVat()) * 100).longValue());
            }
            
            tempRow.setQuantity((tempRow.getQuantity() == null ? 0.0 : tempRow.getQuantity()));
            if (tempRow.getQuantity() >= 0) {
                tempRow.setQuantity(row.getQuantity());
            }
            
            if (row.getUnit() != null) {
                tempRow.setUnit(row.getUnit());
            }
            
            if (null != row.getArticleNumber()) {
                tempRow.setSku(row.getArticleNumber());
            }
            
            newRows.add(tempRow);
            totalAmount += (long)(tempRow.getAmount() * row.getQuantity());
            totalVat += (long)(tempRow.getVat() * row.getQuantity());
        }
    }
    
    private <T extends OrderBuilder<T>> void formatShippingFeeRows(OrderBuilder<T> orderBuilder) {
        if (orderBuilder.getShippingFeeRows() == null) {
            return;
        }
        
        for (ShippingFeeBuilder row : orderBuilder.getShippingFeeRows()) {
            HostedOrderRowBuilder tempRow = new HostedOrderRowBuilder();
            double plusVatCounter = row.getVatPercent() != null ? (row.getVatPercent() * 0.01) + 1 : 0;
            
            if (row.getName() != null) {
                tempRow.setName(row.getName());
            }
            
            if (row.getDescription() != null) {
                tempRow.setDescription(row.getDescription());
            }
            
            if (row.getAmountExVat() != null && row.getVatPercent() != null) {
                tempRow.setAmount(new Double((row.getAmountExVat() * 100) * plusVatCounter).longValue());
                tempRow.setVat(new Double(tempRow.getAmount() - (row.getAmountExVat() * 100)).longValue());
            } else if (row.getAmountIncVat() != null && row.getVatPercent() != null ) {
                tempRow.setAmount(new Double(row.getAmountIncVat() * 100).longValue());
                tempRow.setVat(new Double(tempRow.getAmount() - (tempRow.getAmount() / plusVatCounter)).longValue());
            } else {
                Double amountIncVat = row.getAmountIncVat()!= null ? row.getAmountIncVat() : 0;
                tempRow.setAmount(new Double(amountIncVat * 100).longValue());
                double amountExVat = row.getAmountExVat() != null ? row.getAmountExVat() : 0;
                tempRow.setVat(new Double(amountIncVat - amountExVat).longValue());
            }
            
            tempRow.setQuantity(1.0);
            
            if (row.getUnit() != null) {
                tempRow.setUnit(row.getUnit());
            }
            
            if (row.getShippingId() != null) {
                tempRow.setSku(row.getShippingId());
            }
            
            newRows.add(tempRow);
        }
    }
    
    private void formatFixedDiscountRows(CreateOrderBuilder orderBuilder) {
        if (orderBuilder.getFixedDiscountRows() == null) {
            return;
        }
        
        for (FixedDiscountBuilder row : orderBuilder.getFixedDiscountRows()) {
            HostedOrderRowBuilder tempRow = new HostedOrderRowBuilder();
            
            if (row.getName() != null) {
                tempRow.setName(row.getName());
            }
            
            if (row.getDescription() != null) {
                tempRow.setDescription(row.getDescription());
            }
            
            tempRow.setAmount(new Double(-(row.getAmount() * 100)).longValue());
            
            totalAmount -= new Double(row.getAmount()).longValue();
            
            double discountFactor = tempRow.getAmount() * 1.0 / totalAmount;
            
            if (totalVat > 0) {
                tempRow.setVat(new Double(totalVat * discountFactor).longValue());
            }
            
            tempRow.setQuantity(1.0);
            
            if (row.getUnit() != null) {
                tempRow.setUnit(row.getUnit());
            }
            
            if (row.getDiscountId() != null) {
                tempRow.setSku(row.getDiscountId());
            }
            
            newRows.add(tempRow);
        }
    }
    
    private void formatRelativeDiscountRows(CreateOrderBuilder orderBuilder) {
        if (orderBuilder.getRelativeDiscountRows() == null) {
            return;
        }
        
        for (RelativeDiscountBuilder row : orderBuilder.getRelativeDiscountRows()) {
            HostedOrderRowBuilder tempRow = new HostedOrderRowBuilder();
            double discountFactor = (row.getDiscountPercent() == null ? 0.0 : row.getDiscountPercent()) * 0.01;
            
            if (row.getName() != null) {
                tempRow.setName(row.getName());
            }
            
            if (row.getDescription() != null) {
                tempRow.setDescription(row.getDescription());
            }
            
            if (row.getDiscountId() != null) {
                tempRow.setSku(row.getDiscountId());
            }
            
            tempRow.setQuantity(1.0);
            
            if (row.getUnit() != null) {
                tempRow.setUnit(row.getUnit());
            }
            
            tempRow.setAmount(new Double(-(discountFactor * totalAmount)).longValue());
            totalAmount -= tempRow.getAmount();
            
            if (totalVat > 0) {
                tempRow.setVat(new Double(-(totalVat * discountFactor)).longValue());
            }
            
            newRows.add(tempRow);
        }
    }
    
    public Long formatTotalAmount(ArrayList<HostedOrderRowBuilder> rows) {
        Long amount = 0L;
        
        for (HostedOrderRowBuilder row : rows) {
            amount += (long)(row.getAmount() * (row.getQuantity() == null ? 0.0 : row.getQuantity()) );
        }
        
        return amount;
    }
    
    public Long formatTotalVat(ArrayList<HostedOrderRowBuilder> rows) {
        Long vat = 0L;
        
        for (HostedOrderRowBuilder row : rows) {
            vat += (long)(row.getVat() * (row.getQuantity() == null ? 0.0 : row.getQuantity()));
        }
        
        return vat;
    }
}
