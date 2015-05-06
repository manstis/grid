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
package org.anstis.client.grid.widget.renderers.basic;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Transform;
import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.GridData;
import org.anstis.client.grid.model.basic.GridRow;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.context.GridBodyRenderContext;
import org.anstis.client.grid.widget.context.GridCellRenderContext;
import org.anstis.client.grid.widget.context.GridHeaderRenderContext;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public abstract class AbstractClippingGridRenderer implements IGridRenderer<GridData> {

    @Override
    public Group renderSelector( final double width,
                                 final double height ) {
        final Group g = new Group();
        final Rectangle selector = getSelector()
                .setWidth( width )
                .setHeight( height )
                .setListening( false );
        g.add( selector );
        return g;
    }

    public abstract Rectangle getSelector();

    @Override
    public Group renderHeader( final GridData model,
                               final GridHeaderRenderContext context ) {
        final int startColumnIndex = context.getStartColumnIndex();
        final int endColumnIndex = context.getEndColumnIndex();
        final double width = context.getWidth();

        final Group g = new Group();
        final Rectangle header = getHeader()
                .setWidth( width )
                .setHeight( getHeaderHeight() )
                .setListening( false );
        g.add( header );

        final List<GridColumn<?>> columns = model.getColumns();

        //Linked columns
        double x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            final int w = column.getWidth();
            if ( column.isLinked() ) {
                final Rectangle lr = getHeaderLink()
                        .setWidth( w )
                        .setHeight( getHeaderHeight() )
                        .setX( x );
                g.add( lr );
            }
            x = x + w;
        }

        //Grid lines
        x = 0;
        final MultiPath headerGrid = getHeaderGridLine().setListening( false );
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            headerGrid.M( x, 0 ).L( x,
                                    getHeaderHeight() );
            x = x + column.getWidth();
        }
        g.add( headerGrid );

        //Column title
        x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            final Group hc = column.renderHeader();
            final int w = column.getWidth();
            hc.setX( x + w / 2 )
                    .setY( getHeaderHeight() / 2 )
                    .setListening( false );
            g.add( hc );
            x = x + w;
        }

        return g;
    }

    public abstract Rectangle getHeader();

    public abstract MultiPath getHeaderGridLine();

    public abstract Rectangle getHeaderLink();

    @Override
    public Group renderBody( final GridData model,
                             final GridBodyRenderContext context ) {
        final int startColumnIndex = context.getStartColumnIndex();
        final int endColumnIndex = context.getEndColumnIndex();
        final int startRowIndex = context.getStartRowIndex();
        final int endRowIndex = context.getEndRowIndex();
        final double width = context.getWidth();
        final Transform transform = context.getTransform();
        final BaseGridWidget<?> widget =context.getWidget();

        final Group g = new Group();
        final List<GridColumn<?>> columns = model.getColumns();

        final List<Double> rowOffsets = new ArrayList<>();
        double rowOffset = model.getRowOffset( startRowIndex );
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            rowOffsets.add( rowOffset );
            rowOffset = rowOffset + model.getRow( rowIndex ).getHeight();
        }

        final double maxY = rowOffsets.get( endRowIndex - startRowIndex ) - rowOffsets.get( 0 ) + model.getRow( endRowIndex ).getHeight();
        final double maxX = model.getColumnOffset( endColumnIndex ) - model.getColumnOffset( startColumnIndex ) + columns.get( endColumnIndex ).getWidth();
        final Rectangle body = getBody().setWidth( width ).setHeight( maxY );
        g.add( body );

        //Grid lines
        final MultiPath bodyGrid = getBodyGridLine().setListening( false );
        double x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            bodyGrid.M( x,
                        0 ).L( x,
                               maxY );
            x = x + column.getWidth();
        }
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
            bodyGrid.M( 0,
                        y ).L( maxX,
                               y );
        }
        g.add( bodyGrid );

        //Cell content
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
            final GridRow row = model.getRow( rowIndex );
            x = 0;
            for ( int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++ ) {
                final GridColumn column = columns.get( columnIndex );
                final double rowHeight = row.getHeight();
                final double columnWidth = column.getWidth();
                final GridCellRenderContext cellContext = new GridCellRenderContext( model.getColumnOffset( columnIndex ) + context.getX(),
                                                                                     rowOffsets.get( rowIndex - startRowIndex ) + context.getY() + getHeaderHeight(),
                                                                                     columnWidth,
                                                                                     rowHeight,
                                                                                     rowIndex,
                                                                                     columnIndex,
                                                                                     transform,
                                                                                     widget);
                final Group hc = column.renderRow( row,
                                                   cellContext );
                if ( hc != null ) {
                    hc.setX( x + columnWidth / 2 )
                            .setY( y + model.getRow( rowIndex ).getHeight() / 2 )
                            .setListening( false );
                    g.add( hc );
                }
                x = x + columnWidth;
            }
        }

        return g;
    }

    public abstract Rectangle getBody();

    public abstract MultiPath getBodyGridLine();

}
