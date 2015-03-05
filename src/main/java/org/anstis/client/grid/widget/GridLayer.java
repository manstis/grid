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
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import org.anstis.client.grid.model.Grid;

public class GridLayer extends Layer implements ISelectionManager {

    private Map<Grid, IGridWidget<?>> selectables = new HashMap<>();

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
            if ( c instanceof IGridWidget<?> ) {
                final IGridWidget<?> gridWidget = (IGridWidget<?>) c;
                selectables.put( gridWidget.getModel(),
                                 gridWidget );
            }
        }
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
            if ( c instanceof IGridWidget<?> ) {
                final IGridWidget<?> gridWidget = (IGridWidget<?>) c;
                selectables.remove( gridWidget.getModel() );
            }
        }
    }

    @Override
    public Layer removeAll() {
        selectables.clear();
        return super.removeAll();
    }

    @Override
    public void select( final Grid selectable ) {
        for ( Map.Entry<Grid, IGridWidget<?>> e : selectables.entrySet() ) {
            e.getValue().deselect();
        }
        if ( selectables.containsKey( selectable ) ) {
            selectables.get( selectable ).select();
        }
        draw();
    }

    @Override
    public void scrollIntoView( final Grid selectable ) {
        final IGridWidget<?> gridWidget = selectables.get( selectable );
        if ( gridWidget == null ) {
            return;
        }

        final AbstractAnimation a = getScrollIntoViewAnimation( gridWidget );
        a.run();
    }

    private AbstractAnimation getScrollIntoViewAnimation( final IGridWidget<?> gridWidget ) {
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
                                                             startTranslation = getViewportTranslation();
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
                                                             final Point2D delta = frameLocation.add( actualLocation.mul( -1.0 ) );
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
                                                             return p.mul( -1.0 );
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

}
