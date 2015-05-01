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

import java.util.HashMap;
import java.util.Map;

import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.BaseGridCellValue;
import org.anstis.client.grid.model.IGridCellValue;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.model.IGridData;
import org.anstis.client.grid.model.basic.GridCell;
import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.GridData;
import org.anstis.client.grid.model.basic.GridRow;
import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.model.mergable.MergableGridColumn;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.util.GridDataFactory;
import org.anstis.client.grid.widget.basic.GridWidget;
import org.anstis.client.grid.widget.context.GridCellRenderContext;
import org.anstis.client.grid.widget.dom.CheckBoxDOMContainerFactory;
import org.anstis.client.grid.widget.dom.GridCellDOMContainer;
import org.anstis.client.grid.widget.edit.EditorPopup;
import org.anstis.client.grid.widget.mergable.MergableGridWidget;
import org.anstis.client.grid.widget.renderers.IGridRenderer;
import org.anstis.client.grid.widget.renderers.basic.BlueGridRenderer;
import org.anstis.client.grid.widget.renderers.basic.GreenGridRenderer;
import org.anstis.client.grid.widget.renderers.basic.RedGridRenderer;
import org.anstis.client.grid.widget.renderers.mergable.MergableGridRenderer;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.extras.slider.client.ui.Slider;

public class GridShowcaseWidget extends Composite implements ISelectionManager {

    public static final int VP_WIDTH = 1200;
    public static final int VP_HEIGHT = 600;

    private static final double VP_SCALE = 1.0;

    private static final int GRID1_ROWS = 2000;
    private static final int GRID2_ROWS = 2000;
    private static final int GRID3_ROWS = 2000;
    private static final int GRID4_ROWS = 2000;

    interface GridShowcaseWidgetUiBinder extends UiBinder<Widget, GridShowcaseWidget> {

    }

    private static GridShowcaseWidgetUiBinder uiBinder = GWT.create( GridShowcaseWidgetUiBinder.class );

    @UiField
    AbsolutePanel table;

    @UiField
    Slider slider;

    @UiField
    ListBox basicRendererSelector;

    @UiField
    CheckBox chkShowMerged;

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
        table.add( gridPanel );

        //Grid 1
        final MergableGridData grid1 = new MergableGridData();
        for ( int idx = 0; idx < 10; idx++ ) {
            final MergableGridColumn<String> column = new MergableGridColumn<String>( "G1-Col: " + idx,
                                                                                      100 ) {
                @Override
                public void renderCell( final Group g,
                                        final MergableGridCell<String> cell,
                                        final GridCellRenderContext context ) {
                    final Text t = new Text( cell.getValue().getValue() )
                            .setFillColor( ColorName.GREY )
                            .setFontSize( 12 )
                            .setFontFamily( "serif" )
                            .setListening( false )
                            .setTextBaseLine( TextBaseLine.MIDDLE )
                            .setTextAlign( TextAlign.CENTER );
                    g.add( t );
                }

                @Override
                public void edit( final IGridCellValue<String> value,
                                  final Callback<IGridCellValue<String>, IGridCellValue<String>> callback ) {
                    editor.edit( value,
                                 callback );
                }

            };
            grid1.addColumn( column );
        }
        GridDataFactory.populate( grid1,
                                  GRID1_ROWS );

        //Grid 2
        final MergableGridData grid2 = new MergableGridData();
        for ( int idx = 0; idx < 5; idx++ ) {
            final MergableGridColumn<String> column = new MergableGridColumn<String>( "G2-Col: " + idx,
                                                                                      150 ) {
                @Override
                public void renderCell( final Group g,
                                        final MergableGridCell<String> cell,
                                        final GridCellRenderContext context ) {
                    final Text t = new Text( cell.getValue().getValue() )
                            .setFillColor( ColorName.GREY )
                            .setFontSize( 12 )
                            .setFontFamily( "serif" )
                            .setListening( false )
                            .setTextBaseLine( TextBaseLine.MIDDLE )
                            .setTextAlign( TextAlign.CENTER );
                    g.add( t );
                }

                @Override
                public void edit( final IGridCellValue<String> value,
                                  final Callback<IGridCellValue<String>, IGridCellValue<String>> callback ) {
                    editor.edit( value,
                                 callback );
                }

            };
            grid2.addColumn( column );
        }
        GridDataFactory.populate( grid2,
                                  GRID2_ROWS );

        //Grid 3
        final MergableGridData grid3 = new MergableGridData();
        for ( int idx = 0; idx < 5; idx++ ) {
            final MergableGridColumn<String> column = new MergableGridColumn<String>( "G3-Col: " + idx,
                                                                                      100 ) {
                @Override
                public void renderCell( final Group g,
                                        final MergableGridCell<String> cell,
                                        final GridCellRenderContext context ) {
                    final Text t = new Text( cell.getValue().getValue() )
                            .setFillColor( ColorName.GREY )
                            .setFontSize( 12 )
                            .setFontFamily( "serif" )
                            .setListening( false )
                            .setTextBaseLine( TextBaseLine.MIDDLE )
                            .setTextAlign( TextAlign.CENTER );
                    g.add( t );
                }

                @Override
                public void edit( final IGridCellValue<String> value,
                                  final Callback<IGridCellValue<String>, IGridCellValue<String>> callback ) {
                    editor.edit( value,
                                 callback );
                }

            };
            grid3.addColumn( column );
        }
        GridDataFactory.populate( grid3,
                                  GRID3_ROWS );

        //Grid 4
        final GridData grid4 = new GridData();
        final GridColumn<String> column1 = new GridColumn<String>( "G4-Col: 1",
                                                                   100 ) {

            @Override
            public void renderCell( final Group g,
                                    final GridCell<String> cell,
                                    final GridCellRenderContext context ) {
                final Text t = new Text( cell.getValue().getValue() )
                        .setFillColor( ColorName.GREY )
                        .setFontSize( 12 )
                        .setFontFamily( "serif" )
                        .setListening( false )
                        .setTextBaseLine( TextBaseLine.MIDDLE )
                        .setTextAlign( TextAlign.CENTER );
                g.add( t );
            }

            @Override
            public void edit( final IGridCellValue<String> value,
                              final Callback<IGridCellValue<String>, IGridCellValue<String>> callback ) {
                editor.edit( value,
                             callback );
            }

        };
        grid4.addColumn( column1 );

        final GridColumn<Boolean> column2 = new GridColumn<Boolean>( "G4-Col: 2",
                                                                     100 ) {

            private CheckBoxDOMContainerFactory factory = new CheckBoxDOMContainerFactory( table );

            @Override
            public void renderCell( final Group g,
                                    final GridCell<Boolean> cell,
                                    final GridCellRenderContext context ) {
                final GridCellDOMContainer dom = factory.getContainer();
                dom.transform( context );
                dom.initialise( cell );
            }

            @Override
            public void edit( final IGridCellValue<Boolean> value,
                              final Callback<IGridCellValue<Boolean>, IGridCellValue<Boolean>> callback ) {
                //Editing disabled for now
            }

            @Override
            public void attachToDom() {
                factory.attach();
            }

            @Override
            public void detachFromDom() {
                factory.detach();
            }

        };
        grid4.addColumn( column2 );

        for ( int rowIndex = 0; rowIndex < GRID4_ROWS; rowIndex++ ) {
            final GridRow row = new GridRow();
            grid4.addRow( row );
            for ( int columnIndex = 0; columnIndex < grid4.getColumns().size(); columnIndex++ ) {
                switch ( columnIndex ) {
                    case 0:
                        grid4.setCell( rowIndex,
                                       columnIndex,
                                       new BaseGridCellValue<>( "(" + columnIndex + ", " + rowIndex + ")" ) );
                        break;
                    case 1:
                        grid4.setCell( rowIndex,
                                       columnIndex,
                                       new BaseGridCellValue<>( true ) );
                        break;
                }
            }
        }

        //Link grids
        grid1.getColumns().get( 9 ).setLink( grid2.getColumns().get( 0 ) );
        grid2.getColumns().get( 3 ).setLink( grid3.getColumns().get( 0 ) );
        grid3.getColumns().get( 0 ).setLink( grid1.getColumns().get( 0 ) );

        //Widgets
        final MergableGridWidget gridWidget1 = new MergableGridWidget( grid1,
                                                                       this,
                                                                       new MergableGridRenderer() );

        final MergableGridWidget gridWidget2 = new MergableGridWidget( grid2,
                                                                       this,
                                                                       new MergableGridRenderer() );

        final MergableGridWidget gridWidget3 = new MergableGridWidget( grid3,
                                                                       this,
                                                                       new MergableGridRenderer() );

        final GridWidget gridWidget4 = new GridWidget( grid4,
                                                       this,
                                                       new RedGridRenderer() );

        //Add Widgets to the Layer
        gridWidget1.setLocation( new Point2D( -1300,
                                              0 ) );
        gridWidget2.setLocation( new Point2D( 0,
                                              750 ) );
        gridWidget3.setLocation( new Point2D( 1050,
                                              0 ) );
        gridWidget4.setLocation( new Point2D( 1800,
                                              0 ) );
        gridLayer.add( gridWidget1 );
        gridLayer.add( gridWidget2 );
        gridLayer.add( gridWidget3 );
        gridLayer.add( gridWidget4 );

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

        //Style selectors
        final Map<String, IGridRenderer<GridData>> basicRenderers = new HashMap<>();
        final RedGridRenderer redRenderer = new RedGridRenderer();
        final GreenGridRenderer greenRenderer = new GreenGridRenderer();
        final BlueGridRenderer blueRenderer = new BlueGridRenderer();
        basicRenderers.put( redRenderer.getName(),
                            redRenderer );
        basicRenderers.put( greenRenderer.getName(),
                            greenRenderer );
        basicRenderers.put( blueRenderer.getName(),
                            blueRenderer );
        for ( String name : basicRenderers.keySet() ) {
            basicRendererSelector.addItem( name );
        }
        basicRendererSelector.addChangeHandler( new ChangeHandler() {
            @Override
            @SuppressWarnings("unused")
            public void onChange( final ChangeEvent event ) {
                final IGridRenderer<GridData> renderer = basicRenderers.get( basicRendererSelector.getItemText( basicRendererSelector.getSelectedIndex() ) );
                gridWidget4.setRenderer( renderer );
                gridLayer.draw();
            }
        } );

        //Merged indicator
        chkShowMerged.setValue( grid1.isMerged() );
        chkShowMerged.addChangeHandler( new ChangeHandler() {
            @Override
            @SuppressWarnings("unused")
            public void onChange( final ChangeEvent event ) {
                grid1.setMerged( chkShowMerged.getValue() );
                grid2.setMerged( chkShowMerged.getValue() );
                grid3.setMerged( chkShowMerged.getValue() );
                gridLayer.draw();
            }
        } );
    }

    @Override
    public void select( final IGridData<?, ?, ?> selectable ) {
        gridLayer.select( selectable );
    }

    @Override
    public void scrollIntoView( final IGridColumn<?, ?> link ) {
        gridLayer.scrollIntoView( link );
    }

}
