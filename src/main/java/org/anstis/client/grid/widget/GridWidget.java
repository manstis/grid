package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.NFastArrayList;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public class GridWidget extends Group {

    public static final int ROW_HEIGHT = 20;
    public static final int HEADER_HEIGHT = 30;

    private List<Map<Integer, IPrimitive<?>>> rowsCells = new ArrayList<>();

    private Grid model;
    private ISelectionManager selectionManager;

    private Rectangle selection = new Rectangle( 0, 0 )
            .setStrokeWidth( 2.0 )
            .setStrokeColor( ColorName.GREEN )
            .setListening( false );
    private List<GridColumn> columns = new ArrayList<>();
    private List<Map<Integer, String>> data = new ArrayList<>();
    private double width = 0;
    private double height = 0;

    public GridWidget( final Grid model,
                       final ISelectionManager selectionManager ) {
        setColumns( model.getColumns() );
        setData( model.getData() );

        this.model = model;
        this.selectionManager = selectionManager;
        selection.setWidth( width );
        selection.setHeight( height );

        addNodeMouseClickHandler( new GridWidgetNodeClickHandler() );
    }

    private void setColumns( final List<GridColumn> columns ) {
        this.columns.addAll( columns );
        int x = 0;
        for ( GridColumn column : columns ) {
            x = x + column.getWidth();
        }
        width = x;
        makeGridHeaderWidget();
    }

    private void setData( final List<Map<Integer, String>> data ) {
        this.data.addAll( data );
        height = HEADER_HEIGHT + ROW_HEIGHT * data.size();
        makeGridCellWidget();
    }

    public Grid getModel() {
        return model;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void select() {
        add( selection );
    }

    public void deselect() {
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

        final NFastArrayList<IPrimitive<?>> toRender = new NFastArrayList<>();
        final NFastArrayList<IPrimitive<?>> childNodes = getChildNodes();
        for ( int i = 0; i < childNodes.size(); i++ ) {
            toRender.add( childNodes.get( i ) );
        }

        //Get viewable area dimensions
        final GridLayer gridLayer = ( (GridLayer) getLayer() );
        final Rectangle bounds = gridLayer.getVisibleBounds();
        final double vpX = bounds.getX();
        final double vpY = bounds.getY();
        final double vpHeight = bounds.getHeight();
        final double vpWidth = bounds.getWidth();

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
        int maxRow = ( (int) ( vpHeight + HEADER_HEIGHT ) / ROW_HEIGHT ) + minRow;
        if ( maxRow > rowsCells.size() ) {
            maxRow = rowsCells.size();
        }

        if ( minCol >= 0 && maxCol <= columns.size() && minRow >= 0 && maxRow <= rowsCells.size() ) {
            for ( int i = minRow; i < maxRow; i++ ) {
                final Map<Integer, IPrimitive<?>> rowCells = rowsCells.get( i );
                for ( Map.Entry<Integer, IPrimitive<?>> e : rowCells.entrySet() ) {
                    final int columnIndex = e.getKey();
                    final IPrimitive<?> child = e.getValue();
                    if ( columnIndex >= minCol && columnIndex <= maxCol ) {
                        toRender.add( child );
                    }
                }
            }
        }
        for ( int i = 0; i < toRender.size(); i++ ) {
            toRender.get( i ).drawWithTransforms( context,
                                                  alpha );
        }
    }

    private void makeGridHeaderWidget() {
        final double width = getWidth();
        final Rectangle r = new Rectangle( width,
                                           HEADER_HEIGHT )
                .setFillColor( ColorName.BISQUE )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        add( r );

        //Grid lines
        final MultiPath pl = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false );
        int x = 0;
        for ( GridColumn column : columns ) {
            final int w = column.getWidth();
            pl.M( x, 0 ).L( x,
                            HEADER_HEIGHT );
            x = x + w;
        }
        add( pl );

        //Linked columns
        x = 0;
        for ( GridColumn column : columns ) {
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
        x = 0;
        for ( GridColumn column : columns ) {
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

    private void makeGridCellWidget() {
        final int rows = data.size();
        final double width = getWidth();
        final Rectangle r = new Rectangle( width,
                                           ROW_HEIGHT * rows )
                .setFillColor( ColorName.ANTIQUEWHITE )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setY( HEADER_HEIGHT );
        add( r );

        //Grid lines
        final MultiPath pl = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false )
                .setY( HEADER_HEIGHT );
        double x = 0;
        for ( GridColumn column : columns ) {
            pl.M( x, 0 ).L( x,
                            ROW_HEIGHT * rows );
            x = x + column.getWidth();
        }
        for ( int idx = 0; idx < rows; idx++ ) {
            pl.M( 0,
                  ROW_HEIGHT * idx ).L( width,
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

        double offsetY = HEADER_HEIGHT;
        for ( Map<Integer, String> row : data ) {
            final Map<Integer, IPrimitive<?>> rowCells = new HashMap<>();
            for ( Map.Entry<Integer, String> e : row.entrySet() ) {
                final int columnIndex = e.getKey();
                final int columnWidth = columns.get( columnIndex ).getWidth();
                final double offsetX = columnPositions.get( columnIndex );
                final Rectangle cr = new Rectangle( columnWidth,
                                                    ROW_HEIGHT )
                        .setLocation( new Point2D( offsetX,
                                                   offsetY ) )
                        .setFillColor( ColorName.THISTLE );
                rowCells.put( e.getKey(),
                              cr );
            }
            offsetY = offsetY + ROW_HEIGHT;
            rowsCells.add( rowCells );
        }
    }

    private class GridWidgetNodeClickHandler implements NodeMouseClickHandler {

        @Override
        public void onNodeMouseClick( final NodeMouseClickEvent event ) {
            selectionManager.select( GridWidget.this.model );
            handleHeaderCellClick( event );
            handleBodyCellClick( event );
        }

        private double getX( final NodeMouseClickEvent event ) {
            final Transform t = GridWidget.this.getViewport().getTransform().copy().getInverse();
            final Point2D p = new Point2D( event.getX(),
                                           event.getY() );
            t.transform( p,
                         p );
            return p.add( GridWidget.this.getLocation().mul( -1.0 ) ).getX();
        }

        private double getY( final NodeMouseClickEvent event ) {
            final Transform t = GridWidget.this.getViewport().getTransform().copy().getInverse();
            final Point2D p = new Point2D( event.getX(),
                                           event.getY() );
            t.transform( p,
                         p );
            return p.add( GridWidget.this.getLocation().mul( -1.0 ) ).getY();
        }

        private void handleHeaderCellClick( final NodeMouseClickEvent event ) {
            final double x = getX( event );
            final double y = getY( event );
            if ( x < 0 || x > GridWidget.this.getWidth() ) {
                return;
            }
            if ( y < 0 || y > HEADER_HEIGHT ) {
                return;
            }

            //Refactor to utility method to get a GridColumn
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
            final double x = getX( event );
            final double y = getY( event );
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
