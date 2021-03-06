package se.sveaekonomi.webpay.integration.webservice.getaddresses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.sveaekonomi.webpay.integration.WebPay;
import se.sveaekonomi.webpay.integration.config.SveaConfig;
import se.sveaekonomi.webpay.integration.response.webservice.GetAddressesResponse;
import se.sveaekonomi.webpay.integration.util.constant.COUNTRYCODE;
import se.sveaekonomi.webpay.integration.util.test.TestingTool;

public class GetAddressesTest {
    
    @Test
    public void testGetAddresses() {
        GetAddressesResponse response = WebPay.getAddresses(SveaConfig.getDefaultConfig())
            .setCountryCode(TestingTool.DefaultTestCountryCode)
            .setIndividual("460509-2222")
            .setOrderTypeInvoice()
            .doRequest();
        
        assertTrue(response.isOrderAccepted());
        assertEquals("Persson, Tess T", response.getLegalName());
    }
    
    @Test
    public void testResultGetAddresses() {
        GetAddressesResponse response = WebPay.getAddresses(SveaConfig.getDefaultConfig())
            .setCountryCode(TestingTool.DefaultTestCountryCode)
            .setIndividual(TestingTool.DefaultTestIndividualNationalIdNumber)
            .setOrderTypeInvoice()
            .doRequest();
        
        assertTrue(response.isOrderAccepted());
        assertEquals("Tess T", response.getFirstName());
        assertEquals("Persson", response.getLastName());
        assertEquals("Testgatan 1", response.getAddressLine2());
        assertEquals("99999", response.getPostcode());
        assertEquals("Stan", response.getPostarea());
    }
    
    @Test
    public void testResultGetIndividualAddressesNO()
    {
    	GetAddressesResponse response = WebPay.getAddresses(SveaConfig.getDefaultConfig())
                                                         .setCountryCode(COUNTRYCODE.NO)
                                                         .setOrderTypeInvoice() 
                                                         .setIndividual("17054512066")
                                                         .doRequest();

    	assertFalse(response.isOrderAccepted());
    	assertEquals("CountryCode: Supported countries are SE, DK", response.getErrorMessage());
    }

    @Test
    public void testResultGetCompanyAddressesNO()
    {
    	GetAddressesResponse response = WebPay.getAddresses(SveaConfig.getDefaultConfig())
                										 .setCountryCode(COUNTRYCODE.NO)
                                                         .setOrderTypeInvoice()
                                                         .setCompany("923313850")
                                                         .doRequest();

        assertTrue(response.isOrderAccepted());
        assertEquals("Test firma AS", response.getLegalName());
        assertEquals("Testveien 1", response.getAddressLine2());
        assertEquals("Oslo", response.getPostarea());
    }
}
