package AnimeTavernWebsite;

import AnimeTavernWebsite.Controllers.HomeController;
import WebServer.MVC.Annotations.Route;
import WebServer.MVC.BaseController;
import WebServer.MVC.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main {

    public static ArrayList<BaseController> registeredControllers = new ArrayList<>();

    public static void main(String[] args) {

        // Register MVC controllers here
        registeredControllers.add(new HomeController());

        String testURI = "/";


        // Ignore below
        controllerLoop:
        for(BaseController c : Main.registeredControllers){
            Method[] instanceMethods = c.getClass().getMethods();
            methodLoop:
            for (Method m : instanceMethods) {
                Route[] methodRoutes = m.getDeclaredAnnotationsByType(Route.class);
                if (methodRoutes.length > 0) {
                    for (Route r : methodRoutes) {
                        if (r.uri().equalsIgnoreCase(testURI)){
                            try {
                                HttpResponse response = (HttpResponse) m.invoke(c);
                                System.out.println(response.body);
                                break controllerLoop;
                            }catch(IllegalAccessException e){

                            }catch(InvocationTargetException e){

                            }
                        }
                    }
                }
            }
        }
    }
}
