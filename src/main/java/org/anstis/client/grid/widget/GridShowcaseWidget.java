package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
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

public class GridShowcaseWidget extends Composite implements ISelectionManager {

    public static final int VP_WIDTH = 1200;
    public static final int VP_HEIGHT = 600;

    private static final int GRID1_ROWS = 1000;
    private static final int GRID2_ROWS = 100;
    private static final int GRID3_ROWS = 25;

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

    private GridLayer gridLayer = new GridLayer();
    private LienzoPanel gridPanel = new LienzoPanel( VP_WIDTH,
                                                     VP_HEIGHT );

    public GridShowcaseWidget() {
        initWidget( uiBinder.createAndBindUi( this ) );
        setup();
    }

    private void setup() {
        //Lienzo stuff - Set default scale to 50%
        final Transform transform = new Transform().scale( 0.5 );
        gridPanel.getViewport().setTransform( transform );

        //Lienzo stuff - Add mouse pan support
        final MousePanMediator mediator1 = new MousePanMediator() {
            @Override
            protected void onMouseMove( final NodeMouseMoveEvent event ) {
                super.onMouseMove( event );
                setDebugTranslation();
            }
        };
        gridPanel.getViewport().getMediators().push( mediator1 );

        //Wire-up widgets
        gridPanel.add( gridLayer );
        table.setWidget( gridPanel );

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
        addGrid( grid1,
                 gridLayer,
                 new Point2D( -1100,
                              0 ) );
        addGrid( grid2,
                 gridLayer,
                 new Point2D( 0,
                              0 ) );
        addGrid( grid3,
                 gridLayer,
                 new Point2D( 850,
                              0 ) );

        //Slider
        slider.setValue( 50.0 );
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
                final double tx = gridPanel.getViewport().getTransform().getTranslateX();
                final double ty = gridPanel.getViewport().getTransform().getTranslateY();
                transform.translate( tx, ty );
                transform.scale( m_currentZoom / 100 );

                gridPanel.getViewport().setTransform( transform );
                gridPanel.getViewport().draw();

                setDebugTranslation();
            }

        } );
    }

    public void addGrid( final Grid grid,
                         final Layer layer,
                         final Point2D location ) {
        final GridWidget2 gridWidget = new GridWidget2( grid,
                                                        this );
        gridWidget.setLocation( location );
        layer.add( gridWidget );
    }

    @Override
    public void select( final ISelectable selectable ) {
        gridLayer.select( selectable );
    }

    private void setDebugTranslation() {
        final double tx = gridPanel.getViewport().getTransform().getTranslateX();
        final double ty = gridPanel.getViewport().getTransform().getTranslateY();
        debug.setText( "Translation: (" + format.format( tx ) + ", " + format.format( ty ) + ")" );
    }

}
