/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anstis.client.grid.widget.dnd;

import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Point2D;
import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.IGrid;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.GridWidget;

public class GridWidgetMouseDownHandler implements NodeMouseDownHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState state;
    private final Map<IGrid<?>, GridWidget> selectables;

    public GridWidgetMouseDownHandler( final GridLayer layer,
                                       final GridWidgetHandlersState state,
                                       final Map<IGrid<?>, GridWidget> selectables ) {
        this.layer = layer;
        this.state = state;
        this.selectables = selectables;
    }

    @Override
    public void onNodeMouseDown( final NodeMouseDownEvent event ) {
        if ( state.getGrid() == null || state.getGridColumn() == null ) {
            return;
        }

        final GridWidget gridWidget = selectables.get( state.getGrid() );
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        switch ( state.getOperation() ) {
            case COLUMN_RESIZE_PENDING:
                state.setEventInitialX( ap.getX() );
                state.setEventInitialColumnWidth( state.getGridColumn().getWidth() );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_RESIZE );
                break;
            case COLUMN_MOVE_PENDING:
                showColumnHighlight( state.getGrid(),
                                     state.getGridColumn() );
                state.setEventInitialX( ap.getX() );
                state.setEventInitialColumnWidth( state.getGridColumn().getWidth() );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_MOVE );
                break;
        }
    }

    private void showColumnHighlight( final IGrid<?> grid,
                                      final GridColumn gridColumn ) {
        final GridWidget gridWidget = selectables.get( state.getGrid() );
        final double highlightOffsetX = grid.getColumnOffset( gridColumn );

        final Rectangle bounds = layer.getVisibleBounds();
        final double highlightHeight = Math.min( bounds.getY() + bounds.getHeight() - gridWidget.getY(),
                                                 gridWidget.getHeight() );

        state.getEventColumnHighlight().setWidth( gridColumn.getWidth() )
                .setHeight( highlightHeight )
                .setX( gridWidget.getX() + highlightOffsetX )
                .setY( gridWidget.getY() );
        layer.add( state.getEventColumnHighlight() );
        layer.getLayer().draw();
    }

}
