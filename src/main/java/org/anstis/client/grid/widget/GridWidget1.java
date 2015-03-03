package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.transition.GridSwapperViewPortTransformation;
import org.anstis.client.grid.transition.IGridSwapper;

public class GridWidget1 extends Group implements IGridWidget<Group> {

    private Rectangle selection = new Rectangle( 0, 0 )
            .setStrokeWidth( 2.0 )
            .setStrokeColor( ColorName.RED )
            .setListening( false );
    private List<GridColumn> columns = new ArrayList<>();
    private double width = 0;
    private double height = 0;

    public GridWidget1( final Grid model ) {
        setColumns( model.getColumns() );
        setData( model.getData() );
        selection.setWidth( width );
        selection.setHeight( height );
    }

    private void setColumns( final List<GridColumn> columns ) {
        this.columns.addAll( columns );
        int x = 0;
        for ( GridColumn column : columns ) {
            final Group h = makeHeader( column );
            h.setLocation( new Point2D( x,
                                        0 ) );
            add( h );
            x = x + column.getWidth();
        }
        width = x;
    }

    private void setData( final int rows ) {
        int x = 0;
        for ( GridColumn column : columns ) {
            int y = GridShowcaseWidget.ROW_HEIGHT;
            for ( int row = 0; row < rows; row++ ) {
                final Group r = makeCell( column );
                r.setLocation( new Point2D( x,
                                            y ) );
                add( r );
                y = y + GridShowcaseWidget.ROW_HEIGHT;
                height = y;
            }
            x = x + column.getWidth();
        }
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void select() {
        add( selection );
    }

    @Override
    public void deselect() {
        remove( selection );
    }

    private Group makeHeader( final GridColumn column ) {
        final GridHeaderWidget r = new GridHeaderWidget( this,
                                                         column );
        return r;
    }

    private Group makeCell( final GridColumn column ) {
        final GridCellWidget r = new GridCellWidget( column );
        r.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( final NodeMouseClickEvent event ) {
                if ( r.isSelected() ) {
                    r.deselect();
                } else {
                    r.select();
                }
            }
        } );
        return r;
    }

    private static class GridHeaderWidget extends Group {

        private Rectangle r;
        private Text t;

        public GridHeaderWidget( final IGridWidget gridWidget,
                                 final GridColumn column ) {
            final double width = column.getWidth();
            final String title = column.getTitle();

            r = new Rectangle( width,
                               GridShowcaseWidget.ROW_HEIGHT );
            r.setFillColor( column.isLinked() ? ColorName.BROWN : ColorName.BISQUE );
            r.setStrokeColor( ColorName.SLATEGRAY );
            r.setStrokeWidth( 0.5 );
            add( r );

            t = new Text( title ).setFillColor( ColorName.DEEPPINK ).setX( width / 2 ).setY( GridShowcaseWidget.ROW_HEIGHT / 2 ).setFontSize( 12 );
            t.setTextBaseLine( TextBaseLine.MIDDLE );
            t.setTextAlign( TextAlign.CENTER );
            add( t );

            if ( column.isLinked() ) {
                addNodeMouseClickHandler( new NodeMouseClickHandler() {
                    @Override
                    public void onNodeMouseClick( final NodeMouseClickEvent event ) {
                        final Grid link = column.getLink();
                        final IGridWidget linkWidget = new GridWidget1( link );
                        //final IGridSwapper swapper = new GridSwapperGroupScale( gridWidget.getLayer() );
                        final IGridSwapper swapper = new GridSwapperViewPortTransformation( GridShowcaseWidget.VP_WIDTH,
                                                                                            GridShowcaseWidget.VP_HEIGHT,
                                                                                            gridWidget.getLayer() );
                        swapper.swap( gridWidget,
                                      linkWidget );
                    }
                } );
            }
        }

    }

    private static class GridCellWidget extends Group {

        private Rectangle r;

        private boolean isSelected = false;

        public GridCellWidget( final GridColumn column ) {
            final double width = column.getWidth();
            final String title = column.getTitle();

            r = new Rectangle( width,
                               GridShowcaseWidget.ROW_HEIGHT );
            r.setFillColor( ColorName.ANTIQUEWHITE );
            r.setStrokeColor( ColorName.SLATEGRAY );
            r.setStrokeWidth( 0.5 );
            add( r );
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
