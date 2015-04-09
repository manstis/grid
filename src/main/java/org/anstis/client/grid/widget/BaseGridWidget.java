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
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.model.IGridData;
import org.anstis.client.grid.model.IGridRow;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public abstract class BaseGridWidget<M extends IGridData<?, ?, ?>> extends Group {

    private boolean isSelected = false;
    private Group selection = null;

    protected M model;
    protected IEditManager editManager;
    protected ISelectionManager selectionManager;
    protected IGridRenderer<M> renderer;

    public BaseGridWidget( final M model,
                           final IEditManager editManager,
                           final ISelectionManager selectionManager,
                           final IGridRenderer<M> renderer ) {
        this.model = model;
        this.editManager = editManager;
        this.selectionManager = selectionManager;
        this.renderer = renderer;
    }

    public M getModel() {
        return model;
    }

    public IGridRenderer<?> getRenderer() {
        return this.renderer;
    }

    public double getWidth() {
        double width = 0;
        for ( IGridColumn<?, ?> column : model.getColumns() ) {
            width = width + column.getWidth();
        }
        return width;
    }

    private double getWidth( final int startColumnIndex,
                             final int endColumnIndex ) {
        double width = 0;
        final List<? extends IGridColumn<?, ?>> columns = model.getColumns();
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final IGridColumn<?, ?> column = columns.get( i );
            width = width + column.getWidth();
        }
        return width;
    }

    public double getHeight() {
        double height = renderer.getHeaderHeight();
        height = height + model.getRowOffset( model.getRowCount() );
        return height;
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
        this.selection = renderer.renderSelector( getWidth(),
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

        final List<? extends IGridColumn<?, ?>> columns = model.getColumns();

        //Determine which columns are within visible area
        double x = 0;
        int minCol = -1;
        int maxCol = -1;
        for ( int i = 0; i < columns.size(); i++ ) {
            final IGridColumn<?, ?> column = columns.get( i );
            if ( getX() + x + column.getWidth() > vpX ) {
                minCol = i;
                break;
            }
            x = x + column.getWidth();
        }
        x = 0;
        for ( int i = 0; i < columns.size(); i++ ) {
            final IGridColumn<?, ?> column = columns.get( i );
            if ( getX() + x < vpX + vpWidth ) {
                maxCol = i;
            }
            x = x + column.getWidth();
        }

        //Determine which rows are within visible area
        int minRow = 0;
        IGridRow<?> row;
        double clipTop = vpY - getY() - renderer.getHeaderHeight();
        while ( ( row = model.getRow( minRow ) ).getHeight() < clipTop && minRow < model.getRowCount() - 1 ) {
            clipTop = clipTop - row.getHeight();
            minRow++;
        }

        int maxRow = minRow;
        double clipBottom = vpY - getY() - renderer.getHeaderHeight() + vpHeight - model.getRowOffset( minRow );
        while ( ( row = model.getRow( maxRow ) ).getHeight() < clipBottom && maxRow < model.getRowCount() - 1 ) {
            clipBottom = clipBottom - row.getHeight();
            maxRow++;
        }

        if ( minCol < 0 || maxCol < 0 || maxRow < minRow ) {
            return;
        }

        //Draw header if required
        if ( vpY - getY() < renderer.getHeaderHeight() && getY() < vpY + vpHeight ) {
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

    protected void makeGridHeaderWidget( final int startColumnIndex,
                                         final int endColumnIndex ) {
        final Group g = renderer.renderHeader( model,
                                               startColumnIndex,
                                               endColumnIndex,
                                               getWidth( startColumnIndex,
                                                         endColumnIndex ) );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        add( g );
    }

    protected void makeGridBodyWidget( final int startColumnIndex,
                                       final int endColumnIndex,
                                       final int startRowIndex,
                                       final int endRowIndex ) {
        final Group g = renderer.renderBody( model,
                                             startColumnIndex,
                                             endColumnIndex,
                                             startRowIndex,
                                             endRowIndex,
                                             getWidth( startColumnIndex,
                                                       endColumnIndex ) );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        g.setY( renderer.getHeaderHeight() + model.getRowOffset( startRowIndex ) );
        add( g );
    }

}
