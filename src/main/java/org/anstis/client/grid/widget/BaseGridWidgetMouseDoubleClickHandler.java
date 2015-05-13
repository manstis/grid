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
package org.anstis.client.grid.widget;

import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseDoubleClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseDoubleClickHandler;
import com.ait.lienzo.client.core.types.Point2D;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.model.IGridRow;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public abstract class BaseGridWidgetMouseDoubleClickHandler<W extends BaseGridWidget<?>> implements NodeMouseDoubleClickHandler {

    protected W gridWidget;
    protected ISelectionManager selectionManager;
    protected IGridRenderer<?> renderer;

    public BaseGridWidgetMouseDoubleClickHandler( final W gridWidget,
                                                  final ISelectionManager selectionManager,
                                                  final IGridRenderer<?> renderer ) {
        this.gridWidget = gridWidget;
        this.selectionManager = selectionManager;
        this.renderer = renderer;
    }

    @Override
    public void onNodeMouseDoubleClick( final NodeMouseDoubleClickEvent event ) {
        handleHeaderCellDoubleClick( event );
        handleBodyCellDoubleClick( event );
    }

    protected void handleHeaderCellDoubleClick( final NodeMouseDoubleClickEvent event ) {
        //Do nothing by default
    }

    protected void handleBodyCellDoubleClick( final NodeMouseDoubleClickEvent event ) {
        //Convert Canvas co-ordinate to Grid co-ordinate
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double x = ap.getX();
        final double y = ap.getY();
        if ( x < 0 || x > gridWidget.getWidth() ) {
            return;
        }
        if ( y < renderer.getHeaderHeight() || y > gridWidget.getHeight() ) {
            return;
        }

        //Get row index
        IGridRow<?> row;
        int rowIndex = 0;
        double offsetY = y - renderer.getHeaderHeight();
        while ( ( row = gridWidget.getModel().getRow( rowIndex ) ).getHeight() < offsetY ) {
            offsetY = offsetY - row.getHeight();
            rowIndex++;
        }
        if ( rowIndex < 0 || rowIndex > gridWidget.getModel().getRowCount() - 1 ) {
            return;
        }

        //Get column index
        int columnIndex = -1;
        double offsetX = 0;
        final List<? extends IGridColumn<?, ?>> columns = gridWidget.getModel().getColumns();
        for ( int idx = 0; idx < columns.size(); idx++ ) {
            final IGridColumn<?, ?> gridColumn = columns.get( idx );
            final double width = gridColumn.getWidth();
            if ( x > offsetX && x < offsetX + width ) {
                columnIndex = idx;
                break;
            }
            offsetX = offsetX + width;
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return;
        }

        doEdit( rowIndex,
                columnIndex );
    }

    protected abstract void doEdit( final int rowIndex,
                                    final int columnIndex );

}
