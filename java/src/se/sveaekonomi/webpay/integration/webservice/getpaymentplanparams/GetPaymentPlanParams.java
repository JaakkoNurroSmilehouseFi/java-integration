package se.sveaekonomi.webpay.integration.webservice.getpaymentplanparams;

import java.net.URL;

import org.w3c.dom.NodeList;

import se.sveaekonomi.webpay.integration.config.ConfigurationProvider;

import se.sveaekonomi.webpay.integration.response.webservice.PaymentPlanParamsResponse;
import se.sveaekonomi.webpay.integration.util.constant.COUNTRYCODE;
import se.sveaekonomi.webpay.integration.util.constant.PAYMENTTYPE;
import se.sveaekonomi.webpay.integration.webservice.helper.WebServiceXmlBuilder;
import se.sveaekonomi.webpay.integration.webservice.svea_soap.SveaAuth;
import se.sveaekonomi.webpay.integration.webservice.svea_soap.SveaGetPaymentPlanParams;
import se.sveaekonomi.webpay.integration.webservice.svea_soap.SveaRequest;
import se.sveaekonomi.webpay.integration.webservice.svea_soap.SveaSoapBuilder;

public class GetPaymentPlanParams {
        
	private COUNTRYCODE countryCode;
    private ConfigurationProvider config;
    
    public GetPaymentPlanParams(ConfigurationProvider config) {
    	this.config = config;
    }
    
    public GetPaymentPlanParams setCountryCode(COUNTRYCODE countryCode) {
    	this.countryCode = countryCode;
    	return this;
    }
        
    protected SveaAuth getStoreAuthorization() {
    	SveaAuth auth = new SveaAuth();
        auth.Username = config.getUsername(PAYMENTTYPE.PAYMENTPLAN, countryCode);
        auth.Password = config.getPassword(PAYMENTTYPE.PAYMENTPLAN, countryCode);        
        auth.ClientNumber = config.getClientNumber(PAYMENTTYPE.PAYMENTPLAN, countryCode);        
        
        return auth;
    }
    
    private SveaRequest<SveaGetPaymentPlanParams> prepareRequest() {
        SveaGetPaymentPlanParams params = new SveaGetPaymentPlanParams();
        params.Auth = getStoreAuthorization();
        SveaRequest<SveaGetPaymentPlanParams> request = new SveaRequest<SveaGetPaymentPlanParams>();
        request.request = params;

        return request;
    }
    
    public PaymentPlanParamsResponse doRequest() throws Exception {
        SveaRequest<SveaGetPaymentPlanParams> request = prepareRequest();
        
        WebServiceXmlBuilder xmlBuilder = new WebServiceXmlBuilder();
        String xml = xmlBuilder.getGetPaymentPlanParamsXml(request.request);
        URL url = this.config.getEndPoint(PAYMENTTYPE.PAYMENTPLAN);
        SveaSoapBuilder soapBuilder = new SveaSoapBuilder();
        String soapMessage = soapBuilder.makeSoapMessage("GetPaymentPlanParamsEu", xml);
        NodeList soapResponse = soapBuilder.createGetPaymentPlanParamsEuRequest(soapMessage, url.toString());
        PaymentPlanParamsResponse response = new PaymentPlanParamsResponse(soapResponse);
        return response;
    }
}
