package WebServer.MVC;

import AnimeTavernWebsite.Main;
import WebServer.MVC.Annotations.Route;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Router {

    public static ArrayList<BaseController> registeredRoutes;

    public Router(){}

    public RouteResponse routeRequest(String method, String uri){
        RouteResponse routeResponse = new RouteResponse();

        controllerLoop:
        for (BaseController c : Main.registeredControllers) {
            Method[] instanceMethods = c.getClass().getMethods();
            for (Method m : instanceMethods) {
                Route[] methodRoutes = m.getDeclaredAnnotationsByType(Route.class);
                if (methodRoutes.length > 0) {
                    for (Route r : methodRoutes) {
                        System.out.println("RECEIVED URI and METHOD " + uri + " " + method + " against " + r.uri() + " and METHOD " + r.method());
                        if (r.uri().equalsIgnoreCase(uri) && r.method().equalsIgnoreCase(method)) {
                            try {
                                HttpResponse response = (HttpResponse) m.invoke(c);
                                routeResponse.didMatchRoute = true;
                                routeResponse.httpResponse = response;
                                break controllerLoop;
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return routeResponse;
    }
}
