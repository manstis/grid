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
package org.anstis.client.grid.widget.renderers.mergable;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.ColorName;
import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.model.mergable.MergableGridColumn;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.model.mergable.MergableGridRow;

public class MergableGridRenderer implements IMergableGridRenderer {

    private static final int HEADER_HEIGHT = 34;

    @Override
    public String getName() {
        return "Custom";
    }

    @Override
    public double getHeaderHeight() {
        return HEADER_HEIGHT;
    }

    @Override
    public Group renderSelector( final double width,
                                 final double height ) {
        final Group g = new Group();
        final Rectangle r = new Rectangle( width,
                                           height )
                .setStrokeWidth( 2.0 )
                .setStrokeColor( ColorName.GREEN )
                .setShadow( new Shadow( ColorName.DARKGREEN, 4, 0.0, 0.0 ) )
                .setListening( false );
        g.add( r );
        return g;
    }

    @Override
    public Group renderHeader( final MergableGridData model,
                               final int startColumnIndex,
                               final int endColumnIndex,
                               final double width ) {
        final Group g = new Group();
        final Rectangle header = new Rectangle( 0, 0 )
                .setFillColor( ColorName.BISQUE )
                .setStrokeColor( ColorName.GREY )
                .setStrokeWidth( 0.5 )
                .setWidth( width )
                .setHeight( HEADER_HEIGHT );
        g.add( header );

        final List<MergableGridColumn> columns = model.getColumns();

        //Linked columns
        double x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final MergableGridColumn column = columns.get( i );
            final int w = column.getWidth();
            if ( column.isLinked() ) {
                final Rectangle lr = new Rectangle( w,
                                                    HEADER_HEIGHT )
                        .setFillColor( ColorName.BROWN )
                        .setStrokeColor( ColorName.GREY )
                        .setStrokeWidth( 0.5 )
                        .setX( x );
                g.add( lr );
            }
            x = x + w;
        }

        //Grid lines
        x = 0;
        final MultiPath headerGrid = new MultiPath()
                .setStrokeColor( ColorName.GREY )
                .setStrokeWidth( 0.5 )
                .setListening( false );
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final MergableGridColumn column = columns.get( i );
            headerGrid.M( x, 0 ).L( x,
                                    HEADER_HEIGHT );
            x = x + column.getWidth();
        }
        g.add( headerGrid );

        final MultiPath headerDivider = new MultiPath()
                .setStrokeColor( ColorName.GREY )
                .setStrokeWidth( 1.0 )
                .setListening( false );
        headerDivider.M( 0,
                         HEADER_HEIGHT - 4 )
                .L( x,
                    HEADER_HEIGHT - 4 );
        g.add( headerDivider );

        //Column text
        x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final MergableGridColumn column = columns.get( i );
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

    @Override
    public Group renderBody( final MergableGridData model,
                             final int startColumnIndex,
                             final int endColumnIndex,
                             final int startRowIndex,
                             final int endRowIndex,
                             final double width ) {
        final Group g = new Group();
        final List<MergableGridColumn> columns = model.getColumns();

        final List<Double> rowOffsets = new ArrayList<>();
        double rowOffset = model.getRowOffset( startRowIndex );
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            rowOffsets.add( rowOffset );
            rowOffset = rowOffset + model.getRow( rowIndex ).getHeight();
        }

        final double maxY = rowOffsets.get( endRowIndex - startRowIndex ) - rowOffsets.get( 0 ) + model.getRow( endRowIndex ).getHeight();
        final double maxX = model.getColumnOffset( endColumnIndex ) - model.getColumnOffset( startColumnIndex ) + columns.get( endColumnIndex ).getWidth();
        final Rectangle body = new Rectangle( 0, 0 )
                .setFillColor( ColorName.LIGHTYELLOW )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setWidth( width )
                .setHeight( maxY );
        g.add( body );

        //Grid lines
        final MultiPath bodyGrid = new MultiPath()
                .setStrokeColor( ColorName.DARKGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false );
        double x = 0;
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final MergableGridColumn column = columns.get( i );
            bodyGrid.M( x,
                        0 ).L( x,
                               maxY );
            x = x + column.getWidth();
        }
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            x = 0;
            final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
            final MergableGridRow row = model.getRow( rowIndex );
            if ( row.isMerged() ) {
                if ( !row.isCollapsed() ) {
                    for ( int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++ ) {
                        final MergableGridColumn column = columns.get( columnIndex );
                        final MergableGridCell cell = model.getCell( rowIndex,
                                                                     columnIndex );
                        if ( cell == null || cell.getMergedCellCount() > 0 ) {
                            bodyGrid.M( x,
                                        y ).L( x + column.getWidth(),
                                               y );
                        }
                        x = x + column.getWidth();
                    }
                }
            } else {
                bodyGrid.M( x,
                            y ).L( maxX,
                                   y );

            }
        }
        g.add( bodyGrid );

        //Cell content
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            x = 0;
            final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
            final MergableGridRow row = model.getRow( rowIndex );
            for ( int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++ ) {
                final MergableGridColumn column = columns.get( columnIndex );
                final int w = column.getWidth();

                final MergableGridCell cell = model.getCell( rowIndex,
                                                             columnIndex );
                if ( cell != null ) {
                    if ( !row.isCollapsed() ) {
                        if ( cell.getMergedCellCount() > 0 ) {
                            final Group hc = column.renderRow( row );
                            if ( hc != null ) {
                                hc.setX( x + w / 2 )
                                        .setY( y + getCellHeight( rowIndex,
                                                                  model,
                                                                  cell ) / 2 )
                                        .setListening( false );
                                g.add( hc );
                            }
                        }
                        if ( cell.getMergedCellCount() > 1 ) {
                            final GroupingToggle gt = renderGroupedCellToggle( w,
                                                                               model.getRow( rowIndex ).getHeight(),
                                                                               cell.isGrouped() );
                            gt.setX( x ).setY( y );
                            g.add( gt );
                        }
                    }
                }
                x = x + w;
            }
        }

        return g;
    }

    private double getCellHeight( final int rowIndex,
                                  final MergableGridData model,
                                  final MergableGridCell cell ) {
        double height = 0;
        for ( int i = rowIndex; i < rowIndex + cell.getMergedCellCount(); i++ ) {
            height = height + model.getRow( i ).getHeight();
        }
        return height;
    }

    @Override
    public GroupingToggle renderGroupedCellToggle( final double containerWidth,
                                                   final double containerHeight,
                                                   final boolean isGrouped ) {
        return new GroupingToggle( containerWidth,
                                   containerHeight,
                                   isGrouped );
    }

}
