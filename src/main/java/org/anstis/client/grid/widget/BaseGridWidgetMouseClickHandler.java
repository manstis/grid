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

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.types.Point2D;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public abstract class BaseGridWidgetMouseClickHandler<W extends BaseGridWidget<?>> implements NodeMouseClickHandler {

    protected W grid;
    protected IEditManager editManager;
    protected ISelectionManager selectionManager;
    protected IGridRenderer<?> renderer;

    public BaseGridWidgetMouseClickHandler( final W grid,
                                            final IEditManager editManager,
                                            final ISelectionManager selectionManager,
                                            final IGridRenderer<?> renderer ) {
        this.grid = grid;
        this.editManager = editManager;
        this.selectionManager = selectionManager;
        this.renderer = renderer;
    }

    @Override
    public void onNodeMouseClick( final NodeMouseClickEvent event ) {
        selectionManager.select( grid.getModel() );
        handleHeaderCellClick( event );
        handleBodyCellClick( event );
    }

    protected void handleHeaderCellClick( final NodeMouseClickEvent event ) {
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( grid,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double x = ap.getX();
        final double y = ap.getY();
        if ( x < 0 || x > grid.getWidth() ) {
            return;
        }
        if ( y < 0 || y > renderer.getHeaderHeight() ) {
            return;
        }

        if ( grid.getModel().getColumns() == null || grid.getModel().getColumns().isEmpty() ) {
            return;
        }
        double extentX = 0;
        IGridColumn<?, ?> extentColumn = null;
        for ( IGridColumn<?, ?> column : grid.getModel().getColumns() ) {
            if ( x > extentX && x < extentX + column.getWidth() ) {
                extentColumn = column;
                break;
            }
            extentX = extentX + column.getWidth();
        }
        if ( extentColumn == null ) {
            return;
        }

        if ( extentColumn.isLinked() ) {
            final IGridColumn<?, ?> link = extentColumn.getLink();
            selectionManager.scrollIntoView( link );
        }
    }

    protected void handleBodyCellClick( final NodeMouseClickEvent event ) {
        //Do nothing by default
    }

}
