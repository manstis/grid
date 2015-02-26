package org.anstis.client;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.GridWidget;

public class GridShowcase implements EntryPoint {

    public static final int LP_WIDTH = 2000;
    public static final int LP_HEIGHT = 2000;

    public static final int SP_WIDTH = 1200;
    public static final int SP_HEIGHT = 800;

    private static final int GRID1_ROWS = 200;
    private static final int GRID2_ROWS = 200;
    private static final int GRID3_ROWS = 10;

    public void onModuleLoad() {
        //Lienzo stuff - Container
        final LienzoPanel p = new LienzoPanel( LP_WIDTH,
                                               LP_HEIGHT );
        final Layer layer = new GridLayer();
        p.add( layer );

        //Model
        final Grid grid1 = new Grid();
        final Grid grid2 = new Grid();
        final Grid grid3 = new Grid();

        //Grid 1
        final List<GridColumn> columnsGrid1 = new ArrayList<>();
        for ( int idx = 0; idx < 10; idx++ ) {
            final GridColumn column = new GridColumn( "Col: " + idx,
                                                      100 );
            columnsGrid1.add( column );
            if ( idx == 9 ) {
                column.setLink( grid2 );
            }
        }

        //Grid 2
        final List<GridColumn> columnsGrid2 = new ArrayList<>();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "Col: " + idx,
                                                      150 );
            columnsGrid2.add( column );
            if ( idx == 3 ) {
                column.setLink( grid3 );
            }
        }

        //Grid 3
        final List<GridColumn> columnsGrid3 = new ArrayList<>();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "Col: " + idx,
                                                      200 );
            columnsGrid3.add( column );
            if ( idx == 0 ) {
                column.setLink( grid1 );
            }
        }

        grid1.getColumns().addAll( columnsGrid1 );
        grid2.getColumns().addAll( columnsGrid2 );
        grid3.getColumns().addAll( columnsGrid3 );
        grid1.setData( GRID1_ROWS );
        grid2.setData( GRID2_ROWS );
        grid3.setData( GRID3_ROWS );

        //Widgets
        final GridWidget gridWidget1 = new GridWidget( grid1 );
        layer.add( gridWidget1 );

        final ScrollPanel sp = new ScrollPanel();
        sp.setSize( SP_WIDTH + "px",
                    SP_HEIGHT + "px" );
        sp.add( p );

        RootPanel.get().add( sp );
    }

}
