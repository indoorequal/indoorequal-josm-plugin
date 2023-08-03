// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.indoorequal;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.io.importexport.OsmExporter;
import org.openstreetmap.josm.tools.OpenBrowser;

import org.openstreetmap.josm.tools.Logging;

public class Action extends JosmAction {

	public Action() {
		super(tr("View in Indoor=..."), null, tr("View current layer in Indoor="), null, false);
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
				OsmExporter osmExporter = new OsmExporter();
				File osmFile = new File(System.getProperty("java.io.tmpdir") + "/indoorequal.osm");
				osmExporter.exportData(osmFile, (OsmDataLayer) layer);

				String result = OpenBrowser
						.displayUrl("https://indoorequal.org/#url=http://localhost:8432/indoorequal.osm");
				if (result != null) {
					Logging.warn(result);
				}
			} catch (IOException e) {
				Logging.error(e);
			}
		}
	}
}
