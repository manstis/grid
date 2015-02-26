package org.anstis.client.grid.transition;

import com.ait.lienzo.client.core.animation.AnimationCallback;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.animation.TimedAnimation;
import com.ait.lienzo.client.core.shape.Layer;
import org.anstis.client.grid.widget.IGridWidget;

public class GridSwapperGroupScale implements IGridSwapper {

    private Layer layer;

    public GridSwapperGroupScale( final Layer layer ) {
        this.layer = layer;
    }

    @Override
    public void swap( final IGridWidget gridWidget1,
                      final IGridWidget gridWidget2 ) {
        final TimedAnimation a = new TimedAnimation( 250,
                                                     new AnimationCallback() {

                                                         @Override
                                                         public void onStart( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             gridWidget1.setAlpha( 1.0 );
                                                             gridWidget1.setScale( 1.0, 1.0 );
                                                             gridWidget1.setOffset( gridWidget1.getWidth() / 2, 0.0 );
                                                             gridWidget2.setAlpha( 0.0 );
                                                             gridWidget1.setScale( 0.5, 0.5 );
                                                             gridWidget2.setOffset( gridWidget2.getWidth() / 2, 0.0 );
                                                             layer.add( gridWidget2 );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onFrame( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             gridWidget1.setAlpha( 1 - animation.getPercent() );
                                                             gridWidget1.setScale( 1 - ( animation.getPercent() / 2 ), 1 - ( animation.getPercent() / 2 ) );
                                                             gridWidget2.setAlpha( animation.getPercent() );
                                                             gridWidget2.setScale( 0.5 + ( animation.getPercent() / 2 ), 0.5 + ( animation.getPercent() / 2 ) );
                                                             layer.draw();
                                                         }

                                                         @Override
                                                         public void onClose( final IAnimation animation,
                                                                              final IAnimationHandle handle ) {
                                                             gridWidget1.setAlpha( 0.0 );
                                                             gridWidget2.setAlpha( 1.0 );
                                                             gridWidget2.setScale( 1.0, 1.0 );
                                                             layer.remove( gridWidget1 );
                                                             layer.draw();
                                                         }
                                                     } );
        a.run();
    }

}
