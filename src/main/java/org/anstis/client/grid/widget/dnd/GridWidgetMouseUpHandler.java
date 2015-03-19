package org.anstis.client.grid.widget.dnd;

import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.google.gwt.dom.client.Style;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.GridWidget;

public class GridWidgetMouseUpHandler implements NodeMouseUpHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState state;
    private final Map<Grid, GridWidget> selectables;

    public GridWidgetMouseUpHandler( final GridLayer layer,
                                     final GridWidgetHandlersState state,
                                     final Map<Grid, GridWidget> selectables ) {
        this.layer = layer;
        this.state = state;
        this.selectables = selectables;
    }

    @Override
    public void onNodeMouseUp( final NodeMouseUpEvent event ) {
        state.setGrid( null );
        state.setGridColumn( null );
        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
        layer.getViewport().getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
        layer.remove( state.getEventColumnHighlight() );
        layer.draw();
    }

}
