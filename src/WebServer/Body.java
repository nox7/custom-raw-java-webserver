package WebServer;

public class Body {

    public String payload;

    public Body(String payload){
        this.payload = payload;
    }

    public int getContentLength(){
        return this.payload.getBytes().length;
    }

    @Override
    public String toString(){
        return this.payload;
    }
}
