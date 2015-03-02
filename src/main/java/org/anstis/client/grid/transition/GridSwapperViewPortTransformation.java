package org.anstis.client.grid.transition;

import com.ait.lienzo.client.core.animation.AbstractAnimation;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationCallback;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.animation.TimedAnimation;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import org.anstis.client.grid.widget.IGridWidget;

public class GridSwapperViewPortTransformation implements IGridSwapper {

    private int width;
    private int height;
    private Layer layer;

    public GridSwapperViewPortTransformation( final int width,
                                              final int height,
                                              final Layer layer ) {
        this.width = width;
        this.height = height;
        this.layer = layer;
    }

    @Override
    public void swap( final IGridWidget gridWidget1,
                      final IGridWidget gridWidget2 ) {
        final AbstractAnimation fadeInAnimation = getFadeInAnimation( gridWidget2 );
        final AbstractAnimation fadeOutAnimation = getFadeOutAnimation( gridWidget1,
                                                                        gridWidget2,
                                                                        fadeInAnimation );
        fadeOutAnimation.run();
    }

    private AbstractAnimation getFadeOutAnimation( final IGridWidget gridWidget1,
                                                   final IGridWidget gridWidget2,
                                                   final AbstractAnimation fadeInAnimation ) {
        final Viewport vp = layer.getViewport();
        final TimedAnimation a = new TimedAnimation( 250,
                                                     new IAnimationCallback() {
                                                         @Override
                                                         public void onStart( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             layer.setListening( false );
                                                             layer.batch();

                                                             vp.setTransform( Transform.createViewportTransform( 0,
                                                                                                                 0,
                                                                                                                 width,
                                                                                                                 height,
                                                                                                                 width,
                                                                                                                 height ) );
                                                             gridWidget1.setAlpha( 1.0 );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onFrame( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             final double pct = assertPct( animation.getPercent() );
                                                             vp.getViewport().setTransform( Transform.createViewportTransform( 0,
                                                                                                                               0,
                                                                                                                               width,
                                                                                                                               height,
                                                                                                                               width * ( 1 - ( pct / 2 ) ),
                                                                                                                               height * ( 1 - ( pct / 2 ) ) ) );
                                                             gridWidget1.setAlpha( 1 - animation.getPercent() );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onClose( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             layer.setListening( true );
                                                             layer.batch();

                                                             layer.remove( gridWidget1 );
                                                             layer.add( gridWidget2 );
                                                             layer.draw();
                                                             fadeInAnimation.run();
                                                         }
                                                     } );
        return a;
    }

    private AbstractAnimation getFadeInAnimation( final IGridWidget gridWidget2 ) {
        final Viewport vp = layer.getViewport();
        final TimedAnimation a = new TimedAnimation( 250,
                                                     new IAnimationCallback() {
                                                         @Override
                                                         public void onStart( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             layer.setListening( false );
                                                             layer.batch();

                                                             vp.setTransform( Transform.createViewportTransform( 0,
                                                                                                                 0,
                                                                                                                 width,
                                                                                                                 height,
                                                                                                                 width,
                                                                                                                 height ) );
                                                             gridWidget2.setAlpha( 0.0 );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onFrame( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             final double pct = assertPct( animation.getPercent() );
                                                             vp.getViewport().setTransform( Transform.createViewportTransform( 0,
                                                                                                                               0,
                                                                                                                               width,
                                                                                                                               height,
                                                                                                                               width * ( 0.5 + ( pct / 2 ) ),
                                                                                                                               height * ( 0.5 + ( pct / 2 ) ) ) );
                                                             gridWidget2.setAlpha( animation.getPercent() );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onClose( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             layer.setListening( true );
                                                             layer.batch();
                                                         }
                                                     } );
        return a;
    }

    private double assertPct( final double pct ) {
        if ( pct < 0 ) {
            return 0;
        }
        if ( pct > 1.0 ) {
            return 1.0;
        }
        return pct;
    }

}
