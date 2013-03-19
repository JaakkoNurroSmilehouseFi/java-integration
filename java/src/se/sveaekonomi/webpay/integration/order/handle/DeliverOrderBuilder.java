package se.sveaekonomi.webpay.integration.order.handle;

import java.net.URL;

import javax.xml.bind.ValidationException;

import se.sveaekonomi.webpay.integration.config.Config;
import se.sveaekonomi.webpay.integration.config.SveaConfig;
import se.sveaekonomi.webpay.integration.order.OrderBuilder;
import se.sveaekonomi.webpay.integration.order.validator.HandleOrderValidator;
import se.sveaekonomi.webpay.integration.util.constant.DISTRIBUTIONTYPE;
import se.sveaekonomi.webpay.integration.webservice.handleorder.HandleOrder;

/**
 * 
 * @author klar-sar
 *
 */
public class DeliverOrderBuilder extends OrderBuilder<DeliverOrderBuilder> {
    private HandleOrderValidator validator;
    
    private long orderId;
    private String orderType;
    private String distributionType;
    private String invoiceIdToCredit;
    private Integer numberOfCreditDays;
    public final SveaConfig config = new SveaConfig();
    private Config configMode;
    
    
    public DeliverOrderBuilder(Config config) {
        this.configMode = config;
    }

    public URL getPayPageUrl() {
    	return this.configMode.getPayPageUrl();
    }
    
    public URL getWebserviceUrl() {
    	return this.configMode.getWebserviceUrl();
    }
    
    public HandleOrderValidator getValidator() {
        return validator;
    }
    
    public DeliverOrderBuilder setValidator(HandleOrderValidator validator) {
        this.validator = validator;
        return this;
    }
    
    public long getOrderId() {
        return orderId;
    }
    
    public DeliverOrderBuilder setOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }
    
    public String getOrderType() {
        return orderType;
    }
    
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    public String getInvoiceDistributionType() {
        return distributionType;
    }
    
    public DeliverOrderBuilder setInvoiceDistributionType(DISTRIBUTIONTYPE type) {
        this.distributionType = type.toString();
        return this;
    }
    
/*    public DeliverOrderBuilder setInvoiceDistributionTypeAsEmail() {
        this.distributionType = "Email";
        return this;
    }*/
    
    public String getInvoiceIdToCredit() {
        return invoiceIdToCredit;
    }
    
    public DeliverOrderBuilder setInvoiceIdToCredit(String invoiceIdToCredit) {
        this.invoiceIdToCredit = invoiceIdToCredit;
        return this;
    }

    public Integer getNumberOfCreditDays() {
        return numberOfCreditDays;
    }
    
    public DeliverOrderBuilder setNumberOfCreditDays(int numberOfCreditDays) {
        this.numberOfCreditDays = numberOfCreditDays;
        return this;
    }
    
    /**
     * Updates the invoice order with additional information and prepares it for delivery.
     * Will automatically match all order rows that are to be delivered with those which was sent
     * when creating the invoice order.
     * @return HandleOrder
     * @throws ValidationException
     */
    public HandleOrder deliverInvoiceOrder() throws ValidationException {
        orderType = "Invoice";
        return new HandleOrder(this);
    }

    /**
     * Prepares the PaymentPlan order for delivery.
     * @return HandleOrder
     * @throws ValidationException
     */
    public HandleOrder deliverPaymentPlanOrder() throws ValidationException {
        orderType = "PaymentPlan";
        return new HandleOrder(this);
    }
    
    
}
