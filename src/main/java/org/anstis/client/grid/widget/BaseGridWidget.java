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
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Viewport;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.model.IGridData;
import org.anstis.client.grid.model.IGridRow;
import org.anstis.client.grid.widget.context.GridBodyRenderContext;
import org.anstis.client.grid.widget.context.GridHeaderRenderContext;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public abstract class BaseGridWidget<M extends IGridData<?, ?, ?>, R extends IGridRenderer<M>> extends Group implements NodeMouseClickHandler {

    private boolean isSelected = false;
    private Group selection = null;

    protected M model;
    protected ISelectionManager selectionManager;
    protected R renderer;

    public BaseGridWidget( final M model,
                           final ISelectionManager selectionManager,
                           final R renderer ) {
        this.model = model;
        this.selectionManager = selectionManager;
        this.renderer = renderer;
    }

    public M getModel() {
        return model;
    }

    public IGridRenderer<M> getRenderer() {
        return this.renderer;
    }

    public void setRenderer( final R renderer ) {
        this.renderer = renderer;
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

        //Draw header if required
        if ( minCol >= 0 && maxCol >= 0 ) {
            if ( vpY - getY() < renderer.getHeaderHeight() && getY() < vpY + vpHeight ) {
                makeGridHeaderWidget( minCol,
                                      maxCol );
            }
        } else {
            for ( int i = 0; i < columns.size(); i++ ) {
                final IGridColumn<?, ?> column = columns.get( i );
                column.destroyResources();
            }
            return;
        }

        //Determine which rows are within visible area
        if ( model.getRowCount() > 0 ) {
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

            //Draw body if required
            if ( minRow < maxRow || ( clipTop < model.getRow( minRow ).getHeight() && clipBottom > 0 ) ) {

                //Signal columns to attach or detach rendering support
                for ( int i = 0; i < columns.size(); i++ ) {
                    final IGridColumn<?, ?> column = columns.get( i );
                    if ( i >= minCol && i <= maxCol ) {
                        column.initialiseResources();
                    } else {
                        column.destroyResources();
                    }
                }

                makeGridBodyWidget( minCol,
                                    maxCol,
                                    minRow,
                                    maxRow );

                //Signal columns to free any unused resources
                for ( int i = 0; i < columns.size(); i++ ) {
                    final IGridColumn<?, ?> column = columns.get( i );
                    if ( i >= minCol && i <= maxCol ) {
                        column.freeResources();
                    }
                }

            } else {
                for ( int i = 0; i < columns.size(); i++ ) {
                    final IGridColumn<?, ?> column = columns.get( i );
                    column.destroyResources();
                }
            }
        }

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
        final Viewport viewport = BaseGridWidget.this.getViewport();
        final GridHeaderRenderContext context = new GridHeaderRenderContext( startColumnIndex,
                                                                             endColumnIndex,
                                                                             getWidth( startColumnIndex,
                                                                                       endColumnIndex ),
                                                                             viewport.getTransform(),
                                                                             this );
        final Group g = renderer.renderHeader( model,
                                               context );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        add( g );
    }

    protected void makeGridBodyWidget( final int startColumnIndex,
                                       final int endColumnIndex,
                                       final int startRowIndex,
                                       final int endRowIndex ) {
        final Viewport viewport = BaseGridWidget.this.getViewport();
        final GridBodyRenderContext context = new GridBodyRenderContext( getX(),
                                                                         getY(),
                                                                         getWidth( startColumnIndex,
                                                                                   endColumnIndex ),
                                                                         startColumnIndex,
                                                                         endColumnIndex,
                                                                         startRowIndex,
                                                                         endRowIndex,
                                                                         viewport.getTransform(),
                                                                         this );
        final Group g = renderer.renderBody( model,
                                             context );
        g.setX( model.getColumnOffset( startColumnIndex ) );
        g.setY( renderer.getHeaderHeight() + model.getRowOffset( startRowIndex ) );
        add( g );
    }

}
