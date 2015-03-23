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
import java.util.Map;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Point2D;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.renderers.GridRendererRegistry;

public class GridWidget extends Group {

    private boolean isSelected = false;
    private Group selection = null;

    private Grid model;

    private ISelectionManager selectionManager;

    public GridWidget( final Grid model,
                       final ISelectionManager selectionManager ) {
        this.model = model;
        this.selectionManager = selectionManager;

        //Click handler
        addNodeMouseClickHandler( new GridWidgetMouseClickHandler() );
    }

    public Grid getModel() {
        return model;
    }

    public double getWidth() {
        double width = 0;
        for ( GridColumn column : model.getColumns() ) {
            width = width + column.getWidth();
        }
        return width;
    }

    private double getWidth( final int startColumnIndex,
                             final int endColumnIndex ) {
        double width = 0;
        final List<GridColumn> columns = model.getColumns();
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            width = width + column.getWidth();
        }
        return width;
    }

    public double getHeight() {
        return model.getData().size() * GridRendererRegistry.getRowHeight() + GridRendererRegistry.getHeaderHeight();
    }

    public void select() {
        isSelected = true;
        assertSelectionWidget();
        add( selection );
    }

    public void deselect() {
        isSelected = false;
        assertSelectionWidget();
        remove( selection );
    }

    private void assertSelectionWidget() {
        this.selection = GridRendererRegistry.renderSelector( getWidth(),
                                                              getHeight() );
    }

    @Override
    protected void drawWithoutTransforms( Context2D context,
                                          double alpha ) {
        if ( ( context.isSelection() ) && ( false == isListening() ) ) {
            return;
        }
        alpha = alpha * getAttributes().getAlpha();

        if ( alpha <= 0 ) {
            return;
        }

        if ( model.getColumns().isEmpty() ) {
            return;
        }

        this.removeAll();

        //Get viewable area dimensions
        final GridLayer gridLayer = ( (GridLayer) getLayer() );
        final Rectangle bounds = gridLayer.getVisibleBounds();
        final double vpX = bounds.getX();
        final double vpY = bounds.getY();
        final double vpHeight = bounds.getHeight();
        final double vpWidth = bounds.getWidth();

        final List<GridColumn> columns = model.getColumns();
        final List<Map<Integer, String>> data = model.getData();

        //Determine which columns are within visible area
        double x = 0;
        int minCol = -1;
        int maxCol = -1;
        for ( int i = 0; i < columns.size(); i++ ) {
            final GridColumn column = columns.get( i );
            if ( getX() + x + column.getWidth() > vpX ) {
                minCol = i;
                break;
            }
            x = x + column.getWidth();
        }
        x = 0;
        for ( int i = 0; i < columns.size(); i++ ) {
            final GridColumn column = columns.get( i );
            if ( getX() + x < vpX + vpWidth ) {
                maxCol = i;
            }
            x = x + column.getWidth();
        }

        //Determine which rows are within visible area
        int minRow = (int) ( ( vpY - getY() - GridRendererRegistry.getHeaderHeight() ) / GridRendererRegistry.getRowHeight() );
        if ( minRow < 0 ) {
            minRow = 0;
        }
        int maxRow = ( (int) ( ( vpY - getY() - GridRendererRegistry.getHeaderHeight() + vpHeight + GridRendererRegistry.getRowHeight() ) / GridRendererRegistry.getRowHeight() ) );
        if ( maxRow > data.size() ) {
            maxRow = data.size();
        }

        if ( minCol < 0 || maxCol < 0 || maxRow < minRow ) {
            return;
        }

        //Draw header if required
        if ( vpY - getY() < GridRendererRegistry.getHeaderHeight() && getY() < vpY + vpHeight ) {
            makeGridHeaderWidget( minCol,
                                  maxCol );
        }

        //Draw body if required
        makeGridBodyWidget( minCol,
                            maxCol,
                            minRow,
                            maxRow );

        //Include selection indicator if required
        if ( isSelected ) {
            select();
        }

        //Then render to the canvas
        super.drawWithoutTransforms( context,
                                     alpha );
    }

    private void makeGridHeaderWidget( final int startColumnIndex,
                                       final int endColumnIndex ) {
        final Group g = GridRendererRegistry.renderHeader( model,
                                                           startColumnIndex,
                                                           endColumnIndex,
                                                           getWidth( startColumnIndex,
                                                                     endColumnIndex ) );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        add( g );
    }

    private void makeGridBodyWidget( final int startColumnIndex,
                                     final int endColumnIndex,
                                     final int startRowIndex,
                                     final int endRowIndex ) {
        final Group g = GridRendererRegistry.renderBody( model,
                                                         startColumnIndex,
                                                         endColumnIndex,
                                                         startRowIndex,
                                                         endRowIndex,
                                                         getWidth( startColumnIndex,
                                                                   endColumnIndex ) );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        g.setY( GridRendererRegistry.getHeaderHeight() + startRowIndex * GridRendererRegistry.getRowHeight() );
        add( g );
    }

    private class GridWidgetMouseClickHandler implements NodeMouseClickHandler {

        @Override
        public void onNodeMouseClick( final NodeMouseClickEvent event ) {
            selectionManager.select( GridWidget.this.model );
            handleHeaderCellClick( event );
            handleBodyCellClick( event );
        }

        private void handleHeaderCellClick( final NodeMouseClickEvent event ) {
            final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( GridWidget.this,
                                                                                 new Point2D( event.getX(),
                                                                                              event.getY() ) );
            final double x = ap.getX();
            final double y = ap.getY();
            if ( x < 0 || x > GridWidget.this.getWidth() ) {
                return;
            }
            if ( y < 0 || y > GridRendererRegistry.getHeaderHeight() ) {
                return;
            }

            if ( GridWidget.this.model.getColumns() == null || GridWidget.this.model.getColumns().isEmpty() ) {
                return;
            }
            double extentX = 0;
            GridColumn extentColumn = null;
            for ( GridColumn column : GridWidget.this.model.getColumns() ) {
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
                final GridColumn link = extentColumn.getLink();
                selectionManager.scrollIntoView( link );
            }
        }

        private void handleBodyCellClick( final NodeMouseClickEvent event ) {
            final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( GridWidget.this,
                                                                                 new Point2D( event.getX(),
                                                                                              event.getY() ) );
            final double x = ap.getX();
            final double y = ap.getY();
            if ( x < 0 || x > GridWidget.this.getWidth() ) {
                return;
            }
            if ( y < GridRendererRegistry.getHeaderHeight() || y > GridWidget.this.getHeight() ) {
                return;
            }
            //Nothing to do at the moment
        }

    }

}
