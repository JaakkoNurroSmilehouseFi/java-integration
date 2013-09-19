package se.sveaekonomi.webpay.integration.hosted.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import se.sveaekonomi.webpay.integration.WebPay;
import se.sveaekonomi.webpay.integration.hosted.payment.FakeHostedPayment;
import se.sveaekonomi.webpay.integration.order.create.CreateOrderBuilder;
import se.sveaekonomi.webpay.integration.order.row.Item;
import se.sveaekonomi.webpay.integration.util.constant.COUNTRYCODE;
import se.sveaekonomi.webpay.integration.util.constant.CURRENCY;
import se.sveaekonomi.webpay.integration.util.constant.LANGUAGECODE;
import se.sveaekonomi.webpay.integration.util.constant.PAYMENTMETHOD;

public class HostedXmlBuilderTest {

    HostedXmlBuilder xmlBuilder;
    private CreateOrderBuilder order;
    String xml;
    
    @Before
    public void setUp() {
        xmlBuilder = new HostedXmlBuilder();
        xml = "";
    }
    
    @Test
    public void testAmountXml() throws Exception {
    	order = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
				.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
    					.setAmountExVat(4)
    					.setVatPercent(25)
    					.setQuantity(1))
    			.addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"));
    	
        FakeHostedPayment payment = new FakeHostedPayment(order);
        payment.setReturnUrl("http://myurl.se") 
        	.calculateRequestValues();
        
        
        try {
            xml = xmlBuilder.getXml(payment);
        } catch (Exception e) {
            throw e;
        }
        //expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>http://myurl.se</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testXmlCancelUrl() throws Exception {
    	order = WebPay.createOrder() 
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"));
        
        FakeHostedPayment payment = new FakeHostedPayment(order);
        payment.setCancelUrl("http://www.cancel.com")
        	.setReturnUrl("http://myurl.se") 
        	.calculateRequestValues();
        
        try {
            xml = xmlBuilder.getXml(payment);
        } catch (Exception e) {
            throw e;
        }
        //expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>http://myurl.se</returnurl><cancelurl>http://www.cancel.com</cancelurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testOrderRowXml() throws Exception {
    	order = WebPay.createOrder() 
			.addOrderRow(Item.orderRow()
				.setArticleNumber("0")
	            .setName("Product")
	            .setDescription("Good product")
	            .setAmountExVat(4)
	            .setVatPercent(25)
	            .setQuantity(1)
	            .setUnit("kg"))
	        .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
		    .setCountryCode(COUNTRYCODE.SE)
		    .setCurrency(CURRENCY.SEK)
    		.setClientOrderNumber("nr26");
        
        FakeHostedPayment payment = new FakeHostedPayment(order);
        payment.setPayPageLanguageCode(LANGUAGECODE.sv)
        	.setReturnUrl("http://myurl.se")
        		.calculateRequestValues();
        
        try {
            xml = xmlBuilder.getXml(payment);
        } catch (Exception e) {
            throw e;
        }
        //expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>http://myurl.se</returnurl><currency>SEK</currency><amount>500</amount><lang>sv</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku>0</sku><name>Product</name><description>Good product</description><amount>500</amount><vat>100</vat><quantity>1</quantity><unit>kg</unit></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testDirectPaymentSpecificXml() throws Exception {
    	xml = WebPay.createOrder()
                .setCountryCode(COUNTRYCODE.SE)
                .setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
                .usePayPageDirectBankOnly()
                .setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
                .getPaymentForm()
                .getXmlMessage();
    	//expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods><exclude>BANKAXESS</exclude><exclude>PAYPAL</exclude><exclude>KORTCERT</exclude><exclude>SKRILL</exclude><exclude>SVEAINVOICESE</exclude><exclude>SVEAINVOICEEU_SE</exclude><exclude>SVEASPLITSE</exclude><exclude>SVEASPLITEU_SE</exclude><exclude>SVEAINVOICEEU_DE</exclude><exclude>SVEASPLITEU_DE</exclude><exclude>SVEAINVOICEEU_DK</exclude><exclude>SVEASPLITEU_DK</exclude><exclude>SVEAINVOICEEU_FI</exclude><exclude>SVEASPLITEU_FI</exclude><exclude>SVEAINVOICEEU_NL</exclude><exclude>SVEASPLITEU_NL</exclude><exclude>SVEAINVOICEEU_NO</exclude><exclude>SVEASPLITEU_NO</exclude></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testCardPaymentSpecificXml() throws Exception {
    	xml = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
    			.usePayPageCardOnly()
    			.setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
    			.getPaymentForm()
    			.getXmlMessage();
    	//expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods><exclude>PAYPAL</exclude><exclude>DBNORDEASE</exclude><exclude>DBSEBSE</exclude><exclude>DBSEBFTGSE</exclude><exclude>DBSHBSE</exclude><exclude>DBSWEDBANKSE</exclude><exclude>BANKAXESS</exclude><exclude>SVEAINVOICESE</exclude><exclude>SVEAINVOICEEU_SE</exclude><exclude>SVEASPLITSE</exclude><exclude>SVEASPLITEU_SE</exclude><exclude>SVEAINVOICEEU_DE</exclude><exclude>SVEASPLITEU_DE</exclude><exclude>SVEAINVOICEEU_DK</exclude><exclude>SVEASPLITEU_DK</exclude><exclude>SVEAINVOICEEU_FI</exclude><exclude>SVEASPLITEU_FI</exclude><exclude>SVEAINVOICEEU_NL</exclude><exclude>SVEASPLITEU_NL</exclude><exclude>SVEAINVOICEEU_NO</exclude><exclude>SVEASPLITEU_NO</exclude></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testPayPagePaymentSpecificXmlNullPaymentMethod() throws Exception {
    	xml = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
    			.usePayPage()
    			.setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
    			.getPaymentForm()
    			.getXmlMessage();
        //expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML, xml);
    }
    
    @Test
    public void testPayPagePaymentSetLanguageCode() throws Exception {
    	xml = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
    			.usePayPage()
    			.setPayPageLanguage(LANGUAGECODE.sv)
    			.setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
    			.getPaymentForm()
    			.getXmlMessage();
    	//expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>sv</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML,xml);
    }
    
    @Test
    public void testPayPagePaymentPayPal() throws Exception {
    	xml = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
                		.addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
    			.usePayPage()
    			.setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
    			.setPaymentMethod(PAYMENTMETHOD.PAYPAL)
    			.getPaymentForm()
    			.getXmlMessage();
    	//expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><paymentmethod>PAYPAL</paymentmethod><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML,xml);
    }
    
    @Test
    public void testPayPagePaymentSpecificXml() throws Exception {
    	xml = WebPay.createOrder()
    			.setCountryCode(COUNTRYCODE.SE)
    			.setCurrency(CURRENCY.SEK)
    			.setClientOrderNumber("nr26")
    			.addOrderRow(Item.orderRow()
                		.setQuantity(1)
                		.setAmountExVat(4)
                		.setAmountIncVat(5))
               .addCustomerDetails(Item.companyCustomer()
	                .setNationalIdNumber("666666")
	                .setEmail("test@svea.com")
	                .setPhoneNumber(999999)
	                .setIpAddress("123.123.123.123")
	                .setStreetAddress("Gatan", "23")
	                .setCoAddress("c/o Eriksson")
	                .setZipCode("9999")
	                .setLocality("Stan"))
    			.usePayPage()  
    			.setReturnUrl("https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm")
    			.setPaymentMethod(PAYMENTMETHOD.INVOICE)
    			.getPaymentForm()
    			.getXmlMessage();
        //expected xml string tested with tool in hosted admin
        final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Message generated by Integration package Java--><payment><paymentmethod>SVEAINVOICEEU_SE</paymentmethod><customerrefno>nr26</customerrefno><returnurl>https://test.sveaekonomi.se/webpay/admin/merchantresponsetest.xhtm</returnurl><currency>SEK</currency><amount>500</amount><lang>en</lang><customer><ssn>666666</ssn><housenumber>23</housenumber><zip>9999</zip><address>Gatan</address><city>Stan</city><email>test@svea.com</email><address2>c/o Eriksson</address2><email>test@svea.com</email><phone>999999</phone><country>SE</country></customer><vat>100</vat><ipaddress>123.123.123.123</ipaddress><orderrows><row><sku></sku><name></name><description></description><amount>500</amount><vat>100</vat><quantity>1</quantity></row></orderrows><excludepaymentmethods></excludepaymentmethods><iscompany>true</iscompany><addinvoicefee>false</addinvoicefee></payment>";
        assertEquals(EXPECTED_XML,xml);
    }
}
