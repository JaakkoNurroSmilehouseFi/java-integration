package se.sveaekonomi.webpay.integration;

import se.sveaekonomi.webpay.integration.order.row.Item;
import se.sveaekonomi.webpay.integration.order.row.OrderRowBuilder;

public class TestingTool {

	public static OrderRowBuilder createOrderRow() {
		return Item.orderRow()
                .setArticleNumber("1")
                .setName("Prod")
                .setDescription("Specification")
                .setAmountExVat(100.00)
                .setQuantity(2)
                .setUnit("st")
                .setVatPercent(25)
                .setVatDiscount(0); 
	}
}
