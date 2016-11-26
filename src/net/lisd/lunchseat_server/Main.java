package net.lisd.lunchseat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi;

/**
 * A registration plugin, server side.
 *
 * @author Joshua Jones
 */
public class Main {

    /**
     * Global debug mode
     */
    static Boolean DEBUG;

    /**
     * Global version number
     */
    static String VERSION;

    /**
     * Logger
     */
    Logger logger = Logger.getLogger(getClass().getName());

    /**
     * ANSI color
     */
    final static String BLUE = Ansi.ansi().fg(Ansi.Color.BLUE).boldOff().toString();
    final static String RESET = Ansi.ansi().reset().toString();

    /**
     * Fired when plugin is first run
     *
     * @param args Arguments passed to JAR. We don't need them,
     *             so they are conveniently ignored. Sorry :/
     */
    public void main(String[] args) {
        // Try to run websocket server
        WebsocketServer.runServer();

        // Find if we are in debug mode or not
        Properties information = new Properties();
        try {
            information.load(getClass().getResourceAsStream("/info.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DEBUG = Boolean.parseBoolean(information.getProperty("debug"));
        // If we're in debug mode, we should show the commit.
        if (DEBUG) {
            VERSION = information.getProperty("version") + "-" + information.getProperty("commit");
        } else {
            VERSION = information.getProperty("version");
        }

        logger.info(BLUE + "Websocket server started (version " + VERSION + ", with debug " + (DEBUG ? "on" : "off") + "." + RESET);

        //
        // -------------------------
        //
        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.log(Level.SEVERE, "Stopping server...");
            try {
                WebsocketServer.stopServer();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
