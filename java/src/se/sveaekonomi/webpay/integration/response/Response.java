package se.sveaekonomi.webpay.integration.response;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Response {
    
    private boolean isOrderAccepted;
    private String resultCode;
    private String errorMessage;
    
    public Response(NodeList soapMessage) {
    	if (soapMessage == null) {
    		return;
    	}
    	
        int size = soapMessage.getLength();
        
        for (int i = 0; i < size; i++) {
            Element node = (Element) soapMessage.item(i);
            this.setOrderAccepted(Boolean.parseBoolean(getTagValue(node, "Accepted")));
            this.setResultCode(getTagValue(node, "ResultCode"));
            this.setErrorMessage(getTagValue(node, "ErrorMessage"));
        }
    }
    
    protected String getTagValue(Element elementNode, String tagName) {
        NodeList nodeList = elementNode.getElementsByTagName(tagName);
        Element element = (Element) nodeList.item(0);
        
        if (element != null && element.hasChildNodes()) {
            NodeList textList = element.getChildNodes();
            return ((Node) textList.item(0)).getNodeValue().trim();
        }
        
        return null;
    }
     
    public boolean isOrderAccepted() {
        return isOrderAccepted;
    }

    public void setOrderAccepted(boolean isOrderAccepted) {
        this.isOrderAccepted = isOrderAccepted;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
