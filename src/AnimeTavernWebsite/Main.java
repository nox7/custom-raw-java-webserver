package AnimeTavernWebsite;

import AnimeTavernWebsite.Controllers.HomeController;
import WebServer.MVC.Annotations.Route;
import WebServer.MVC.BaseController;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main {

    public static ArrayList<BaseController> registeredControllers = new ArrayList<>(
            new HomeController()
    );

    public static void main(String args[]){

        // Register MVC controllers here
        registeredControllers.add(new HomeController());


        // Ignore below
        for(BaseController c : Main.registeredControllers){
            Method[] instanceMethods = c.getClass().getMethods();
            for (Method m : instanceMethods) {
                Route[] methodRoutes = m.getDeclaredAnnotationsByType(Route.class);
                if (methodRoutes.length > 0) {
                    for (Route r : methodRoutes) {
                        System.out.println(r.uri());
                    }
                }
            }
        }
    }
}
