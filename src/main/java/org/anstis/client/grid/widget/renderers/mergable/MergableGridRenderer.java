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

        //Grid lines - Verticals - easy!
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

        //Grid lines - Horizontals - not so easy!
        for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
            x = 0;
            final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
            final MergableGridRow row = model.getRow( rowIndex );

            if ( !row.isMerged() ) {
                //If row doesn't contain merged cells just draw a line across the visible body
                bodyGrid.M( x,
                            y ).L( maxX,
                                   y );

            } else if ( !row.isCollapsed() ) {
                //We need to break the line into sections for the different cells
                for ( int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++ ) {
                    final MergableGridColumn column = columns.get( columnIndex );
                    final MergableGridCell cell = model.getCell( rowIndex,
                                                                 columnIndex );

                    if ( cell == null || cell.getMergedCellCount() > 0 ) {
                        //Draw a line-segment for empty cells and cells that are to have content rendered
                        bodyGrid.M( x,
                                    y ).L( x + column.getWidth(),
                                           y );

                    } else if ( isCollapsedRowMultiValue( model,
                                                          cell,
                                                          rowIndex,
                                                          columnIndex ) ) {
                        //Special case for when a cell follows collapsed row(s) with multiple values
                        bodyGrid.M( x,
                                    y ).L( x + column.getWidth(),
                                           y );
                    }
                    x = x + column.getWidth();
                }
            }
        }
        g.add( bodyGrid );

        //Cell content
        x = 0;
        for ( int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++ ) {
            final MergableGridColumn column = columns.get( columnIndex );
            final int w = column.getWidth();
            for ( int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++ ) {
                final double y = rowOffsets.get( rowIndex - startRowIndex ) - rowOffsets.get( 0 );
                final MergableGridRow row = model.getRow( rowIndex );
                final MergableGridCell cell = model.getCell( rowIndex,
                                                             columnIndex );

                //Only show content for rows that are not collapsed
                if ( !row.isCollapsed() ) {

                    //Only show content if there's a Cell behind it!
                    if ( cell != null ) {
                        final Group hc = column.renderRow( row );
                        hc.setX( x + w / 2 ).setListening( false );

                        if ( cell.getMergedCellCount() > 0 ) {
                            //If cell is "lead" i.e. top of a merged block centralize content in cell
                            hc.setY( y + getCellHeight( rowIndex,
                                                        model,
                                                        cell ) / 2 );
                            g.add( hc );

                            //Skip remainder of merged block
                            rowIndex = rowIndex + cell.getMergedCellCount() - 1;

                        } else {
                            //Otherwise the cell has been clipped and we need to back-track to the "lead" cell to centralize content
                            double _y = y;
                            int _rowIndex = rowIndex;
                            MergableGridCell _cell = cell;
                            while ( _cell.getMergedCellCount() == 0 ) {
                                _rowIndex--;
                                _y = _y - model.getRow( _rowIndex ).getHeight();
                                _cell = model.getCell( _rowIndex,
                                                       columnIndex );
                            }
                            hc.setY( _y + getCellHeight( _rowIndex,
                                                         model,
                                                         _cell ) / 2 );
                            g.add( hc );

                            //Skip remainder of merged block
                            rowIndex = _rowIndex + _cell.getMergedCellCount() - 1;
                        }

                        //Add Group Toggle for first row in a Merged block
                        if ( cell.getMergedCellCount() > 1 ) {
                            final GroupingToggle gt = renderGroupedCellToggle( w,
                                                                               row.getHeight(),
                                                                               cell.isCollapsed() );
                            gt.setX( x ).setY( y );
                            g.add( gt );
                        }
                    }
                }
            }
            x = x + w;
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

    private boolean isCollapsedRowMultiValue( final MergableGridData model,
                                              final MergableGridCell cell,
                                              final int rowIndex,
                                              final int columnIndex ) {
        MergableGridRow row;
        int rowOffset = 1;
        //Iterate collapsed rows checking if the values differ
        while ( ( row = model.getRow( rowIndex - rowOffset ) ).isCollapsed() ) {
            final MergableGridCell nc = row.getCells().get( columnIndex );
            if ( nc == null ) {
                return true;
            }
            if ( !cell.getValue().equals( nc.getValue() ) ) {
                return true;
            }
            rowOffset++;
        }
        //Check "lead" row as well - since this is not marked as collapsed
        final MergableGridCell nc = row.getCells().get( columnIndex );
        if ( nc == null ) {
            return true;
        }
        if ( !cell.getValue().equals( nc.getValue() ) ) {
            return true;
        }
        return false;
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
