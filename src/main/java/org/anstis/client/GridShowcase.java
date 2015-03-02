package org.anstis.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import org.anstis.client.grid.widget.GridShowcaseWidget;

public class GridShowcase implements EntryPoint {

    public void onModuleLoad() {
        final GridShowcaseWidget container = new GridShowcaseWidget();
        RootPanel.get().add( container );
    }

}
