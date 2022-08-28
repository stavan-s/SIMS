package com.stavan.sims;

public class PdfUpload {

    private String name;
    private String refName;
    private String uri;
    private String date;

    public PdfUpload(String name, String uri, String date) {
        this.name = name;
        this.refName = refName;
        this.uri = uri;
        this.date = date;
    }

    public PdfUpload() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
