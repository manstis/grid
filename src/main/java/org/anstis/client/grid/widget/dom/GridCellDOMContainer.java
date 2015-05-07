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
package org.anstis.client.grid.widget.dom;

import java.util.Iterator;

import com.ait.lienzo.client.core.types.Transform;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public abstract class GridCellDOMContainer<T, W extends Widget> {

    private static final double EPSILON = 0.0000001;
    private static final NumberFormat FORMAT = NumberFormat.getFormat( "0.0000" );

    protected final AbsolutePanel parent;
    protected final SimplePanel container = new SimplePanel();

    public GridCellDOMContainer( final AbsolutePanel parent ) {
        this.parent = parent;
        //Disable HTML5 DnD support on element, otherwise it interferes with column resizing
        container.getElement().setAttribute( "draggable", "false" );

        //Allow MouseEvents over absolutely positioned elements to bubble
        container.getElement().getStyle().setProperty( "pointerEvents", "none" );
    }

    public abstract void initialise( final IGridCell<T> cell,
                                     final GridCellRenderContext context );

    public abstract W getWidget();

    protected SimplePanel getContainer() {
        return container;
    }

    protected void transform( final GridCellRenderContext context ) {
        final Transform transform = context.getTransform();
        final Style style = container.getElement().getStyle();
        final double width = context.getWidth();
        final double height = context.getHeight();

        style.setPosition( Style.Position.ABSOLUTE );
        style.setLeft( ( context.getX() * transform.getScaleX() ) + transform.getTranslateX(),
                       Style.Unit.PX );
        style.setTop( ( context.getY() * transform.getScaleY() ) + transform.getTranslateY(),
                      Style.Unit.PX );
        style.setWidth( width,
                        Style.Unit.PX );
        style.setHeight( height,
                         Style.Unit.PX );

        if ( isOne( transform.getScaleX() ) && isOne( transform.getScaleY() ) ) {
            style.clearProperty( "WebkitTransform" );
            style.clearProperty( "MozTransform" );
            style.clearProperty( "Transform" );
            return;
        }

        final String scale = "scale(" + FORMAT.format( transform.getScaleX() ) + ", " + FORMAT.format( transform.getScaleY() ) + ")";
        final String translate = "translate(" + FORMAT.format( ( ( width - width * transform.getScaleX() ) / -2.0 ) ) + "px, " + FORMAT.format( ( ( height - height * transform.getScaleY() ) / -2.0 ) ) + "px)";
        style.setProperty( "WebkitTransform", translate + " " + scale );
        style.setProperty( "MozTransform", translate + " " + scale );
        style.setProperty( "Transform", translate + " " + scale );
    }

    private boolean isOne( final double value ) {
        return value >= 1.0 - EPSILON && value <= 1.0 + EPSILON;
    }

    public void attach() {
        final Iterator<Widget> itr = parent.iterator();
        while ( itr.hasNext() ) {
            if ( itr.next().equals( container ) ) {
                return;
            }
        }
        parent.add( container );
    }

    public void detach() {
        final Iterator<Widget> itr = parent.iterator();
        while ( itr.hasNext() ) {
            if ( itr.next().equals( container ) ) {
                itr.remove();
                return;
            }
        }
    }

}
