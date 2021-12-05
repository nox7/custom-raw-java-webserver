package WebServer.MVC.ViewRenderer;

import WebServer.MVC.ViewParser.View;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class LayoutParser {
    public static String cwd = Path.of("").toAbsolutePath().toString();

    private String getLayoutFileContents(String layoutFileName){
        Path layoutFilePath = Path.of(cwd + "/public/layouts/" + layoutFileName);
        try {
            String fileContents = new String(Files.readAllBytes(layoutFilePath));
            return fileContents;
        }catch(IOException e){
            System.out.println("FAILED TO READ FILE: " + e.getMessage());
        }

        return null;
    }

    public Layout getLayout(String layoutFileName) {
        String layoutFileContents = this.getLayoutFileContents(layoutFileName);
        char[] charArray = layoutFileContents.toCharArray();

        Layout layout = new Layout();
        layout.content = layoutFileContents.toString();
        return layout;
    }
}
