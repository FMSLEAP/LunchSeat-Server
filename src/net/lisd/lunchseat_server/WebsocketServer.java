package net.lisd.lunchseat_server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.logging.Level;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebsocketServer extends WebSocketServer {

    private static WebsocketServer server;

    private WebsocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        WebsocketSessionManager.getSessionManager().openSession(conn.getRemoteSocketAddress().getAddress().getHostAddress());
        if (Main.DEBUG) {
            Bukkit.getLogger().log(Level.INFO, "{0} has connected to the WS server.", conn.getRemoteSocketAddress().getAddress().getHostName());
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        WebsocketSessionManager.getSessionManager().endSession(conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @SuppressWarnings("CanBeFinal")
    class IntialParse {
        @SerializedName("action")
        private Integer action;
    }

    class ParseWithUsername {
        @SerializedName("action")
        private Integer action;
        @SerializedName("username")
        private String username;
    }


    class ParseWithCode {
        @SerializedName("action")
        private Integer action;
        @SerializedName("username")
        private String username;
        @SerializedName("code")
        private Integer code;
    }

    @Override
    public void onMessage(WebSocket conn, String json) {
        if (Main.DEBUG) {
            Bukkit.getLogger().log(Level.INFO, "Json received: {0}", json);
        }
        Gson gson = new Gson();
        Integer action;
        try {
            IntialParse jsonRequest = gson.fromJson(json, IntialParse.class);
            action = jsonRequest.action;
        } catch (JsonSyntaxException e) {
            conn.send("{\"result\": " + ErrorsAndTheirMeanings.LOLWAT.getId() + "}");
            conn.close();
            throw new JsonSyntaxException("Invalid JSON received from " + conn.getRemoteSocketAddress() + "!");
        }
        Integer result;
        switch (action) {
            case 1:try {
                WebsocketServer.stopServer();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
                ParseWithUsername datBoi = gson.fromJson(json, ParseWithUsername.class);
                result = new Utils(datBoi.username).checkHasJoined();
                break;
            case 2:
                ParseWithUsername waddUp = gson.fromJson(json, ParseWithUsername.class);
                result = new Utils(waddUp.username).authenticateSend();
                break;
            case 3:
                ParseWithCode jsonRequest = gson.fromJson(json, ParseWithCode.class);
                result = new Utils(jsonRequest.username).authenticateCheck(jsonRequest.code);
                break;
            default:
                conn.send("{\"result\": " + ErrorsAndTheirMeanings.LOLWAT.getId() + "}");
                conn.close();
                throw new JsonSyntaxException("Invalid action received from " + conn.getRemoteSocketAddress() + "!");
        }
        conn.send("{\"result\": " + result + "}");
        conn.close();
    }

    static void runServer() {
        WebSocketImpl.DEBUG = false;
        int port = 8887;
        server = new WebsocketServer(port);
        server.start();
    }

    static void stopServer() throws InterruptedException, IOException {
        server.stop();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAll(String data) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(data);
            }
        }
    }

    public void sendData(WebsocketSession session, String data) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            con.stream().filter(c -> c.getRemoteSocketAddress().getAddress().getHostAddress().equalsIgnoreCase(session.getHost())).forEachOrdered(c -> {
                Bukkit.getLogger().log(Level.INFO, "Send data packet: {0}", data);
                c.send(data);
            });
        }
    }
}
