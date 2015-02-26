package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;

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
import org.anstis.client.GridShowcase;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.transition.GridSwapperViewPortTransformation;
import org.anstis.client.grid.transition.IGridSwapper;

public class GridWidget2 extends Group implements IGridWidget<Group> {

    private static final int ROW_HEIGHT = 20;
    private List<GridColumn> columns = new ArrayList<>();
    private double width = 0;
    private double height = 0;

    public GridWidget2( final Grid model ) {
        setColumns( model.getColumns() );
        setData( model.getData() );
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
        add( h );

    }

    private void setData( final int rows ) {
        height = ROW_HEIGHT * rows;
        final Group r = makeCell( columns,
                                  rows );
        r.setLocation( new Point2D( 0, ROW_HEIGHT ) );
        add( r );
    }

    @Override
    public double getWidth() {
        return width;
    }

    private Group makeHeader( final List<GridColumn> columns ) {
        final GridHeaderWidget r = new GridHeaderWidget( this,
                                                         columns );
        return r;
    }

    private Group makeCell( final List<GridColumn> columns,
                            final int rows ) {
        final GridCellWidget r = new GridCellWidget( this,
                                                     columns,
                                                     rows );
        return r;
    }

    private static class GridHeaderWidget extends Group {

        private Rectangle r;
        private Text t;

        public GridHeaderWidget( final IGridWidget gridWidget,
                                 final List<GridColumn> columns ) {
            final double width = gridWidget.getWidth();
            r = new Rectangle( width,
                               ROW_HEIGHT );
            r.setStrokeColor( ColorName.SLATEGRAY );
            r.setStrokeWidth( 0.5 );

            //TODO {manstis} Draw column text
            r.setFillColor( ColorName.BISQUE );
            r.setStrokeColor( ColorName.SLATEGRAY );
            r.setStrokeWidth( 0.5 );
            add( r );

            //Grid lines
            final MultiPath pl = new MultiPath();
            pl.setStrokeColor( ColorName.SLATEGRAY );
            pl.setStrokeWidth( 0.5 );
            int x = 0;
            for ( GridColumn column : columns ) {
                final int w = column.getWidth();
                pl.M( x, 0 ).L( x, ROW_HEIGHT );
                x = x + w;
            }
            add( pl );

            //Linked columns
            x = 0;
            for ( GridColumn column : columns ) {
                final int w = column.getWidth();
                if ( column.isLinked() ) {
                    final Rectangle lr = new Rectangle( w,
                                                        ROW_HEIGHT );
                    lr.setFillColor( ColorName.BROWN );
                    lr.setStrokeColor( ColorName.SLATEGRAY );
                    lr.setStrokeWidth( 0.5 );
                    lr.setX( x );
                    add( lr );

                    final Grid link = column.getLink();
                    lr.addNodeMouseClickHandler( new NodeMouseClickHandler() {
                        @Override
                        public void onNodeMouseClick( final NodeMouseClickEvent event ) {
                            final GridWidget2 linkWidget = new GridWidget2( link );
                            //final IGridSwapper swapper = new GridSwapperGroupScale( gridWidget.getLayer() );
                            final IGridSwapper swapper = new GridSwapperViewPortTransformation( GridShowcase.LP_WIDTH,
                                                                                                GridShowcase.LP_HEIGHT,
                                                                                                gridWidget.getLayer() );
                            swapper.swap( gridWidget,
                                          linkWidget );
                        }
                    } );
                }
                x = x + w;
            }

            //Column text
            x = 0;
            for ( GridColumn column : columns ) {
                final int w = column.getWidth();
                final Text t = new Text( column.getTitle() ).setFillColor( ColorName.DEEPPINK ).setX( x + w / 2 ).setY( ROW_HEIGHT / 2 ).setFontSize( 12 );
                t.setTextBaseLine( TextBaseLine.MIDDLE );
                t.setTextAlign( TextAlign.CENTER );
                add( t );
                x = x + w;
            }
        }

    }

    private static class GridCellWidget extends Group {

        private Rectangle r;

        private boolean isSelected = false;

        public GridCellWidget( final IGridWidget gridWidget,
                               final List<GridColumn> columns,
                               final int rows ) {
            final double width = gridWidget.getWidth();
            r = new Rectangle( width,
                               ROW_HEIGHT * rows );
            r.setFillColor( ColorName.ANTIQUEWHITE );
            r.setStrokeColor( ColorName.SLATEGRAY );
            r.setStrokeWidth( 0.5 );
            add( r );

            //Grid lines
            final MultiPath pl = new MultiPath();
            pl.setStrokeColor( ColorName.SLATEGRAY );
            pl.setStrokeWidth( 0.5 );
            int x = 0;
            for ( GridColumn column : columns ) {
                pl.M( x, 0 ).L( x, ROW_HEIGHT * rows );
                x = x + column.getWidth();
            }
            for ( int idx = 0; idx < rows; idx++ ) {
                pl.M( 0, ROW_HEIGHT * idx ).L( gridWidget.getWidth(), ROW_HEIGHT * idx );
            }
            add( pl );

//            r.addNodeMouseClickHandler( new NodeMouseClickHandler() {
//                @Override
//                public void onNodeMouseClick( final NodeMouseClickEvent event ) {
//                    if ( r.isSelected() ) {
//                        r.deselect();
//                    } else {
//                        r.select();
//                    }
//                }
//            } );
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void select() {
            r.setFillColor( ColorName.CHARTREUSE );
            getLayer().draw();
            isSelected = true;
        }

        public void deselect() {
            r.setFillColor( ColorName.ANTIQUEWHITE );
            getLayer().draw();
            isSelected = false;
        }

    }

}
