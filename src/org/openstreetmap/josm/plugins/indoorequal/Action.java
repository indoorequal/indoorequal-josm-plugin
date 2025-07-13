// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.indoorequal;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.tools.OpenBrowser;

public class Action extends JosmAction {

	public Action() {
		super(tr("View in Indoor=..."), "indoorequal", tr("View current layer in Indoor="), null, false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String result = OpenBrowser
				.displayUrl("https://indoorequal.org/#url=http://localhost:8432/indoorequal.osm");
		if (result != null) {
			Logging.warn(result);
		}
	}
}
