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
package org.anstis.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.widget.GridShowcaseWidget;
import org.anstis.client.grid.widget.renderers.GridRendererRegistry;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public class GridShowcase implements EntryPoint {

    public void onModuleLoad() {
        GridRendererRegistry.addRenderer( new IGridRenderer() {

            private static final int HEADER_HEIGHT = 30;
            private static final int ROW_HEIGHT = 20;

            @Override
            public String getName() {
                return "Default";
            }

            @Override
            public double getHeaderHeight() {
                return HEADER_HEIGHT;
            }

            @Override
            public double getRowHeight() {
                return ROW_HEIGHT;
            }

            @Override
            public Group renderSelector( final double width,
                                         final double height ) {
                final Group g = new Group();
                final Rectangle r = new Rectangle( width,
                                                   height )
                        .setStrokeWidth( 2.0 )
                        .setStrokeColor( ColorName.GREEN )
                        .setListening( false );
                g.add( r );
                return g;
            }

            @Override
            public Group renderHeader( final Grid model,
                                       final int startColumnIndex,
                                       final int endColumnIndex,
                                       final double width ) {
                final Group g = new Group();
                final Rectangle header = new Rectangle( 0, 0 )
                        .setFillColor( ColorName.BISQUE )
                        .setStrokeColor( ColorName.SLATEGRAY )
                        .setStrokeWidth( 0.5 )
                        .setWidth( width )
                        .setHeight( HEADER_HEIGHT );
                g.add( header );

                final List<GridColumn> columns = model.getColumns();

                //Grid lines
                double x = 0;
                final MultiPath headerGrid = new MultiPath()
                        .setStrokeColor( ColorName.SLATEGRAY )
                        .setStrokeWidth( 0.5 )
                        .setListening( false );
                for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
                    final GridColumn column = columns.get( i );
                    headerGrid.M( x, 0 ).L( x,
                                            HEADER_HEIGHT );
                    x = x + column.getWidth();
                }
                g.add( headerGrid );

                //Linked columns
                x = 0;
                for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
                    final GridColumn column = columns.get( i );
                    final int w = column.getWidth();
                    if ( column.isLinked() ) {
                        final Rectangle lr = new Rectangle( w,
                                                            HEADER_HEIGHT )
                                .setFillColor( ColorName.BROWN )
                                .setStrokeColor( ColorName.SLATEGRAY )
                                .setStrokeWidth( 0.5 )
                                .setX( x );
                        g.add( lr );
                    }
                    x = x + w;
                }

                //Column text
                x = 0;
                for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
                    final GridColumn column = columns.get( i );
                    final int w = column.getWidth();
                    final Text t = new Text( column.getTitle() )
                            .setFillColor( ColorName.DEEPPINK )
                            .setX( x + w / 2 )
                            .setY( HEADER_HEIGHT / 2 )
                            .setFontSize( 12 )
                            .setListening( false )
                            .setTextBaseLine( TextBaseLine.MIDDLE )
                            .setTextAlign( TextAlign.CENTER );
                    g.add( t );
                    x = x + w;
                }

                return g;
            }

            @Override
            public Group renderBody( final Grid model,
                                     final int startColumnIndex,
                                     final int endColumnIndex,
                                     final int startRowIndex,
                                     final int endRowIndex,
                                     final double width ) {
                final Group g = new Group();
                final int rows = endRowIndex - startRowIndex;

                final Rectangle body = new Rectangle( 0, 0 )
                        .setFillColor( ColorName.LIGHTYELLOW )
                        .setStrokeColor( ColorName.SLATEGRAY )
                        .setStrokeWidth( 0.5 )
                        .setWidth( width )
                        .setHeight( ROW_HEIGHT * rows );
                g.add( body );

                final List<GridColumn> columns = model.getColumns();
                final List<Map<Integer, String>> data = model.getData();

                //Grid lines
                final double minX = 0;
                final double minY = 0;
                final double maxX = model.getColumnOffset( endColumnIndex ) - model.getColumnOffset( startColumnIndex ) + columns.get( endColumnIndex ).getWidth();
                final double maxY = ( endRowIndex - startRowIndex ) * ROW_HEIGHT;
                final MultiPath bodyGrid = new MultiPath()
                        .setStrokeColor( ColorName.SLATEGRAY )
                        .setStrokeWidth( 0.5 )
                        .setListening( false );
                double x = 0;
                for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
                    final GridColumn column = columns.get( i );
                    bodyGrid.M( x, minY ).L( x,
                                             maxY );
                    x = x + column.getWidth();
                }
                for ( int rowIndex = startRowIndex; rowIndex < endRowIndex; rowIndex++ ) {
                    bodyGrid.M( minX,
                                ( rowIndex - startRowIndex ) * ROW_HEIGHT ).L( maxX,
                                                                               ( rowIndex - startRowIndex ) * ROW_HEIGHT );
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
                    final double offsetY = ( rowIndex - startRowIndex ) * ROW_HEIGHT;
                    final Map<Integer, String> row = data.get( rowIndex );
                    for ( Map.Entry<Integer, String> e : row.entrySet() ) {
                        final int absoluteColumnIndex = e.getKey();
                        final int relativeColumnIndex = model.mapToRelativeIndex( absoluteColumnIndex );
                        if ( relativeColumnIndex >= startColumnIndex && relativeColumnIndex <= endColumnIndex ) {
                            final int columnWidth = columns.get( relativeColumnIndex ).getWidth();
                            final double offsetX = columnPositions.get( relativeColumnIndex );
                            final Text t = new Text( e.getValue() )
                                    .setFillColor( ColorName.DEEPPINK )
                                    .setX( offsetX + columnWidth / 2 )
                                    .setY( offsetY + ROW_HEIGHT / 2 )
                                    .setFontSize( 12 )
                                    .setListening( false )
                                    .setTextBaseLine( TextBaseLine.MIDDLE )
                                    .setTextAlign( TextAlign.CENTER );
                            g.add( t );
                        }
                    }
                }
                return g;
            }
        } );

        GridRendererRegistry.setActiveStyleName( "Default" );

        final GridShowcaseWidget container = new GridShowcaseWidget();
        RootPanel.get().add( container );
    }

}
