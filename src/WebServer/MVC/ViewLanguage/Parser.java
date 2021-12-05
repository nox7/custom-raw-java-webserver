package WebServer.MVC.ViewLanguage;


import java.util.ArrayList;

public class Parser {
    public Parser(){

    }

    /**
     * Takes a set of text and creates Tokens for it to eventually be outputted
     */
    public ArrayList<Token> tokenize(String content){
        ArrayList<Token> tokens = new ArrayList<Token>();
        char[] characters = content.toCharArray();
        StringBuilder buffer = new StringBuilder();
        String parsingState = "";

        Token currentToken = new Token();
        for (char c : characters){
            if (parsingState.equals("")) {
                switch(c){
                    case '{':
                        if (buffer.length() > 0){
                            // Emmit a token
                            currentToken.rawText = buffer.toString();
                            currentToken.type = TokenType.TEXT;
                            tokens.add(currentToken);
                            currentToken = new Token();
                            buffer.setLength(0);
                        }
                        parsingState = "OPEN_CONTROL_STRUCTURE_STAGE_1";
                        buffer.append(c);
                        break;
                    default:
                        buffer.append(c);
                        break;
                }
            }else if (parsingState.equals("OPEN_CONTROL_STRUCTURE_STAGE_1")){
                switch(c){
                    case '{':
                        parsingState = "CONSUMING_CONTROL_STRUCTURE_CONTENTS";
                        buffer.append(c);
                        break;
                    default:
                        // Otherwise, reset the parsing state. Not a control structure.
                        parsingState = "";
                        buffer.append(c);
                        break;
                }
            }else if (parsingState.equals("CONSUMING_CONTROL_STRUCTURE_CONTENTS")){
                switch(c){
                    case '}':
                        parsingState = "CLOSING_CONTROL_STRUCTURE_STAGE_1";
                        buffer.append(c);
                        break;
                    default:
                        buffer.append(c);
                        break;
                }
            }else if (parsingState.equals("CLOSING_CONTROL_STRUCTURE_STAGE_1")){
                switch(c){
                    case '}':
                        // This control structure is finished. Emmit the token and reset the parsing state
                        parsingState = "";
                        buffer.append(c);
                        currentToken.rawText = buffer.toString();
                        currentToken.type = TokenType.CONTROL_STRUCTURE;
                        // Add token to list
                        tokens.add(currentToken);
                        // Reset the current token
                        currentToken = new Token();

                        // Clear the buffer
                        buffer.setLength(0);
                        break;
                    default:
                        // Not a }, so go back to consuming the control structure's contents
                        parsingState = "CONSUMING_CONTROL_STRUCTURE_CONTENTS";
                        buffer.append(c);
                        break;
                }
            }
        }

        // End of data. If buffer is not empty, then emit it as a text token
        if (buffer.length() > 0){
            currentToken.rawText = buffer.toString();
            currentToken.type = TokenType.TEXT;
            tokens.add(currentToken);
            buffer.setLength(0);
        }

        return tokens;
    }
}
