package org.anstis.client.grid.widget;

import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public interface ISelectionManager {

    void select( final Grid selectable );

    void scrollIntoView( final GridColumn link );

}
