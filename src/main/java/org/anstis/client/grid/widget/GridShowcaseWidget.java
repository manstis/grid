package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.extras.slider.client.ui.Slider;

public class GridShowcaseWidget extends Composite {

    public static final int VP_WIDTH = 1200;
    public static final int VP_HEIGHT = 600;

    private static final int GRID1_ROWS = 1000;
    private static final int GRID2_ROWS = 100;
    private static final int GRID3_ROWS = 25;

    public static final int ROW_HEIGHT = 20;

    private static final NumberFormat format = NumberFormat.getFormat( "#.00" );

    interface GridShowcaseWidgetUiBinder extends UiBinder<Widget, GridShowcaseWidget> {

    }

    private static GridShowcaseWidgetUiBinder uiBinder = GWT.create( GridShowcaseWidgetUiBinder.class );

    @UiField
    Label debug;

    @UiField
    SimplePanel table;

    @UiField
    Slider slider;

    private LienzoPanel lienzoPanel;

    public GridShowcaseWidget() {
        initWidget( uiBinder.createAndBindUi( this ) );
        setup();
    }

    private void setup() {
        //Lienzo stuff - Container
        lienzoPanel = new LienzoPanel( VP_WIDTH,
                                       VP_HEIGHT );
        lienzoPanel.getViewport().setPixelSize( VP_WIDTH,
                                                VP_HEIGHT );

        final MousePanMediator mediator1 = new MousePanMediator() {
            @Override
            protected void onMouseMove( final NodeMouseMoveEvent event ) {
                super.onMouseMove( event );
                setDebugTranslation();
            }
        };
        lienzoPanel.getViewport().getMediators().push( mediator1 );

        final Layer layer = new GridLayer();
        lienzoPanel.add( layer );

        final Rectangle bounds = new Rectangle( 200000,
                                                200000,
                                                20 );
        bounds.setX( -100000 );
        bounds.setY( -100000 );
        bounds.setStrokeWidth( 10.0 );
        bounds.setStrokeColor( ColorName.MEDIUMSEAGREEN );
        layer.add( bounds );

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
        final IGridWidget gridWidget1 = new GridWidget2( grid1 );
        layer.add( gridWidget1 );

        table.setWidget( lienzoPanel );
        slider.setValue( 100.0 );
        slider.addValueChangeHandler( new ValueChangeHandler<Double>() {

            private double m_currentZoom = 1.0;

            @Override
            public void onValueChange( final ValueChangeEvent<Double> event ) {
                final double pct = event.getValue();
                final int compare = Double.compare( m_currentZoom,
                                                    pct );
                if ( compare == 0 ) {
                    return;
                }
                m_currentZoom = pct;

                final Transform transform = new Transform();
                final double tx = lienzoPanel.getViewport().getTransform().getTranslateX();
                final double ty = lienzoPanel.getViewport().getTransform().getTranslateY();
                transform.translate( tx, ty );
                transform.scale( m_currentZoom / 100 );

                lienzoPanel.getViewport().setTransform( transform );
                lienzoPanel.getViewport().draw();

                setDebugTranslation();
            }

        } );
    }

    private void setDebugTranslation() {
        final double tx = lienzoPanel.getViewport().getTransform().getTranslateX();
        final double ty = lienzoPanel.getViewport().getTransform().getTranslateY();
        debug.setText( "Translation: (" + format.format( tx ) + ", " + format.format( ty ) + ")" );
    }

}
