package org.anstis.client.grid.widget.dnd;

import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.mediator.IMediator;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.dom.client.Style;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.GridWidget;

import static org.anstis.client.grid.widget.dnd.GridWidgetHandlersSettings.*;

public class GridWidgetMouseMoveHandler implements NodeMouseMoveHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState state;
    private final Map<Grid, GridWidget> selectables;

    public GridWidgetMouseMoveHandler( final GridLayer layer,
                                       final GridWidgetHandlersState state,
                                       final Map<Grid, GridWidget> selectables ) {
        this.layer = layer;
        this.state = state;
        this.selectables = selectables;
    }

    @Override
    public void onNodeMouseMove( final NodeMouseMoveEvent event ) {
        switch ( state.getOperation() ) {
            case COLUMN_RESIZE:
                //If we're currently resizing a column we don't need to find a column
                handleColumnResize( event );
                break;
            case COLUMN_MOVE:
                //If we're currently moving a column we don't need to find a column
                handleColumnMove( event );
                break;
            default:
                //Otherwise try to find a Grid and GridColumn
                state.setGrid( null );
                state.setGridColumn( null );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
                Style.Cursor cursor = Style.Cursor.DEFAULT;

                for ( Map.Entry<Grid, GridWidget> e : selectables.entrySet() ) {
                    final Grid grid = e.getKey();
                    final GridWidget gridWidget = e.getValue();
                    final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                                         new Point2D( event.getX(),
                                                                                                      event.getY() ) );

                    final double ax = ap.getX();
                    final double ay = ap.getY();
                    if ( ax < 0 || ax > gridWidget.getWidth() ) {
                        continue;
                    }
                    if ( ay < 0 || ay > gridWidget.getHeight() ) {
                        continue;
                    } else if ( ay < GridWidget.HEADER_HEIGHT ) {
                        double offsetX = 0;
                        for ( GridColumn gc : grid.getColumns() ) {
                            //Check for column moving
                            final double columnWidth = gc.getWidth();
                            if ( ax > offsetX && ax < offsetX + columnWidth ) {
                                state.setGrid( grid );
                                state.setGridColumn( gc );
                                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_MOVE_PENDING );
                                cursor = Style.Cursor.MOVE;
                                break;
                            }
                            offsetX = offsetX + columnWidth;
                        }

                    } else {
                        double offsetX = 0;
                        for ( GridColumn gc : grid.getColumns() ) {
                            //Check for column resizing
                            final double columnWidth = gc.getWidth();
                            if ( ax > columnWidth + offsetX - COLUMN_RESIZE_HANDLE_SENSITIVITY && ax < columnWidth + offsetX + COLUMN_RESIZE_HANDLE_SENSITIVITY ) {
                                state.setGrid( grid );
                                state.setGridColumn( gc );
                                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_RESIZE_PENDING );
                                cursor = Style.Cursor.COL_RESIZE;
                                break;
                            }
                            offsetX = offsetX + columnWidth;
                        }
                    }
                }

                layer.getViewport().getElement().getStyle().setCursor( cursor );
                for ( IMediator mediator : layer.getViewport().getMediators() ) {
                    mediator.setEnabled( state.getGrid() == null );
                }
        }
    }

    private void handleColumnResize( final NodeMouseMoveEvent event ) {
        final GridWidget gridWidget = selectables.get( state.getGrid() );
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double deltaX = ap.getX() - state.getEventInitialX();
        double columnNewWidth = state.getEventInitialColumnWidth() + deltaX;
        if ( columnNewWidth < COLUMN_MIN_WIDTH ) {
            columnNewWidth = COLUMN_MIN_WIDTH;
        }
        state.getGridColumn().setWidth( (int) ( columnNewWidth ) );
        layer.draw();
        return;
    }

    private void handleColumnMove( final NodeMouseMoveEvent event ) {
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
            final double columnMovedWidth = state.getGridColumn().getWidth();
            final double minColX = Math.max( offsetX, offsetX + ( columnWidth - columnMovedWidth ) / 2 );
            final double maxColX = Math.min( offsetX + columnWidth, offsetX + ( columnWidth + columnMovedWidth ) / 2 );
            if ( ax > minColX && ax < maxColX ) {
                state.getGrid().moveColumnTo( index,
                                              state.getGridColumn() );
                state.getEventColumnHighlight().setX( gridWidget.getX() + gridWidget.getModel().getColumnOffset( state.getGridColumn() ) );
                layer.draw();
                break;
            }
            offsetX = offsetX + columnWidth;
        }

    }

}
