package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;

public class GridWidget extends Group {

    public static final int ROW_HEIGHT = 20;
    public static final int HEADER_HEIGHT = 30;

    private boolean isSelected = false;
    private Rectangle selection = new Rectangle( 0, 0 )
            .setStrokeWidth( 2.0 )
            .setStrokeColor( ColorName.GREEN )
            .setListening( false );

    private Grid model;

    private ISelectionManager selectionManager;

    public GridWidget( final Grid model,
                       final ISelectionManager selectionManager ) {
        this.model = model;
        this.selectionManager = selectionManager;
        selection.setWidth( getWidth() );
        selection.setHeight( getHeight() );

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
        return model.getData().size() * ROW_HEIGHT + HEADER_HEIGHT;
    }

    public void select() {
        isSelected = true;
        add( selection );
    }

    public void deselect() {
        isSelected = false;
        remove( selection );
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
        int minRow = (int) ( vpY - getY() - HEADER_HEIGHT ) / ROW_HEIGHT;
        if ( minRow < 0 ) {
            minRow = 0;
        }
        int maxRow = ( (int) ( vpY - getY() - HEADER_HEIGHT + vpHeight + ROW_HEIGHT ) / ROW_HEIGHT );
        if ( maxRow > data.size() ) {
            maxRow = data.size();
        }

        if ( minCol < 0 || maxCol < 0 || maxRow < minRow ) {
            return;
        }

        //Draw header if required
        if ( vpY - getY() < HEADER_HEIGHT && getY() < vpY + vpHeight ) {
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
            selection.setWidth( getWidth() );
            add( selection );
        }

        //Then render to the canvas
        super.drawWithoutTransforms( context,
                                     alpha );
    }

    private void makeGridHeaderWidget( final int startColumnIndex,
                                       final int endColumnIndex ) {
        final double width = getWidth( startColumnIndex,
                                       endColumnIndex );
        final Rectangle r = new Rectangle( width,
                                           HEADER_HEIGHT )
                .setX( model.getColumnOffset( startColumnIndex ) )
                .setFillColor( ColorName.BISQUE )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        add( r );

        final List<GridColumn> columns = model.getColumns();

        //Grid lines
        final MultiPath pl = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false );
        double x = model.getColumnOffset( startColumnIndex );
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            pl.M( x, 0 ).L( x,
                            HEADER_HEIGHT );
            x = x + column.getWidth();
        }
        add( pl );

        //Linked columns
        x = model.getColumnOffset( startColumnIndex );
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
                add( lr );
            }
            x = x + w;
        }

        //Column text
        x = model.getColumnOffset( startColumnIndex );
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
            add( t );
            x = x + w;
        }
    }

    private void makeGridBodyWidget( final int startColumnIndex,
                                     final int endColumnIndex,
                                     final int startRowIndex,
                                     final int endRowIndex ) {
        final int rows = endRowIndex - startRowIndex;
        final double width = getWidth( startColumnIndex,
                                       endColumnIndex );
        final Rectangle r = new Rectangle( width,
                                           ROW_HEIGHT * rows )
                .setX( model.getColumnOffset( startColumnIndex ) )
                .setY( HEADER_HEIGHT + startRowIndex * ROW_HEIGHT )
                .setFillColor( ColorName.ANTIQUEWHITE )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        add( r );

        final List<GridColumn> columns = model.getColumns();
        final List<Map<Integer, String>> data = model.getData();

        //Grid lines
        final MultiPath pl = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false )
                .setY( HEADER_HEIGHT );
        final double minX = model.getColumnOffset( startColumnIndex );
        final double maxX = model.getColumnOffset( endColumnIndex ) + columns.get( endColumnIndex ).getWidth();
        final double minY = startRowIndex * ROW_HEIGHT;
        final double maxY = endRowIndex * ROW_HEIGHT;
        double x = model.getColumnOffset( startColumnIndex );
        for ( int i = startColumnIndex; i <= endColumnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            pl.M( x, minY ).L( x,
                               maxY );
            x = x + column.getWidth();
        }
        for ( int idx = startRowIndex; idx < endRowIndex; idx++ ) {
            pl.M( minX,
                  ROW_HEIGHT * idx ).L( maxX,
                                        ROW_HEIGHT * idx );
        }
        add( pl );

        //Cell content
        final List<Double> columnPositions = new ArrayList<>();
        x = 0;
        for ( GridColumn column : columns ) {
            columnPositions.add( x );
            x = x + column.getWidth();
        }

        for ( int rowIndex = startRowIndex; rowIndex < endRowIndex; rowIndex++ ) {
            final double offsetY = HEADER_HEIGHT + rowIndex * ROW_HEIGHT;
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
                    add( t );
                }
            }
        }
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
            if ( y < 0 || y > HEADER_HEIGHT ) {
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
            if ( y < HEADER_HEIGHT || y > GridWidget.this.getHeight() ) {
                return;
            }
            //Nothing to do at the moment
        }

    }

}
