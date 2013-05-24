package se.sveaekonomi.webpay.integration.webservice.getaddresses;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.sveaekonomi.webpay.integration.WebPay;
import se.sveaekonomi.webpay.integration.response.webservice.GetAddressesResponse;
import se.sveaekonomi.webpay.integration.util.constant.COUNTRYCODE;

public class GetAddressesTest {
    
    @Test
    public void testGetAddresses() throws Exception {
        try {
            GetAddressesResponse response = WebPay.getAddresses()                
                .setCountryCode(COUNTRYCODE.SE)
                .setIndividual("460509-2222")
                .setOrderTypeInvoice()
                .doRequest();
            
            assertEquals(true, response.isOrderAccepted());
            assertEquals("Persson, Tess T", response.getLegalName());
        } catch (Exception e) {
            throw e;
        }
    }
}
