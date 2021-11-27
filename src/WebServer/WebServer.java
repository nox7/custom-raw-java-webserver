package WebServer;

import WebServer.Request.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public int httpPortNumber = 80;
    public int tlsPortNumber = 443;

    public WebServer(int httpPortNumber, int tlsPortNumber){
        this.httpPortNumber = httpPortNumber;
        this.tlsPortNumber = tlsPortNumber;
    }

    public void startHTTPServer(){
        try {

            // Setup the listener socket on the defined port
            ServerSocket httpServer = new ServerSocket(this.httpPortNumber);

            // Infinite loop
            while (true) {
                System.out.println("Waiting for a connection.");
                // .accept() holds the thread until a client connection wants to connect
                Socket clientSocket = httpServer.accept();
                System.out.println("Received a connection.");

                // Get the input stream from the client socket
                // Read to a buffer from an InputStreamReader
                BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
                );

                // Get an output stream for the client connection
                OutputStream clientSocketOutputStream = clientSocket.getOutputStream();

                // Set up a PrintWriter to write text to the output stream later
                PrintWriter outputStreamWriter = new PrintWriter(clientSocketOutputStream);

                // Read the first line. It must be a valid HTTP request line
                String httpRequestLine = inputReader.readLine();

                if (httpRequestLine == null){
                    // Failed to provide valid HTTP request on first line input
                    // TODO
                    // This is temporary
                    // Respond with a 400 Bad Request
                    outputStreamWriter.print("HTTP/1.1 400 Bad Request\r\n\r\n");
                    outputStreamWriter.flush();
                    clientSocket.close();
                    continue;
                }

                HttpRequestLine requestLine = HttpRequestLine.fromRaw(httpRequestLine);

                // Read individual lines as HTTP headers from the client socket
                Headers requestHeaders = new Headers();
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
                    if (inputLine.equals("")) {
                        System.out.println("Finished reading HTTP request headers from client. Switching to reading HTTP body");
                        break;
                    } else {
                        System.out.println(inputLine);
                        requestHeaders.addHeader(Header.fromRaw(inputLine));
                    }
                }

                Request request = new Request(requestLine);
                request.headers = requestHeaders;

                // If this is not an OPTIONS or GET request then check for a Content-Length header
                // For the Nox server, Content-Length is required
                int requestBodyContentLength = 0;
                if (!request.method.equalsIgnoreCase("get") && !request.method.equalsIgnoreCase("options") && !request.method.equalsIgnoreCase("head")){
                    Header contentLengthHeader = request.getHeader("content-length");
                    if (contentLengthHeader == null){
                        System.out.println("Missing content length");
                        // TODO
                        // This is temporary
                        // Respond with a 400 Bad Request
                        outputStreamWriter.print("HTTP/1.1 411 Length Required\r\n\r\n");
                        outputStreamWriter.flush();
                        clientSocket.close();
                        continue;
                    }else{
                        // TODO catch exception here
                        requestBodyContentLength = Integer.parseInt(contentLengthHeader.value);
                        System.out.println("Got content length " + requestBodyContentLength);
                    }
                }

                if (requestBodyContentLength > 0){
                    // Read request body
                    StringBuilder requestBodyBuilder = new StringBuilder();
                    char inputCharacter;
                    int lengthRead = 0;
                    System.out.println("Reading request body");
                    do{
                        int inputCharacterCode = inputReader.read();
                        inputCharacter = (char) inputCharacterCode;
                        ++lengthRead;

                        System.out.println("Char code: " + inputCharacterCode + " | Length read: " + lengthRead + " | Expected length: " + requestBodyContentLength);

                        if (inputCharacter != 0xFF){
                            requestBodyBuilder.append(inputCharacter);
                        }
                    }while(inputCharacter != 0xFF && lengthRead < requestBodyContentLength);

                    // Set the Request body property
                    Body requestBody = new Body(requestBodyBuilder.toString());
                    request.body = requestBody;
                    System.out.println(requestBodyBuilder.toString());
                }

                // Response
                String bodyPayload = "<!doctype><html><head></head><body><p style=\"background-color:blue; padding:5rem;\">Hello world!</p></body></html>";
                HttpStatusLine statusLine = new HttpStatusLine("1.1", 200, "OK");
                Headers headers = new Headers();
                Body body = new Body(bodyPayload);
                headers.addHeader(new Header("Content-Type", "text/html"));
                headers.addHeader(new Header("Connection", "close"));
                headers.addHeader(new Header("Content-Length", String.valueOf(body.getContentLength())));

                // Prepare the headers and then concat the HTML response body
                System.out.println(statusLine.toString());
                String toSendToClient = statusLine.toString()
                    .concat(headers.toString())
                    .concat("\r\n")
                    .concat(body.toString());
                outputStreamWriter.print(toSendToClient);
                System.out.println("Sending output stream to client connection.");
                outputStreamWriter.flush();
                clientSocket.close(); // Close the client socket
            }
        }catch(IOException ioException){
            System.out.println(ioException.getMessage());
        }
    }
}
