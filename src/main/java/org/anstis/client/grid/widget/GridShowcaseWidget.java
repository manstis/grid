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
package org.anstis.client.grid.widget;

import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.model.IGrid;
import org.anstis.client.grid.model.mergable.MergableGrid;
import org.anstis.client.grid.util.GridDataFactory;
import org.anstis.client.grid.widget.edit.EditorPopup;
import org.anstis.client.grid.widget.renderers.GridRendererRegistry;
import org.anstis.client.grid.widget.renderers.IGridRenderer;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.extras.slider.client.ui.Slider;

public class GridShowcaseWidget extends Composite implements IEditManager,
                                                             ISelectionManager {

    public static final int VP_WIDTH = 1200;
    public static final int VP_HEIGHT = 600;

    private static final double VP_SCALE = 1.0;

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

    @UiField
    ListBox rendererSelector;

    private final EditorPopup editor = new EditorPopup();

    private GridLayer gridLayer = new GridLayer();
    private LienzoPanel gridPanel = new LienzoPanel( VP_WIDTH,
                                                     VP_HEIGHT );

    public GridShowcaseWidget() {
        initWidget( uiBinder.createAndBindUi( this ) );
        setup();
    }

    private void setup() {
        //Lienzo stuff - Set default scale
        final Transform transform = new Transform().scale( VP_SCALE );
        gridPanel.getViewport().setTransform( transform );

        //Lienzo stuff - Add mouse pan support
        final MousePanMediator mediator1 = new MousePanMediator();
        gridPanel.getViewport().getMediators().push( mediator1 );

        //Wire-up widgets
        gridPanel.add( gridLayer );
        table.setWidget( gridPanel );

        //Grid 1
        final MergableGrid grid1 = new MergableGrid();
        for ( int idx = 0; idx < 10; idx++ ) {
            final GridColumn column = new GridColumn( "G1-Col: " + idx,
                                                      100 );
            grid1.addColumn( column );
        }
        grid1.setData( GridDataFactory.makeData( grid1.getColumns().size(),
                                                 GRID1_ROWS ) );

        //Grid 2
        final MergableGrid grid2 = new MergableGrid();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "G2-Col: " + idx,
                                                      150 );
            grid2.addColumn( column );
        }
        grid2.setData( GridDataFactory.makeData( grid2.getColumns().size(),
                                                 GRID2_ROWS ) );

        //Grid 3
        final MergableGrid grid3 = new MergableGrid();
        for ( int idx = 0; idx < 5; idx++ ) {
            final GridColumn column = new GridColumn( "G3-Col: " + idx,
                                                      200 );
            grid3.addColumn( column );
        }
        grid3.setData( GridDataFactory.makeData( grid3.getColumns().size(),
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
        slider.setValue( VP_SCALE * 100 );
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

        //Style selector
        for ( IGridRenderer renderer : GridRendererRegistry.getRenderers() ) {
            rendererSelector.addItem( renderer.getName() );
            if ( renderer.equals( GridRendererRegistry.getActiveRenderer() ) ) {
                rendererSelector.setSelectedIndex( rendererSelector.getItemCount() - 1 );
            }
        }
        rendererSelector.addChangeHandler( new ChangeHandler() {
            @Override
            public void onChange( final ChangeEvent event ) {
                GridRendererRegistry.setActiveStyleName( rendererSelector.getItemText( rendererSelector.getSelectedIndex() ) );
                gridLayer.draw();
            }
        } );
    }

    public void addGrid( final IGrid<?> grid,
                         final Layer layer,
                         final Point2D location ) {
        final GridWidget gridWidget = new GridWidget( grid,
                                                      this,
                                                      this );
        gridWidget.setLocation( location );
        layer.add( gridWidget );
    }

    @Override
    public void edit( final String value,
                      final Callback<String, String> callback ) {
        editor.edit( value,
                     callback );
    }

    @Override
    public void select( final IGrid<?> selectable ) {
        gridLayer.select( selectable );
    }

    @Override
    public void scrollIntoView( final GridColumn link ) {
        gridLayer.scrollIntoView( link );
    }

}
