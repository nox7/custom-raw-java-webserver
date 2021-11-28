package WebServer;

import WebServer.Request.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    public int httpPortNumber = 80;
    public int tlsPortNumber = 443;
    public int maxThreadsInPool = 1;

    public WebServer(int httpPortNumber, int tlsPortNumber, int maxThreadsInPool){
        this.httpPortNumber = httpPortNumber;
        this.tlsPortNumber = tlsPortNumber;
        this.maxThreadsInPool = maxThreadsInPool;
    }

    public void startHTTPServer(){
        try {

            // Setup the listener socket on the defined port
            ServerSocket httpServer = new ServerSocket(this.httpPortNumber);
            ExecutorService pool = Executors.newFixedThreadPool(this.maxThreadsInPool);

            // Infinite loop
            while (true) {
                System.out.println("Waiting for a connection.");
                // .accept() holds the thread until a client connection wants to connect
                Socket clientSocket = httpServer.accept();
                String ipAddress = clientSocket.getRemoteSocketAddress().toString();

                System.out.println("Received a connection. Spawning new task to thread pool to handle.");
                RequestProcessor requestProcessor = new RequestProcessor(ipAddress, clientSocket);
                pool.execute(requestProcessor);
            }
        }catch(IOException ioException){
            System.out.println(ioException.getMessage());
        }
    }
}
