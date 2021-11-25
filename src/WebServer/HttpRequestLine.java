package WebServer;

public class HttpRequestLine {

    public String httpMethod;
    public String requestURI;
    public String httpVersion = "1.1";

    /**
     * Parses an HttpRequestLine from a raw string - if it can
     */
    public static HttpRequestLine fromRaw(String rawRequestLine){
        String parsingMode = "METHOD";
        char[] chars = rawRequestLine.toCharArray();
        StringBuilder httpMethodBuilder = new StringBuilder();
        StringBuilder requestURIBuilder = new StringBuilder();
        StringBuilder httpVersionBuilder = new StringBuilder();
        for (char c : chars){
            switch (parsingMode) {
                case "METHOD":
                    if (c == ' ') {
                        parsingMode = "REQUEST_URI";
                    } else {
                        httpMethodBuilder.append(c);
                    }
                    break;
                case "REQUEST_URI":
                    if (c == ' ') {
                        parsingMode = "HTTP_STRING_IDENTIFIER";
                    } else {
                        requestURIBuilder.append(c);
                    }
                    break;
                case "HTTP_STRING_IDENTIFIER":
                    if (c == '/') {
                        parsingMode = "VERSION";
                    }
                    break;
                case "VERSION":
                    httpVersionBuilder.append(c);
                    break;
            }
        }

        return new HttpRequestLine(httpMethodBuilder.toString(), requestURIBuilder.toString(), httpVersionBuilder.toString());
    }

    public HttpRequestLine(String httpMethod, String requestURI, String httpVersion){
        this.httpMethod = httpMethod;
        this.requestURI = requestURI;
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString(){
        return this.httpMethod
                .concat(" ")
                .concat(this.requestURI)
                .concat(" ")
                .concat(String.format("HTTP/%s", this.httpVersion))
                .concat("\r\n");
    }
}
