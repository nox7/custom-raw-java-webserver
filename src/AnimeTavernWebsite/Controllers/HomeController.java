package AnimeTavernWebsite.Controllers;

import WebServer.MVC.Annotations.Route;
import WebServer.MVC.BaseController;
import WebServer.MVC.HttpResponse;
import WebServer.MVC.ViewParser.View;
import WebServer.MVC.ViewParser.ViewParser;

public class HomeController extends BaseController {

    @Route(method = "get", uri = "/")
    @Route(method = "get", uri = "/home")
    public HttpResponse homeView(){
        System.out.println("INVOKED controller method");

        ViewParser vp = new ViewParser();
        View view = vp.getView("home.html");

        System.out.println("EEEEE: " + view.body.toString());
        System.out.println("== View Body ==: ".concat(view.body));

        HttpResponse response = new HttpResponse();
        response.statusCode = 200;
        response.body = "<p>Hello</p>";

        System.out.println("Responsive parsed from controller");

        return response;
    }

}
