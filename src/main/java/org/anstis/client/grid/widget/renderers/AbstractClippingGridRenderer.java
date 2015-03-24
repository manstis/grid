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
package org.anstis.client.grid.widget.renderers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public abstract class AbstractClippingGridRenderer implements IGridRenderer {

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
    public Group renderHeader( final Grid model,
                               final int startColumnIndex,
                               final int endColumnIndex,
                               final double width ) {
        final Group g = new Group();
        final Rectangle header = getHeader()
                .setWidth( width )
                .setHeight( getHeaderHeight() )
                .setListening( false );
        g.add( header );

        final List<GridColumn> columns = model.getColumns();

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

        //Column text
        x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            final int w = column.getWidth();
            final Text t = getHeaderText()
                    .setX( x + w / 2 )
                    .setY( getHeaderHeight() / 2 )
                    .setListening( false )
                    .setTextBaseLine( TextBaseLine.MIDDLE )
                    .setTextAlign( TextAlign.CENTER )
                    .setText( column.getTitle() );
            g.add( t );
            x = x + w;
        }

        return g;
    }

    public abstract Rectangle getHeader();

    public abstract MultiPath getHeaderGridLine();

    public abstract Rectangle getHeaderLink();

    public abstract Text getHeaderText();

    @Override
    public Group renderBody( final Grid model,
                             final int startColumnIndex,
                             final int endColumnIndex,
                             final int startRowIndex,
                             final int endRowIndex,
                             final double width ) {
        final Group g = new Group();
        final int rows = endRowIndex - startRowIndex;

        final Rectangle body = getBody().setWidth( width ).setHeight( getRowHeight() * rows );
        g.add( body );

        final List<GridColumn> columns = model.getColumns();
        final List<Map<Integer, String>> data = model.getData();

        //Grid lines
        final double minX = 0;
        final double minY = 0;
        final double maxX = model.getColumnOffset( endColumnIndex ) - model.getColumnOffset( startColumnIndex ) + columns.get( endColumnIndex ).getWidth();
        final double maxY = ( endRowIndex - startRowIndex ) * getRowHeight();
        final MultiPath bodyGrid = getBodyGridLine().setListening( false );
        double x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            bodyGrid.M( x, minY ).L( x,
                                     maxY );
            x = x + column.getWidth();
        }
        for ( int rowIndex = startRowIndex; rowIndex < endRowIndex; rowIndex++ ) {
            bodyGrid.M( minX,
                        ( rowIndex - startRowIndex ) * getRowHeight() ).L( maxX,
                                                                           ( rowIndex - startRowIndex ) * getRowHeight() );
        }
        g.add( bodyGrid );

        //Cell content
        final List<Double> columnPositions = new ArrayList<>();
        x = 0;
        for ( GridColumn column : columns ) {
            columnPositions.add( x - model.getColumnOffset( startColumnIndex ) );
            x = x + column.getWidth();
        }

        for ( int rowIndex = startRowIndex; rowIndex < endRowIndex; rowIndex++ ) {
            final double offsetY = ( rowIndex - startRowIndex ) * getRowHeight();
            final Map<Integer, String> row = data.get( rowIndex );
            for ( Map.Entry<Integer, String> e : row.entrySet() ) {
                final int absoluteColumnIndex = e.getKey();
                final int relativeColumnIndex = model.mapToRelativeIndex( absoluteColumnIndex );
                if ( relativeColumnIndex >= startColumnIndex && relativeColumnIndex <= endColumnIndex ) {
                    final int columnWidth = columns.get( relativeColumnIndex ).getWidth();
                    final double offsetX = columnPositions.get( relativeColumnIndex );
                    final Text t = getBodyText()
                            .setX( offsetX + columnWidth / 2 )
                            .setY( offsetY + getRowHeight() / 2 )
                            .setTextBaseLine( TextBaseLine.MIDDLE )
                            .setTextAlign( TextAlign.CENTER )
                            .setText( e.getValue() );
                    g.add( t );
                }
            }
        }
        return g;
    }

    public abstract Rectangle getBody();

    public abstract MultiPath getBodyGridLine();

    public abstract Text getBodyText();

}
