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
                            System.out.println("yes");
                            try {
                                System.out.println("WILL INVOKE");
                                HttpResponse response = (HttpResponse) m.invoke(c);
                                System.out.println("INVOKED");
                                routeResponse.didMatchRoute = true;
                                routeResponse.httpResponse = response;
                                System.out.println("DONE");
                                break controllerLoop;
                            } catch (IllegalAccessException e) {
                                System.out.println("There was an exception in the router." + e.getClass().toString() + " message: " + e.getMessage());
                            }catch(InvocationTargetException e){
                                System.out.println("Invocation exception propagated ." + e.getCause().getMessage());
                            }
                        }
                    }
                }
            }
        }

        return routeResponse;
    }
}
