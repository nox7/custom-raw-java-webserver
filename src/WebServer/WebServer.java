package WebServer;

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

                // Read individual lines from the client socket
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
                    System.out.println(inputLine);
                    // If there is a blank line received (for now, this is only valid for GET requests), then break the listener loop
                    if (inputLine.equals("")) {
                        System.out.println("Finished reading HTTP request from client.");
                        break;
                    }
                }

                System.out.println("Finished reading line.");
                System.out.println("Responding to client with HTTP 200 OK.");
                String bodyPayload = "<!doctype><html><head></head><body><p>Hello world!</p></body></html>";

                HttpStatusLine statusLine = new HttpStatusLine("1.1", 200, "OK");
                Headers headers = new Headers();
                Body body = new Body(bodyPayload);
                headers.addHeader(new Header("Content-Type", "text/html"));
                headers.addHeader(new Header("Connection", "close"));
                headers.addHeader(new Header("Connection", "close"));
                headers.addHeader(new Header("Content-Length", String.valueOf(body.getContentLength())));

                // Prepare the headers and then concat the HTML response body
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
