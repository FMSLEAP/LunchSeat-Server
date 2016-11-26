package net.lisd.lunchseat_server;

import java.util.ArrayList;
import java.util.List;

public class WebsocketSessionManager {

    private static WebsocketSessionManager sessionManager;

    private final List<WebsocketSession> sessions = new ArrayList<>();

    public static WebsocketSessionManager getSessionManager() {
        if (sessionManager == null) {
            sessionManager = new WebsocketSessionManager();
        }
        return sessionManager;
    }

    public void openSession(String host) {
        sessions.add(new WebsocketSession(host));
    }

    public void endSession(String host) {
        sessions.remove(getSessionByHost(host));
    }

    private WebsocketSession getSessionByHost(String host) {
        for (WebsocketSession s : sessions) {
            if (s.getHost().equals(host)) {
                return s;
            }
        }
        return null;
    }

}
