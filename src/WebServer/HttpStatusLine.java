package WebServer;

public class HttpStatusLine {

    public String httpVersion = "1.1";
    public int responseCode = 200;
    public String responseMessage = "OK";

    public HttpStatusLine(String httpVersion, int responseCode, String responseMessage){
        this.httpVersion = httpVersion;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString(){
        return "HTTP/"
                .concat(this.httpVersion)
                .concat(" ")
                .concat(String.valueOf(this.responseMessage))
                .concat(" ")
                .concat(this.responseMessage)
                .concat("\r\n");

    }
}
