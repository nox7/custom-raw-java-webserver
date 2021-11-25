import WebServer.WebServer;

public class Nox {

    public static void main(String[] args){
        System.out.println("Starting Java v17 Nox HTTP server.");
        WebServer webServer = new WebServer(80, 443);
        webServer.startHTTPServer();
    }
}
