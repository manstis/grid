package org.anstis.client.grid.widget;

import org.anstis.client.grid.model.Grid;

public interface ISelectionManager {

    void select( final Grid selectable );

    void scrollIntoView( final Grid selectable );

}
