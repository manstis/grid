package org.anstis.client.grid.widget;

import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridDataFactory;
import org.gwtbootstrap3.extras.slider.client.ui.Slider;

public class GridShowcaseWidget extends Composite implements ISelectionManager {

    public static final int VP_WIDTH = 1200;
    public static final int VP_HEIGHT = 600;

    private static final int GRID1_ROWS = 100;
    private static final int GRID2_ROWS = 100;
    private static final int GRID3_ROWS = 100;

    interface GridShowcaseWidgetUiBinder extends UiBinder<Widget, GridShowcaseWidget> {

    }

    private static GridShowcaseWidgetUiBinder uiBinder = GWT.create( GridShowcaseWidgetUiBinder.class );

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
        final MousePanMediator mediator1 = new MousePanMediator();
        gridPanel.getViewport().getMediators().push( mediator1 );

        //Wire-up widgets
        gridPanel.add( gridLayer );
        table.setWidget( gridPanel );

        //Grid 1
        final Grid grid1 = new Grid();
        for ( int idx = 0; idx < 10; idx++ ) {
            final GridColumn column = new GridColumn( "G1-Col: " + idx,
                                                      100 );
            grid1.getColumns().add( column );
        }
        grid1.getData().addAll( GridDataFactory.makeData( grid1.getColumns().size(),
                                                          GRID1_ROWS ) );

        //Grid 2
        final Grid grid2 = new Grid();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "G2-Col: " + idx,
                                                      150 );
            grid2.getColumns().add( column );
        }
        grid2.getData().addAll( GridDataFactory.makeData( grid2.getColumns().size(),
                                                          GRID2_ROWS ) );

        //Grid 3
        final Grid grid3 = new Grid();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "G3-Col: " + idx,
                                                      200 );
            grid3.getColumns().add( column );
        }
        grid3.getData().addAll( GridDataFactory.makeData( grid3.getColumns().size(),
                                                          GRID3_ROWS ) );

        //Link grids
        grid1.getColumns().get( 9 ).setLink( grid2.getColumns().get( 0 ) );
        grid2.getColumns().get( 3 ).setLink( grid3.getColumns().get( 0 ) );
        grid3.getColumns().get( 0 ).setLink( grid1.getColumns().get( 0 ) );

        //Widgets
        addGrid( grid1,
                 gridLayer,
                 new Point2D( -1300,
                              0 ) );
        addGrid( grid2,
                 gridLayer,
                 new Point2D( 0,
                              750 ) );
        addGrid( grid3,
                 gridLayer,
                 new Point2D( 1050,
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
            }

        } );
    }

    public void addGrid( final Grid grid,
                         final Layer layer,
                         final Point2D location ) {
        final GridWidget gridWidget = new GridWidget( grid,
                                                      this );
        gridWidget.setLocation( location );
        layer.add( gridWidget );
    }

    @Override
    public void select( final Grid selectable ) {
        gridLayer.select( selectable );
    }

    @Override
    public void scrollIntoView( final GridColumn link ) {
        gridLayer.scrollIntoView( link );
    }

}
