package AnimeTavernWebsite.Controllers;

import WebServer.MVC.Annotations.Route;
import WebServer.MVC.BaseController;
import WebServer.MVC.HttpResponse;

public class HomeController extends BaseController {

    @Route(method = "get", uri = "/")
    @Route(method = "get", uri = "/home")
    public HttpResponse homeView(){

        HttpResponse response = new HttpResponse();
        response.statusCode = 200;
        response.body = "<p>Hello</p>";

        return response;
    }

}
