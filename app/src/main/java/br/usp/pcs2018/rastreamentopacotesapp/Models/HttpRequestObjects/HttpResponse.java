package br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects;

public class HttpResponse {

    private boolean responseStatus;
    private String responseMessage;

    public HttpResponse(){}

    public boolean getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(boolean responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}