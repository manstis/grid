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

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseDoubleClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseDoubleClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.core.client.Callback;
import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.IGrid;
import org.anstis.client.grid.model.basic.IGridCell;
import org.anstis.client.grid.model.basic.IGridData;
import org.anstis.client.grid.util.GridCoordinateUtils;
import org.anstis.client.grid.widget.renderers.GridRendererRegistry;

public class GridWidget extends Group {

    private boolean isSelected = false;
    private Group selection = null;

    private IGrid<?> model;

    private IEditManager editManager;
    private ISelectionManager selectionManager;

    public GridWidget( final IGrid<?> model,
                       final IEditManager editManager,
                       final ISelectionManager selectionManager ) {
        this.model = model;
        this.editManager = editManager;
        this.selectionManager = selectionManager;

        //Click handler
        addNodeMouseClickHandler( new GridWidgetMouseClickHandler() );

        //Double-click handler
        addNodeMouseDoubleClickHandler( new GridWidgetMouseDoubleClickHandler() );
    }

    public IGrid getModel() {
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
        return model.getData().getRowCount() * GridRendererRegistry.getRowHeight() + GridRendererRegistry.getHeaderHeight();
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
        final IGridData<?, ?> data = model.getData();

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
        if ( maxRow > data.getRowCount() ) {
            maxRow = data.getRowCount();
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

    }

    private class GridWidgetMouseDoubleClickHandler implements NodeMouseDoubleClickHandler {

        @Override
        public void onNodeMouseDoubleClick( final NodeMouseDoubleClickEvent event ) {
            handleBodyCellClick( event );
        }

        private void handleBodyCellClick( final NodeMouseDoubleClickEvent event ) {
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
            int _columnIndex = -1;
            final int rowIndex = (int) ( ( y - GridRendererRegistry.getHeaderHeight() ) / GridRendererRegistry.getRowHeight() );
            final List<GridColumn> columns = getModel().getColumns();
            double offsetX = 0;
            for ( int idx = 0; idx < columns.size(); idx++ ) {
                final GridColumn gridColumn = columns.get( idx );
                final double width = gridColumn.getWidth();
                if ( x > offsetX && x < offsetX + width ) {
                    _columnIndex = idx;
                    break;
                }
                offsetX = offsetX + width;
            }
            if ( _columnIndex < 0 || _columnIndex > columns.size() - 1 ) {
                return;
            }
            if ( rowIndex < 0 || rowIndex > getModel().getData().getRowCount() - 1 ) {
                return;
            }
            final int columnIndex = getModel().mapToAbsoluteIndex( _columnIndex );

            final IGridCell cell = getModel().getData().getCell( rowIndex,
                                                                 columnIndex );
            editManager.edit( cell == null ? "" : cell.getValue(),
                              new Callback<String, String>() {
                                  @Override
                                  public void onFailure( final String value ) {
                                      //Do nothing
                                  }

                                  @Override
                                  public void onSuccess( final String value ) {
                                      getModel().getData().setCell( rowIndex,
                                                                    columnIndex,
                                                                    getModel().getData().newCell( value ) );
                                      GridWidget.this.getLayer().draw();
                                  }
                              } );
        }

    }

}
