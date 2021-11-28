package AnimeTavernWebsite;

import AnimeTavernWebsite.Controllers.HomeController;
import WebServer.MVC.Annotations.Route;
import WebServer.MVC.BaseController;
import WebServer.MVC.HttpResponse;
import WebServer.WebServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main {

    public static ArrayList<BaseController> registeredControllers = new ArrayList<>();

    public static void main(String[] args) {

        // Register MVC controllers here
        registeredControllers.add(new HomeController());

        String testURI = "/";

        WebServer httpWebServer = new WebServer(
                80,
                443,
                2,
                registeredControllers
        );
        httpWebServer.startHTTPServer();
    }
}