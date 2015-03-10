package org.anstis.client.grid.util;

import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import org.anstis.client.grid.widget.GridWidget;

public class GridCoordinateUtils {

    public static Point2D mapToGridWidgetAbsolutePoint( final GridWidget gridWidget,
                                                        final Point2D point ) {
        final Viewport viewport = gridWidget.getViewport();
        Transform transform = viewport.getTransform();
        if ( transform == null ) {
            viewport.setTransform( transform = new Transform() );
        }

        transform = transform.copy().getInverse();
        final Point2D p = new Point2D( point.getX(),
                                       point.getY() );
        transform.transform( p,
                             p );
        return p.add( gridWidget.getLocation().mul( -1.0 ) );
    }

}
