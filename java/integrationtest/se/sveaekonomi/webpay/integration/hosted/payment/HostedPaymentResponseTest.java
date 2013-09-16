package se.sveaekonomi.webpay.integration.hosted.payment;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.xml.sax.SAXException;

import se.sveaekonomi.webpay.integration.WebPay;
import se.sveaekonomi.webpay.integration.hosted.helper.PaymentForm;
import se.sveaekonomi.webpay.integration.order.create.CreateOrderBuilder;
import se.sveaekonomi.webpay.integration.order.row.Item;
import se.sveaekonomi.webpay.integration.util.constant.COUNTRYCODE;
import se.sveaekonomi.webpay.integration.util.constant.CURRENCY;
import se.sveaekonomi.webpay.integration.util.constant.PAYMENTMETHOD;
import se.sveaekonomi.webpay.integration.util.constant.PAYMENTTYPE;
import se.sveaekonomi.webpay.integration.util.security.HashUtil;
import se.sveaekonomi.webpay.integration.util.security.HashUtil.HASHALGORITHM;
import se.sveaekonomi.webpay.integration.util.test.TestingTool;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class HostedPaymentResponseTest {
    
    @Test
    public void testDoCardPaymentRequest() throws Exception {
        HttpUnitOptions.setScriptingEnabled( false );
        
        PaymentForm form = WebPay.createOrder()
            .addOrderRow(TestingTool.createOrderRow())
            .addCustomerDetails(Item.companyCustomer()
                .setVatNumber("2345234")
                .setCompanyName("TestCompagniet"))
            .setCountryCode(COUNTRYCODE.SE)
            .setClientOrderNumber(Long.toString((new Date()).getTime()))
            .setCurrency(CURRENCY.SEK)
            .usePayPageCardOnly()
            .setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtml")
            .getPaymentForm();
        
        WebResponse result = postRequest(form);
        
        assertEquals("OK", result.getResponseMessage());
    }
    
    @Test
    public void testNordeaSePaymentRequest() throws Exception {
        HttpUnitOptions.setScriptingEnabled( false );
        
        PaymentForm form = WebPay.createOrder()
            .addOrderRow(TestingTool.createOrderRow())
            .addCustomerDetails(Item.companyCustomer()
                .setVatNumber("2345234")
                .setCompanyName("TestCompagniet"))
            .setCountryCode(COUNTRYCODE.SE)
            .setClientOrderNumber(Long.toString((new Date()).getTime()))
            .setCurrency(CURRENCY.SEK)
            .usePaymentMethod(PAYMENTMETHOD.NORDEA_SE)
            .setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtml")
            .getPaymentForm();
        
        WebResponse result = postRequest(form);
        
        assertEquals("OK", result.getResponseMessage());
    }
    
    private WebResponse postRequest(PaymentForm form) throws IOException, SAXException {
        WebConversation conversation = new WebConversation();
        CreateOrderBuilder order = WebPay.createOrder();
        WebRequest request = new PostMethodWebRequest(order.getConfig().getEndPoint(PAYMENTTYPE.HOSTED).toString());
        
        form.setMacSha512(HashUtil.createHash(form.getXmlMessageBase64() + order.getConfig().getSecret(PAYMENTTYPE.HOSTED, order.getCountryCode()), HASHALGORITHM.SHA_512));
        request.setParameter("mac", form.getMacSha512());
        request.setParameter("message", form.getXmlMessageBase64());
        request.setParameter("merchantid", form.getMerchantId());
        
        return conversation.getResponse(request);
    }
}
