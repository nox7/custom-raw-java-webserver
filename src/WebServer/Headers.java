package WebServer;

import java.util.Vector;

public class Headers {

    public Vector<Header> headers = new Vector<>();

    public Headers(){

    }

    public void addHeader(Header header){
        this.headers.add(header);
    }

    @Override
    public String toString(){
        String httpValidHeadersString = "";
        for(Header header : this.headers){
            String headerAsString = header.name.concat(": ").concat(header.value).concat("\r\n");
            httpValidHeadersString = httpValidHeadersString.concat(headerAsString);
        }

        return httpValidHeadersString;
    }

}
