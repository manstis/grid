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
import com.ait.lienzo.client.core.shape.Arrow;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ArrowType;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public class GridLayer extends Layer implements ISelectionManager {

    private Map<Grid, GridWidget> selectables = new HashMap<>();
    private Map<Connector, IPrimitive<?>> connectors = new HashMap<>();

    @Override
    public void draw() {
        Scheduler.get().scheduleFinally( new Command() {
            @Override
            public void execute() {
                GridLayer.super.draw();
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
                        final Connector connector = new Connector( c,
                                                                   c.getLink() );
                        if ( !connectors.containsKey( connector ) ) {
                            final Point2D sp = new Point2D( e1.getValue().getX() + e1.getValue().getWidth() / 2,
                                                            e1.getValue().getY() + e1.getValue().getHeight() / 2 );
                            final Point2D ep = new Point2D( linkWidget.getX() + linkWidget.getWidth() / 2,
                                                            linkWidget.getY() + linkWidget.getHeight() / 2 );
                            if ( sp.getX() < ep.getX() ) {
                                sp.setX( sp.getX() + e1.getValue().getWidth() / 2 );
                                ep.setX( ep.getX() - linkWidget.getWidth() / 2 );
                            } else {
                                sp.setX( sp.getX() - e1.getValue().getWidth() / 2 );
                                ep.setX( ep.getX() + linkWidget.getWidth() / 2 );
                            }
//                            if ( sp.getY() < ep.getY() ) {
//                                sp.setY( sp.getY() + e1.getValue().getHeight() / 2 );
//                                ep.setY( ep.getY() - linkWidget.getHeight() / 2 );
//                            } else {
//                                sp.setY( sp.getY() - e1.getValue().getHeight() / 2 );
//                                ep.setY( ep.getY() + linkWidget.getHeight() / 2 );
//                            }
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
        for ( Map.Entry<Connector, IPrimitive<?>> e : connectors.entrySet() ) {
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

        private Connector( final GridColumn sourceColumn,
                           final GridColumn targetColumn ) {
            this.sourceColumn = sourceColumn;
            this.targetColumn = targetColumn;
        }

        private GridColumn getSourceColumn() {
            return sourceColumn;
        }

        private GridColumn getTargetColumn() {
            return targetColumn;
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

            return true;
        }

        @Override
        public int hashCode() {
            int result = sourceColumn.hashCode();
            result = ~~result;
            result = 31 * result + targetColumn.hashCode();
            result = ~~result;
            return result;
        }
    }

}
