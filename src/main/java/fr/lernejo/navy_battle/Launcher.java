package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0 || args.length >= 3)
            throw new IllegalArgumentException("Need 1 port as a first argument");
        int port = Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(1));
        server.createContext("/ping", new PingHandler());
        PlayerBoard b = new PlayerBoard(port);
        server.createContext("/api/game/start", new GameStartHttpHandler(port, b));
        server.createContext("/api/game/fire", new GameFire(b));
        server.start();
        if (args.length == 2) {
            b.SetEnnemyPort(Integer.parseInt(args[1].split(":")[2]));
            new PostRq(args, port);
        }
    }
}
