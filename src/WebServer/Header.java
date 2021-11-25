package WebServer;

public class Header {

    public String name;
    public String value;

    /**
     * Creates a Header instance from a raw header line. By default, it parses the name until the first colon,
     * then it skips the next whitespace encountered, if any, then parses any other characters as the value.
     */
    public static Header fromRaw(String rawHeaderLine){
        char[] charArray = rawHeaderLine.toCharArray();
        String parsingMode = "NAME";
        StringBuilder headerName = new StringBuilder();
        StringBuilder headerValue = new StringBuilder();
        for (char c: charArray){
            if (parsingMode.equals("NAME")) {
                if (c == ':') {
                    // Ignore and swap to value parsing
                    parsingMode = "VALUE";
                }else{
                    headerName.append(c);
                }
            }else{
                headerValue.append(c);
            }
        }

        // Left trim the header value of whitespace
        String headerValueString = headerValue.toString().substring(1);
        return new Header(headerName.toString(), headerValueString);
    }

    public Header(String name, String value){
        this.name = name;
        this.value = value;
    }
}
