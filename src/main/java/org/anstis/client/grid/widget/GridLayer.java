package org.anstis.client.grid.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.animation.AbstractAnimation;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationCallback;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.animation.TimedAnimation;
import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.ait.lienzo.client.core.mediator.IMediator;
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
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;
import org.anstis.client.grid.util.GridCoordinateUtils;

public class GridLayer extends Layer implements ISelectionManager {

    private Map<Grid, GridWidget> selectables = new HashMap<>();
    private Map<Connector, Arrow> connectors = new HashMap<>();

    private Rectangle bounds;

    public GridLayer() {
        bounds = new Rectangle( 0, 0 )
                .setVisible( false );
        add( bounds );

        //Column DnD handlers
        final GridWidgetMouseState state = new GridWidgetMouseState();
        addNodeMouseDownHandler( new GridWidgetMouseDownHandler( state ) );
        addNodeMouseMoveHandler( new GridWidgetMouseMoveHandler( state ) );
        addNodeMouseUpHandler( new GridWidgetMouseUpHandler( state ) );
    }

    @Override
    public void draw() {
        Scheduler.get().scheduleFinally( new Command() {

            private static final int PADDING = 0;

            @Override
            public void execute() {
                updateBounds();
                updateConnectors();
                GridLayer.super.draw();
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
                for ( Map.Entry<Connector, Arrow> e : connectors.entrySet() ) {
                    final Connector connector = e.getKey();
                    final Arrow arrow = e.getValue();
                    final GridColumn sourceColumn = connector.getSourceColumn();
                    final GridColumn targetColumn = connector.getTargetColumn();
                    final GridWidget sourceGrid = getLinkedGrid( sourceColumn );
                    final GridWidget targetGrid = getLinkedGrid( targetColumn );
                    if ( connector.getDirection() == Connector.Direction.EAST_WEST ) {
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
                        Connector.Direction direction;
                        final Point2D sp = new Point2D( e1.getValue().getX() + e1.getValue().getWidth() / 2,
                                                        e1.getValue().getY() + e1.getValue().getHeight() / 2 );
                        final Point2D ep = new Point2D( linkWidget.getX() + linkWidget.getWidth() / 2,
                                                        linkWidget.getY() + linkWidget.getHeight() / 2 );
                        if ( sp.getX() < ep.getX() ) {
                            direction = Connector.Direction.EAST_WEST;
                            sp.setX( sp.getX() + e1.getValue().getWidth() / 2 );
                            ep.setX( ep.getX() - linkWidget.getWidth() / 2 );
                        } else {
                            direction = Connector.Direction.WEST_EAST;
                            sp.setX( sp.getX() - e1.getValue().getWidth() / 2 );
                            ep.setX( ep.getX() + linkWidget.getWidth() / 2 );
                        }

                        final Connector connector = new Connector( c,
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
        final List<Connector> removedConnectors = new ArrayList<>();
        for ( Map.Entry<Connector, Arrow> e : connectors.entrySet() ) {
            if ( model.getColumns().contains( e.getKey().getSourceColumn() ) || model.getColumns().contains( e.getKey().getTargetColumn() ) ) {
                remove( e.getValue() );
                removedConnectors.add( e.getKey() );
            }
        }
        //Remove Connectors from HashMap after iteration of EntrySet to avoid ConcurrentModificationException
        for ( Connector c : removedConnectors ) {
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

        final AbstractAnimation a = getScrollIntoViewAnimation( gridWidget );
        a.run();
    }

    public Rectangle getVisibleBounds() {
        return bounds;
    }

    private AbstractAnimation getScrollIntoViewAnimation( final GridWidget gridWidget ) {
        final Viewport vp = getViewport();
        final TimedAnimation a = new TimedAnimation( 500,
                                                     new IAnimationCallback() {

                                                         private Point2D delta;
                                                         private Point2D startTranslation;
                                                         private AnimationTweener tweener = AnimationTweener.EASE_OUT;

                                                         @Override
                                                         public void onStart( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             if ( vp.getTransform() == null ) {
                                                                 vp.setTransform( new Transform() );
                                                             }
                                                             startTranslation = getViewportTranslation().mul( -1.0 );

                                                             final double vpw = vp.getWidth();
                                                             final double vps = vp.getTransform().getScaleX();
                                                             final double gw = gridWidget.getWidth();
                                                             final double offsetX = ( ( vpw / vps ) - gw ) / 2;
                                                             final Point2D endTranslation = new Point2D( gridWidget.getX() - offsetX,
                                                                                                         gridWidget.getY() ).mul( -1.0 );

                                                             delta = new Point2D( endTranslation.getX() - startTranslation.getX(),
                                                                                  endTranslation.getY() - startTranslation.getY() );

                                                             select( gridWidget.getModel() );
                                                             GridLayer.this.setListening( false );
                                                             GridLayer.this.batch();
                                                         }

                                                         @Override
                                                         public void onFrame( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             final double pct = assertPct( animation.getPercent() );
                                                             final Point2D frameLocation = startTranslation.add( delta.mul( pct ) );
                                                             final Point2D actualLocation = getViewportTranslation();
                                                             final Point2D delta = frameLocation.add( actualLocation );
                                                             final Transform transform = vp.getTransform();
                                                             transform.translate( delta.getX(),
                                                                                  delta.getY() );
                                                             GridLayer.this.draw();
                                                         }

                                                         @Override
                                                         public void onClose( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             GridLayer.this.setListening( true );
                                                             GridLayer.this.batch();
                                                             GridLayer.this.draw();
                                                         }

                                                         private Point2D getViewportTranslation() {
                                                             final Transform transform = vp.getTransform();
                                                             final Transform t = transform.copy().getInverse();
                                                             final Point2D p = new Point2D( t.getTranslateX(),
                                                                                            t.getTranslateY() );
                                                             return p;
                                                         }

                                                         private double assertPct( final double pct ) {
                                                             if ( pct < 0 ) {
                                                                 return 0;
                                                             }
                                                             if ( pct > 1.0 ) {
                                                                 return 1.0;
                                                             }
                                                             return tweener.apply( pct );
                                                         }
                                                     } );
        return a;
    }

    private static class Connector {

        private GridColumn sourceColumn;
        private GridColumn targetColumn;
        private Direction direction;

        enum Direction {
            EAST_WEST,
            WEST_EAST
        }

        private Connector( final GridColumn sourceColumn,
                           final GridColumn targetColumn,
                           final Direction direction ) {
            this.sourceColumn = sourceColumn;
            this.targetColumn = targetColumn;
            this.direction = direction;
        }

        private GridColumn getSourceColumn() {
            return sourceColumn;
        }

        private GridColumn getTargetColumn() {
            return targetColumn;
        }

        private Direction getDirection() {
            return direction;
        }

        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }

            Connector connector = (Connector) o;

            if ( !sourceColumn.equals( connector.sourceColumn ) ) {
                return false;
            }
            if ( !targetColumn.equals( connector.targetColumn ) ) {
                return false;
            }
            if ( !direction.equals( connector.direction ) ) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = sourceColumn.hashCode();
            result = ~~result;
            result = 31 * result + targetColumn.hashCode();
            result = ~~result;
            result = 31 * result + direction.hashCode();
            result = ~~result;
            return result;
        }
    }

    private class GridWidgetMouseState {

        private Grid proposedGridOwningResizeHandle = null;
        private GridColumn proposedGridColumnOwningResizeHandle = null;
        private Grid actualGridOwningResizeHandle = null;
        private GridColumn actualGridColumnOwningResizeHandle = null;
        private double resizeXStart = 0;
        private double resizeColumnWidthStart = 0;

    }

    private class GridWidgetMouseDownHandler implements NodeMouseDownHandler {

        private final GridWidgetMouseState state;

        private GridWidgetMouseDownHandler( final GridWidgetMouseState state ) {
            this.state = state;
        }

        @Override
        public void onNodeMouseDown( final NodeMouseDownEvent event ) {
            if ( state.proposedGridOwningResizeHandle != null && state.proposedGridColumnOwningResizeHandle != null ) {
                final GridWidget gridWidget = selectables.get( state.proposedGridOwningResizeHandle );
                final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
                state.actualGridOwningResizeHandle = state.proposedGridOwningResizeHandle;
                state.actualGridColumnOwningResizeHandle = state.proposedGridColumnOwningResizeHandle;
                state.proposedGridOwningResizeHandle = null;
                state.proposedGridColumnOwningResizeHandle = null;
                state.resizeXStart = ap.getX();
                state.resizeColumnWidthStart = state.actualGridColumnOwningResizeHandle.getWidth();
            }
        }
    }

    private class GridWidgetMouseMoveHandler implements NodeMouseMoveHandler {

        private static final int COLUMN_RESIZE_HANDLE_SENSITIVITY = 5;
        private static final double COLUMN_MIN_WIDTH = 100;

        private final GridWidgetMouseState state;

        private GridWidgetMouseMoveHandler( final GridWidgetMouseState state ) {
            this.state = state;
        }

        @Override
        public void onNodeMouseMove( final NodeMouseMoveEvent event ) {
            //If we're currently resizing a column we don't need to find a column to resize
            if ( state.actualGridOwningResizeHandle != null && state.actualGridColumnOwningResizeHandle != null ) {
                final GridWidget gridWidget = selectables.get( state.actualGridOwningResizeHandle );
                final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
                final double deltaX = ap.getX() - state.resizeXStart;
                double columnNewWidth = state.resizeColumnWidthStart + deltaX;
                if ( columnNewWidth < COLUMN_MIN_WIDTH ) {
                    columnNewWidth = COLUMN_MIN_WIDTH;
                }
                state.actualGridColumnOwningResizeHandle.setWidth( (int) ( columnNewWidth ) );
                GridLayer.this.draw();
                return;
            }

            //Otherwise try to find a Grid and GridColumn to be re-sized
            state.proposedGridOwningResizeHandle = null;
            state.proposedGridColumnOwningResizeHandle = null;
            for ( Map.Entry<Grid, GridWidget> e : selectables.entrySet() ) {
                final Grid grid = e.getKey();
                final GridWidget gridWidget = e.getValue();
                final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );

                final double ax = ap.getX();
                final double ay = ap.getY();
                if ( ax < 0 || ax > gridWidget.getWidth() ) {
                    continue;
                }
                if ( ay < GridWidget.HEADER_HEIGHT || ay > gridWidget.getHeight() ) {
                    continue;
                }

                double offsetX = 0;
                for ( GridColumn gc : grid.getColumns() ) {
                    final double columnWidth = gc.getWidth();
                    if ( ax > columnWidth + offsetX - COLUMN_RESIZE_HANDLE_SENSITIVITY && ax < columnWidth + offsetX + COLUMN_RESIZE_HANDLE_SENSITIVITY ) {
                        state.proposedGridOwningResizeHandle = grid;
                        state.proposedGridColumnOwningResizeHandle = gc;
                        break;
                    }
                    offsetX = offsetX + columnWidth;
                }
            }

            GridLayer.this.getViewport().getElement().getStyle().setCursor( state.proposedGridOwningResizeHandle != null ? Style.Cursor.COL_RESIZE : Style.Cursor.DEFAULT );
            for ( IMediator mediator : GridLayer.this.getViewport().getMediators() ) {
                mediator.setEnabled( state.proposedGridOwningResizeHandle == null );
            }

        }
    }

    private class GridWidgetMouseUpHandler implements NodeMouseUpHandler {

        private final GridWidgetMouseState state;

        private GridWidgetMouseUpHandler( final GridWidgetMouseState state ) {
            this.state = state;
        }

        @Override
        public void onNodeMouseUp( final NodeMouseUpEvent event ) {
            state.proposedGridOwningResizeHandle = null;
            state.proposedGridColumnOwningResizeHandle = null;
            state.actualGridOwningResizeHandle = null;
            state.actualGridColumnOwningResizeHandle = null;
        }
    }

}
