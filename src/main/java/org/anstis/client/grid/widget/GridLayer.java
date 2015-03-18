package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Arrow;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ArrowType;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.widget.animation.GridWidgetScrollIntoViewAnimation;
import org.anstis.client.grid.widget.dnd.GridWidgetHandlersState;
import org.anstis.client.grid.widget.dnd.GridWidgetMouseDownHandler;
import org.anstis.client.grid.widget.dnd.GridWidgetMouseMoveHandler;
import org.anstis.client.grid.widget.dnd.GridWidgetMouseUpHandler;

public class GridLayer extends Layer implements ISelectionManager {

    private Map<Grid, GridWidget> selectables = new HashMap<>();
    private Map<GridWidgetConnector, Arrow> connectors = new HashMap<>();

    private Rectangle bounds;
    private boolean isRedrawScheduled = false;

    public GridLayer() {
        bounds = new Rectangle( 0, 0 )
                .setVisible( false );
        add( bounds );

        //Column DnD handlers
        final GridWidgetHandlersState state = new GridWidgetHandlersState();
        addNodeMouseDownHandler( new GridWidgetMouseDownHandler( this,
                                                                 state,
                                                                 selectables ) );
        addNodeMouseMoveHandler( new GridWidgetMouseMoveHandler( this,
                                                                 state,
                                                                 selectables ) );
        addNodeMouseUpHandler( new GridWidgetMouseUpHandler( this,
                                                             state,
                                                             selectables ) );
    }

    @Override
    public void draw() {
        if ( !isRedrawScheduled ) {
            isRedrawScheduled = true;
            Scheduler.get().scheduleFinally( new Command() {

                //This is helpful when debugging rendering issues to set the bounds smaller than the Viewport
                private static final int PADDING = 0;

                @Override
                public void execute() {
                    updateBounds();
                    updateConnectors();
                    GridLayer.super.draw();
                    isRedrawScheduled = false;
                }

                private void updateBounds() {
                    final Viewport viewport = GridLayer.this.getViewport();
                    Transform transform = viewport.getTransform();
                    if ( transform == null ) {
                        viewport.setTransform( transform = new Transform() );
                    }
                    final double x = ( PADDING - transform.getTranslateX() ) / transform.getScaleX();
                    final double y = ( PADDING - transform.getTranslateY() ) / transform.getScaleY();
                    bounds.setLocation( new Point2D( x,
                                                     y ) );
                    bounds.setHeight( ( viewport.getHeight() - PADDING * 2 ) / transform.getScaleX() );
                    bounds.setWidth( ( viewport.getWidth() - PADDING * 2 ) / transform.getScaleY() );
                    bounds.setStrokeWidth( 1.0 / transform.getScaleX() );
                }

                private void updateConnectors() {
                    for ( Map.Entry<GridWidgetConnector, Arrow> e : connectors.entrySet() ) {
                        final GridWidgetConnector connector = e.getKey();
                        final Arrow arrow = e.getValue();
                        final GridColumn sourceColumn = connector.getSourceColumn();
                        final GridColumn targetColumn = connector.getTargetColumn();
                        final GridWidget sourceGrid = getLinkedGrid( sourceColumn );
                        final GridWidget targetGrid = getLinkedGrid( targetColumn );
                        if ( connector.getDirection() == GridWidgetConnector.Direction.EAST_WEST ) {
                            arrow.setStart( new Point2D( sourceGrid.getX() + sourceGrid.getWidth() / 2,
                                                         arrow.getStart().getY() ) );
                        } else {
                            arrow.setEnd( new Point2D( targetGrid.getX() + targetGrid.getWidth(),
                                                       arrow.getEnd().getY() ) );
                        }
                    }

                }
            } );
        }
    }

    @Override
    public Layer add( final IPrimitive<?> child ) {
        addSelectable( child );
        return super.add( child );
    }

    private void addSelectable( final IPrimitive<?> child,
                                final IPrimitive<?>... children ) {
        final List<IPrimitive<?>> all = new ArrayList<>();
        all.add( child );
        all.addAll( Arrays.asList( children ) );
        for ( IPrimitive<?> c : all ) {
            if ( c instanceof GridWidget ) {
                final GridWidget gridWidget = (GridWidget) c;
                selectables.put( gridWidget.getModel(),
                                 gridWidget );
                addConnectors();
            }
        }
    }

    private void addConnectors() {
        for ( Map.Entry<Grid, GridWidget> e1 : selectables.entrySet() ) {
            for ( GridColumn c : e1.getKey().getColumns() ) {
                if ( c.isLinked() ) {
                    final GridWidget linkWidget = getLinkedGrid( c.getLink() );
                    if ( linkWidget != null ) {
                        GridWidgetConnector.Direction direction;
                        final Point2D sp = new Point2D( e1.getValue().getX() + e1.getValue().getWidth() / 2,
                                                        e1.getValue().getY() + e1.getValue().getHeight() / 2 );
                        final Point2D ep = new Point2D( linkWidget.getX() + linkWidget.getWidth() / 2,
                                                        linkWidget.getY() + linkWidget.getHeight() / 2 );
                        if ( sp.getX() < ep.getX() ) {
                            direction = GridWidgetConnector.Direction.EAST_WEST;
                            sp.setX( sp.getX() + e1.getValue().getWidth() / 2 );
                            ep.setX( ep.getX() - linkWidget.getWidth() / 2 );
                        } else {
                            direction = GridWidgetConnector.Direction.WEST_EAST;
                            sp.setX( sp.getX() - e1.getValue().getWidth() / 2 );
                            ep.setX( ep.getX() + linkWidget.getWidth() / 2 );
                        }

                        final GridWidgetConnector connector = new GridWidgetConnector( c,
                                                                                       c.getLink(),
                                                                                       direction );

                        if ( !connectors.containsKey( connector ) ) {
                            final Arrow arrow = new Arrow( sp,
                                                           ep,
                                                           10.0,
                                                           40.0,
                                                           45.0,
                                                           45.0,
                                                           ArrowType.AT_END )
                                    .setStrokeColor( ColorName.DARKGRAY )
                                    .setFillColor( ColorName.TAN )
                                    .setStrokeWidth( 2.0 );
                            connectors.put( connector,
                                            arrow );
                            super.add( arrow );
                            arrow.moveToBottom();
                        }
                    }
                }
            }
        }
    }

    private GridWidget getLinkedGrid( final GridColumn link ) {
        GridWidget gridWidget = null;
        for ( Map.Entry<Grid, GridWidget> e : selectables.entrySet() ) {
            if ( e.getKey().getColumns().contains( link ) ) {
                gridWidget = e.getValue();
                break;
            }
        }
        return gridWidget;
    }

    @Override
    public Layer add( final IPrimitive<?> child,
                      final IPrimitive<?>... children ) {
        addSelectable( child,
                       children );
        return super.add( child,
                          children );
    }

    @Override
    public Layer remove( final IPrimitive<?> child ) {
        removeSelectable( child );
        return super.remove( child );
    }

    private void removeSelectable( final IPrimitive<?> child,
                                   final IPrimitive<?>... children ) {
        final List<IPrimitive<?>> all = new ArrayList<>();
        all.add( child );
        all.addAll( Arrays.asList( children ) );
        for ( IPrimitive<?> c : all ) {
            if ( c instanceof GridWidget ) {
                final GridWidget gridWidget = (GridWidget) c;
                selectables.remove( gridWidget.getModel() );
                removeConnectors( gridWidget.getModel() );
            }
        }
    }

    private void removeConnectors( final Grid model ) {
        final List<GridWidgetConnector> removedConnectors = new ArrayList<>();
        for ( Map.Entry<GridWidgetConnector, Arrow> e : connectors.entrySet() ) {
            if ( model.getColumns().contains( e.getKey().getSourceColumn() ) || model.getColumns().contains( e.getKey().getTargetColumn() ) ) {
                remove( e.getValue() );
                removedConnectors.add( e.getKey() );
            }
        }
        //Remove Connectors from HashMap after iteration of EntrySet to avoid ConcurrentModificationException
        for ( GridWidgetConnector c : removedConnectors ) {
            connectors.remove( c );
        }
    }

    @Override
    public Layer removeAll() {
        selectables.clear();
        return super.removeAll();
    }

    @Override
    public void select( final Grid selectable ) {
        for ( Map.Entry<Grid, GridWidget> e : selectables.entrySet() ) {
            e.getValue().deselect();
        }
        if ( selectables.containsKey( selectable ) ) {
            selectables.get( selectable ).select();
        }
        draw();
    }

    @Override
    public void scrollIntoView( final GridColumn link ) {
        final GridWidget gridWidget = getLinkedGrid( link );
        if ( gridWidget == null ) {
            return;
        }

        final GridWidgetScrollIntoViewAnimation a = new GridWidgetScrollIntoViewAnimation( gridWidget,
                                                                                           new Command() {
                                                                                               @Override
                                                                                               public void execute() {
                                                                                                   select( gridWidget.getModel() );
                                                                                               }
                                                                                           } );
        a.run();
    }

    public Rectangle getVisibleBounds() {
        return bounds;
    }

}
