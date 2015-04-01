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
package org.anstis.client.grid.widget.mergable;

import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.types.Point2D;
import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.model.mergable.MergableGridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.BaseGridWidgetMouseClickHandler;
import org.anstis.client.grid.widget.IEditManager;
import org.anstis.client.grid.widget.ISelectionManager;
import org.anstis.client.grid.widget.renderers.mergable.GroupingToggle;
import org.anstis.client.grid.widget.renderers.mergable.IMergableGridRenderer;

public class MergableGridWidgetMouseClickHandler extends BaseGridWidgetMouseClickHandler<MergableGridWidget> {

    public MergableGridWidgetMouseClickHandler( final MergableGridWidget grid,
                                                final IEditManager editManager,
                                                final ISelectionManager selectionManager,
                                                final IMergableGridRenderer renderer ) {
        super( grid,
               editManager,
               selectionManager,
               renderer );
    }

    @Override
    protected void handleBodyCellClick( final NodeMouseClickEvent event ) {
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( grid,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double x = ap.getX();
        final double y = ap.getY();
        if ( x < 0 || x > grid.getWidth() ) {
            return;
        }
        if ( y < renderer.getHeaderHeight() || y > grid.getHeight() ) {
            return;
        }
        int columnIndex = -1;
        final int rowIndex = (int) ( ( y - renderer.getHeaderHeight() ) / renderer.getRowHeight() );
        final List<MergableGridColumn> columns = grid.getModel().getColumns();
        double offsetX = 0;
        for ( int idx = 0; idx < columns.size(); idx++ ) {
            final MergableGridColumn gridColumn = columns.get( idx );
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
        if ( rowIndex < 0 || rowIndex > grid.getModel().getRowCount() - 1 ) {
            return;
        }
        final MergableGridCell cell = grid.getModel().getCell( rowIndex,
                                                               columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( cell.getMergedCellCount() > 1 ) {
            final MergableGridColumn gridColumn = columns.get( columnIndex );
            final double rx = x-offsetX;
            final double ry = ( ( y - renderer.getHeaderHeight() ) / renderer.getRowHeight() )*renderer.getRowHeight();
            final GroupingToggle gt = new GroupingToggle( gridColumn.getWidth(),
                                                          renderer.getRowHeight(),
                                                          cell.isGrouped() );
            if(gt.onHotSpot(rx, ry)) {
            grid.getModel().groupCell( rowIndex,
                                       columnIndex,
                                       !cell.isGrouped() );
            }
        }
    }

}
