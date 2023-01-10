package fr.lernejo.navy_battle;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class PlayerBoard {
    private final int[] port_ennemi = {0};

    private final int[] inc = {0};
    private final int[][] board;
    private final Server s;
    private final String[] BoardOptions = {"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "G0", "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "H0", "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "I0", "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "J0", "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9"};
    private final int myport;
    public PlayerBoard(int _myport, Server _s){
        s = _s;
        myport = _myport;
        board = new int[][]{{0,0,0,0,0,0,0,0,0,0},
            {0,1,0,0,0,0,0,0,0,0},
            {0,1,0,0,0,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,1,0,0,0,0},
            {0,1,0,0,1,1,0,0,0,0},
            {1,1,1,0,0,0,0,0,0,0},
            {0,1,0,0,0,0,0,0,0,0}};

    }

    public String Indexx(){
        String res = this.BoardOptions[this.inc[0]];
        this.inc[0] += 1;
        return res;
    }
    public int GetEnnemyPort(){
        return port_ennemi[0];
    }
    public void SetEnnemyPort(int port){
        this.port_ennemi[0] = port;
    }
    public int GetBoardVal(int col, int line){
        return board[line][col];
    }
    public boolean ShipLeft(){
        boolean res = false;
        for (int[] a:board) {
            for (int b:a){
                if(b == 1) {
                    res = true;
                    break;
                }
            }
            if(res){
                break;
            }
        }
        return res;
    }
    public void SetBoardTo0(int col, int line){
        board[line][col] = 0;
    }

    public int getMyport() {
        return myport;
    }

    public void Play(String cell) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://127.0.0.1:" + this.port_ennemi[0] + "/api/game/fire?cell=" + cell)).setHeader("Accept", "application/json").GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            JSONObject json = new JSONObject(response.body());
            if (Objects.equals(json.get("shipLeft").toString(), "false")){
                System.out.println("Bravo Vous Avez Gagné");
                s.CloseServer();
                System.exit(0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
