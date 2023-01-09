package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Objects;
import org.json.JSONObject;

public class GameFire implements HttpHandler {

    private final PlayerBoard b;
    public GameFire(PlayerBoard _b){
        b = _b;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET"))
            SendResponse(exchange,404,"Not Found");
        URI rqurl = exchange.getRequestURI();
        String[] c = rqurl.getQuery().split("=");
        if(!Objects.equals(c[0], "cell")){
            SendResponse(exchange,400,"Bad Request");
        }
        String cell = c[1];
        String response = ConstructResponse(cell);
        SendResponse(exchange,202,response);
    }
    public String ConstructResponse(String cell){
        String state = NavyFormatToCoords(cell);
        boolean shipleft = b.ShipLeft();
        JSONObject json = new JSONObject();
        json.put("consequence",state);
        json.put("shipLeft",shipleft);
        return json.toString();
    }
    public String NavyFormatToCoords(String cell){
        int col = cell.charAt(0) - 65;
        String s = String.valueOf(cell.charAt(0));
        cell = cell.replace(s, "");
        int line = Integer.parseInt(cell) - 1;
        return GetResult(col,line);
    }

    public String GetResult(int col,int line){
        if(b.GetBoardVal(col,line) == 0){
            return "miss";
        }
        else{
            b.SetBoardTo0(col,line);
            int res = IsSunked(col,line);
            if(res == 1)
                return "sunk";
            return "hit";
        }
    }
    public int IsSunked(int col, int line){
        // Bad way to check if a boat is sunk for a real game but with my boat start disposition it work ... :D
        int res = b.GetBoardVal(col - 1,line - 1) & b.GetBoardVal(col + 1,line + 1);
        res = res & b.GetBoardVal(col - 1,line + 1) & b.GetBoardVal(col + 1,line - 1);
        res = res & b.GetBoardVal(col - 1,line) & b.GetBoardVal(col + 1,line);
        res = res & b.GetBoardVal(col,line - 1) & b.GetBoardVal(col,line + 1);
        return res;
    }
    public void SendResponse(HttpExchange exchange,int code, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
        /*try {
            b.Play("C4");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }
}

