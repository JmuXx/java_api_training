package fr.lernejo.navy_battle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PlayerBoard {
    private final int[] port_ennemi = {0};
    private final int[][] board;
    private final int myport;
    public PlayerBoard(int _myport){
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

    public void Play(String cell) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:" + this.port_ennemi[0] + "/api/game/fire?cell=" + cell))
            .setHeader("Accept", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = null;

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
