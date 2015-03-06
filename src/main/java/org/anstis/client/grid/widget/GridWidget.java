package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public class GridWidget extends com.ait.lienzo.client.core.shape.grid.Grid {

    public static final int ROW_HEIGHT = 20;
    public static final int HEADER_HEIGHT = 30;

    private Grid model;
    private ISelectionManager selectionManager;

    private Rectangle selection = new Rectangle( 0, 0 )
            .setStrokeWidth( 2.0 )
            .setStrokeColor( ColorName.GREEN )
            .setListening( false );
    private List<GridColumn> columns = new ArrayList<>();
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
        final Group h = makeHeader( columns );
        h.setLocation( new Point2D( 0, 0 ) );
        getProxy().add( h );

    }

    private void setData( final List<Map<Integer, String>> data ) {
        height = HEADER_HEIGHT + ROW_HEIGHT * data.size();
        final Group r = makeCell( columns,
                                  data );
        r.setLocation( new Point2D( 0,
                                    HEADER_HEIGHT ) );
        getProxy().add( r );
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
        getProxy().add( selection );
    }

    public void deselect() {
        getProxy().remove( selection );
    }

    private Group makeHeader( final List<GridColumn> columns ) {
        final GridHeaderWidget r = new GridHeaderWidget( this,
                                                         columns );
        return r;
    }

    private Group makeCell( final List<GridColumn> columns,
                            final List<Map<Integer, String>> data ) {
        final GridCellWidget r = new GridCellWidget( this,
                                                     columns,
                                                     data );
        return r;
    }

    private static class GridHeaderWidget extends Group {

        private Rectangle r;

        public GridHeaderWidget( final GridWidget gridWidget,
                                 final List<GridColumn> columns ) {
            final double width = gridWidget.getWidth();
            r = new Rectangle( width,
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

    }

    private static class GridCellWidget extends Group {

        private Rectangle r;

        public GridCellWidget( final GridWidget gridWidget,
                               final List<GridColumn> columns,
                               final List<Map<Integer, String>> data ) {
            final int rows = data.size();
            final double width = gridWidget.getWidth();
            r = new Rectangle( width,
                               ROW_HEIGHT * rows )
                    .setFillColor( ColorName.ANTIQUEWHITE )
                    .setStrokeColor( ColorName.SLATEGRAY )
                    .setStrokeWidth( 0.5 );
            add( r );

            //Show last row in a different colour so we can check scrolling
            final Rectangle endRowMarker = new Rectangle( width,
                                                          ROW_HEIGHT )
                    .setFillColor( ColorName.RED )
                    .setY( ( rows - 1 ) * ROW_HEIGHT );
            add( endRowMarker );

            //Grid lines
            final MultiPath pl = new MultiPath()
                    .setStrokeColor( ColorName.SLATEGRAY )
                    .setStrokeWidth( 0.5 )
                    .setListening( false );
            double x = 0;
            for ( GridColumn column : columns ) {
                pl.M( x, 0 ).L( x,
                                ROW_HEIGHT * rows );
                x = x + column.getWidth();
            }
            for ( int idx = 0; idx < rows; idx++ ) {
                pl.M( 0,
                      ROW_HEIGHT * idx ).L( gridWidget.getWidth(),
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

            double cellY = 0;
            for ( Map<Integer, String> row : data ) {
                for ( Map.Entry<Integer, String> e : row.entrySet() ) {
                    final int columnIndex = e.getKey();
                    final int columnWidth = columns.get( columnIndex ).getWidth();
                    final double cellX = columnPositions.get( columnIndex );
                    final Rectangle r = new Rectangle( columnWidth,
                                                       ROW_HEIGHT )
                            .setLocation( new Point2D( cellX,
                                                       cellY ) )
                            .setFillColor( ColorName.THISTLE );
                    add( r );
                }
                cellY = cellY + ROW_HEIGHT;
            }
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
