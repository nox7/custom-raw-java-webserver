package WebServer.MVC.ViewParser;

import WebServer.MVC.ViewLanguage.Parser;
import WebServer.MVC.ViewLanguage.Token;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewParser {
    public static String cwd = Path.of("").toAbsolutePath().toString();

    private String getViewFileContents(String viewFileName){
        Path viewFilePath = Path.of(cwd + "/public/views/" + viewFileName);
        try {
            String fileContents = new String(Files.readAllBytes(viewFilePath));
            return fileContents;
        }catch(IOException e){
            System.out.println("FAILED TO READ FILE: " + e.getMessage());
        }

        return null;
    }

    public View getView(String viewFileName){
        String viewFileContents = this.getViewFileContents(viewFileName);
        char[] charArray = viewFileContents.toCharArray();

        String parseState = "";
        String prevParserState = "";
        String prevDirectiveName = "";
        char tokenDelimiter = Character.MIN_VALUE;
        int tokenValueDelimiterDepth = 0;
        HashMap<String, String> directives = new HashMap<>();
        StringBuilder buffer = new StringBuilder();

        System.out.println("Beginning view parsing...");

        for (char c: charArray){
            switch (parseState){
                case "":
                    if (c == '@'){
                        parseState = "PARSE_DIRECTIVE_NAME";
                        buffer.append(c);
                    }else if (c == '=' && prevParserState.equals("PARSE_DIRECTIVE_NAME")){
                        prevParserState = "";
                        parseState = "PARSE_DIRECTIVE_SHORT_VALUE";
                    }else if (c == '{' && prevParserState.equals("PARSE_DIRECTIVE_NAME")){
                        prevParserState = "";
                        parseState = "PARSE_DIRECTIVE_LONG_VALUE";
                    }
                    break;
                case "PARSE_DIRECTIVE_LONG_VALUE":
                    if (c == tokenDelimiter){
                        if (tokenValueDelimiterDepth > 0){
                            --tokenValueDelimiterDepth;
                            buffer.append(c);
                        }else{
                            prevParserState = "PARSE_DIRECTIVE_LONG_VALUE";
                            parseState = "";
                            tokenDelimiter = Character.MIN_VALUE;
                            directives.put(prevDirectiveName, buffer.toString());
                            buffer.setLength(0);
                        }
                    }else if (c == '{'){
                        ++tokenValueDelimiterDepth;
                        buffer.append(c);
                    }else{
                        buffer.append(c);
                    }
                    break;
                case "PARSE_DIRECTIVE_SHORT_VALUE":
                    if (c == '\"'){
                        prevParserState = "PARSE_DIRECTIVE_SHORT_VALUE";
                        parseState = "PARSE_DIRECTIVE_SHORT_VALUE_TOKEN";
                        tokenDelimiter = c;
                        buffer.append(c);
                    }
                    break;
                case "PARSE_DIRECTIVE_SHORT_VALUE_TOKEN":
                    if (c == tokenDelimiter){
                        prevParserState = "PARSE_DIRECTIVE_SHORT_VALUE_TOKEN";
                        parseState = "";
                        buffer.append(c);

                        // Clear the delimiter from the start and end of the buffer
                        int currentIndex = 0;
                        while (buffer.charAt(currentIndex) == tokenDelimiter){
                            buffer = buffer.deleteCharAt(currentIndex);
                            ++currentIndex;
                        }

                        currentIndex = buffer.length() - 1;
                        while (buffer.charAt(currentIndex) == tokenDelimiter){
                            buffer = buffer.deleteCharAt(currentIndex);
                            --currentIndex;
                        }

                        directives.put(prevDirectiveName, buffer.toString());
                        buffer.setLength(0);
                        tokenDelimiter = Character.MIN_VALUE;
                    }else if (c == '\n'){
                        System.out.println("PARSE ERROR");
                        break;
                        //throw new ParseError("Unexpected EOL when parsing directive string value.");
                    }else{
                        buffer.append(c);
                    }
                    break;
                case "PARSE_DIRECTIVE_NAME":
                    if (c == ' '){
                        prevParserState = "PARSE_DIRECTIVE_NAME";
                        parseState = "";
                        directives.put(buffer.toString(), "");
                        prevDirectiveName = buffer.toString();
                        buffer.setLength(0);
                    }else if (c == '{'){
                        prevParserState = "PARSE_DIRECTIVE_NAME";
                        parseState = "PARSE_DIRECTIVE_LONG_VALUE";
                        directives.put(buffer.toString(), "");
                        prevDirectiveName = buffer.toString();
                        tokenDelimiter = '}'; // What to expect
                        buffer.setLength(0);
                    }else if (c == '='){
                        prevParserState = "PARSE_DIRECTIVE_NAME";
                        parseState = "PARSE_DIRECTIVE_SHORT_VALUE";
                        directives.put(buffer.toString(), "");
                        prevDirectiveName = buffer.toString();
                        buffer.setLength(0);
                    }else{
                        buffer.append(c);
                    }
                    break;
                default:
                    break;
            }
        }

        System.out.println(directives.keySet().toString());
        View view = new View();
        view.layout = directives.get("@Layout");
        view.head = directives.get("@Head");
        view.body = directives.get("@Body");

        // Parse the body
        System.out.println("Here is what the tokenizer got ======= BELOW");
        Parser parser = new Parser();
        ArrayList<Token> tokens = parser.tokenize(view.body);
        for (Token t : tokens){
            System.out.println("Token rawText: " + t.rawText);
        }
        return view;

    }
}
