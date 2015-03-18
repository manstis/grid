package org.anstis.client.grid.widget.dnd;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.ColorName;

public class GridWidgetColumnProxy extends Group {

    private Rectangle r = new Rectangle( 0, 0 )
            .setFillColor( ColorName.DARKGRAY )
            .setAlpha( 0.5 );
    private Line l = new Line()
            .setStrokeWidth( 2.0 )
            .setStrokeColor( ColorName.BLACK );

    public GridWidgetColumnProxy() {
        add( r ).add( l );
    }

    public GridWidgetColumnProxy setWidth( final double width ) {
        r.setWidth( width );
        return this;
    }

    public GridWidgetColumnProxy setHeight( final double height ) {
        r.setHeight( height );
        l.getPoints().get( 1 ).setY( height );
        return this;
    }

}
