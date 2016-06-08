package com.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpGet;

public class HelloWorld {

    private final HttpGet request;

    public HelloWorld() {
        request = new HttpGet("http://www.google.com/search?hl=en&q=httpclient&btnG=Google+Search&aq=f&oq=" + Base64.encodeBase64String("value".getBytes()));
    }

    public String getUri() {
        return request.getURI().toString();
    }
}
