// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.indoorequal;

import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

import org.openstreetmap.josm.tools.Logging;

/**
 * Indoor= plugin.
 */
public final class IndoorEqualPlugin extends Plugin {

    private static IndoorEqualPlugin instance;
    private static int PORT = 8432;

    public IndoorEqualPlugin(PluginInformation info) {
        super(info);
        if (instance == null) {
            instance = this;
            try {	
                Server server = new Server(PORT);
                server.start();
            } catch (IOException e) {
                Logging.error("Error occured:" + e.getMessage());
            }
            refreshMenu();
        } else {
            throw new IllegalStateException("Cannot instantiate plugin twice !");
        }
    }
    
    public static IndoorEqualPlugin getInstance() {
        return instance;
    }

    public static void refreshMenu() {
        JMenu menu = MainApplication.getMenu().moreToolsMenu;
        if (menu.isVisible())
            menu.addSeparator();
        else {
            menu.setVisible(true);
        }
        menu.add(new JMenuItem(new Action()));
    }
}
