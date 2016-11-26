/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lisd.lunchseat_server;

/**
 *
 * @author joshua
 */
class WebsocketSession {

    private final String host;

    public WebsocketSession(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
