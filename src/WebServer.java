import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static int httpPortNumber = 80;

    public static void main(String[] args){
        System.out.println("Starting Java v17 Nox HTTP server.");
        try {

            // Setup the listener socket on the defined port
            ServerSocket httpServer = new ServerSocket(WebServer.httpPortNumber);

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

                // Setup a PrintWriter to write text to the output stream later
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
                // Respond to the client
                String responseBody = "<p>Hello world</p>";

                // Prepare the headers and then concat the HTML response body
                String toSendToClient = "HTTP/1.1 200 OK\r\n"
                        .concat("Content-Type: text/html\r\n")
                        .concat("Connection: close\r\n")
                        .concat("Content-Length: " + responseBody.getBytes().length + "\r\n")
                        .concat("\r\n")
                        .concat(responseBody + "\r\n\r\n");
                System.out.print(toSendToClient);
                outputStreamWriter.print(toSendToClient);
                System.out.println("Sending output stream to client connection.");
                outputStreamWriter.flush();
                clientSocket.close(); // Close the client socket
            }
            
            // System.out.println("Server closing.");
        }catch(IOException ioException){
            System.out.println(ioException.getMessage());
        }
    }
}
