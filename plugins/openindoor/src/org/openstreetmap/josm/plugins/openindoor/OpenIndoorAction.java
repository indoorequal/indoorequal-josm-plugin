// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.openindoor;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ServiceConfigurationError;
import java.io.IOException;
import java.awt.Desktop;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.projection.ProjectionRegistry;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.io.GeoJSONWriter;
import org.openstreetmap.josm.gui.io.importexport.GeoJSONExporter;
import org.openstreetmap.josm.gui.io.importexport.OsmExporter;

import org.openstreetmap.josm.tools.Logging;

public class OpenIndoorAction extends JosmAction {

    public OpenIndoorAction() {
        super(tr("View in OpenIndoor..."), null,
                tr("View current layer in OpenIndoor web app"), null, false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Layer layer = MainApplication.getLayerManager().getActiveLayer();
        if (layer == null)
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(MainApplication.getMainFrame()),
                    "No default layer found.");
        else if (!(layer instanceof OsmDataLayer))
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(MainApplication.getMainFrame()),
                    "The default layer is not an OSM layer.");
        else if (MainApplication.getMap() == null)
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(MainApplication.getMainFrame()),
                    "No map found.");
        else if (MainApplication.getMap().mapView == null)
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(MainApplication.getMainFrame()),
                    "No map view found.");
        else {
            try {
                GeoJSONExporter geojsonExporter = new GeoJSONExporter();
                File geojsonFile = new File(System.getProperty("java.io.tmpdir") + "/openindoor.geojson");
                geojsonExporter.exportData(geojsonFile, (OsmDataLayer) layer);

                OsmExporter osmExporter = new OsmExporter();
                File osmFile = new File(System.getProperty("java.io.tmpdir") + "/openindoor.osm");
                osmExporter.exportData(osmFile, (OsmDataLayer) layer);

                // Launch OpenIndoor web app in detached mode
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://app.openindoor.io/?source=josm"));
                    } catch (Exception e) {
                        Logging.error(e);
                    }
                }
            } catch(SocketTimeoutException e) {
                Logging.error(e);
            } catch (IOException e) {
                Logging.error(e);
            } catch (ServiceConfigurationError e) {
                Logging.error(e);
            }
        }
    }
}
