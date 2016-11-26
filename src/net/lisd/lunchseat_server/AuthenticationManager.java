/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lisd.lunchseat_server;

import org.bukkit.entity.Player;

/**
 *
 * @author joshua
 */
class AuthenticationManager {

    private final Player p;
    private final Main plugin;

    AuthenticationManager(Player p) {
        this.plugin = Main.plugin;
        this.p = p;
    }

    /**
     * Send authentication code to player
     */
    void sendCode() {
        p.sendMessage("Your authentication code is: 477654");
    }

    Integer checkCode(Integer code) {
        if (code.equals(477654)) {
            p.sendMessage("Verified!");
            return ErrorsAndTheirMeanings.SUCCESS.getId();
        } else {
            p.sendMessage("Denied!");
            return ErrorsAndTheirMeanings.CODE_NOT_ACCEPTABLE.getId();
        }
    }

}
