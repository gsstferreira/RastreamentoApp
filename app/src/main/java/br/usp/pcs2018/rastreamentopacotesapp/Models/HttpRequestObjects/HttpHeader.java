package br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects;

public class HttpHeader {

    private String key;
    private String value;

    public HttpHeader(){}

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}