package WebServer.Request;

import WebServer.Body;
import WebServer.Header;
import WebServer.Headers;
import WebServer.HttpRequestLine;

import java.util.Vector;

public class Request {
    private HttpRequestLine httpRequestLine;
    public String method;
    public String uri;
    public String httpVersion;
    public Headers headers = new Headers();
    public Body body;

    public Request(HttpRequestLine requestLine){
        this.httpRequestLine = requestLine;
        this.method = requestLine.httpMethod;
        this.uri = requestLine.requestURI;
        this.httpVersion = requestLine.httpVersion;
        this.body = null;
    }

    public Header getHeader(String headerName){
        for (Header header : this.headers.headers){
            System.out.println(header.name);
            if (header.name.equalsIgnoreCase(headerName)){
                return header;
            }
        }

        return null;
    }
}
