package org.anstis.client.grid.widget.dnd;

import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.dom.client.Style;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;
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
        switch ( state.getOperation() ) {
            case COLUMN_MOVE:
                handleColumnMove( event );
                break;
        }

        state.setGrid( null );
        state.setGridColumn( null );
        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
        layer.getViewport().getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
        layer.remove( state.getEventColumnHighlight() );
        layer.draw();
    }

    private void handleColumnMove( final NodeMouseUpEvent event ) {
        final GridWidget gridWidget = selectables.get( state.getGrid() );
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double ax = ap.getX();

        double offsetX = 0;
        final List<GridColumn> columns = state.getGrid().getColumns();
        for ( int index = 0; index < columns.size(); index++ ) {
            final GridColumn gc = columns.get( index );
            final double columnWidth = gc.getWidth();
            if ( ax > offsetX && ax < offsetX + columnWidth ) {
                if ( !state.getGridColumn().equals( gc ) ) {
                    state.getGrid().moveColumnTo( index,
                                                  state.getGridColumn() );
                    break;
                }
            }
            offsetX = offsetX + columnWidth;
        }

    }

}
