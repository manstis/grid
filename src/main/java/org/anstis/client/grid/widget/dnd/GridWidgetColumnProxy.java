package org.anstis.client.grid.widget.dnd;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.ColorName;

public class GridWidgetColumnProxy extends Group {

    private Rectangle r = new Rectangle( 0, 0 )
            .setFillColor( ColorName.DARKGRAY )
            .setAlpha( 0.5 );

    public GridWidgetColumnProxy() {
        add( r );
    }

    public GridWidgetColumnProxy setWidth( final double width ) {
        r.setWidth( width );
        return this;
    }

    public GridWidgetColumnProxy setHeight( final double height ) {
        r.setHeight( height );
        return this;
    }

}
