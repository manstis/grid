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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public abstract class GridCellDOMContainer<T, W extends Widget> {

    protected final int SIZE = 20;
    protected final AbsolutePanel parent;
    protected final HTMLPanel container = new HTMLPanel( "<div/>" );

    public GridCellDOMContainer( final AbsolutePanel parent ) {
        this.parent = parent;
        final Style style = container.getElement().getStyle();
        style.setWidth( SIZE,
                        Style.Unit.PX );
        style.setHeight( SIZE,
                         Style.Unit.PX );
    }

    public abstract void initialise( final IGridCell<T> cell );

    public abstract W getWidget();

    protected HTMLPanel getContainer() {
        return container;
    }

    public void transform( final GridCellRenderContext context ) {
        final Transform transform = context.getTransform();
        final Style style = container.getElement().getStyle();
        style.setPosition( Style.Position.ABSOLUTE );
        style.setLeft( ( context.getX() * transform.getScaleX() ) + transform.getTranslateX(),
                       Style.Unit.PX );
        style.setTop( ( context.getY() * transform.getScaleY() ) + transform.getTranslateY(),
                      Style.Unit.PX );

        final String scale = "scale(" + transform.getScaleX() + ", " + transform.getScaleY() + ")";
        final String translate = "translate(" + ( ( SIZE - SIZE * transform.getScaleX() ) / -2.0 ) + "px, " + ( ( SIZE - SIZE * transform.getScaleY() ) / -2.0 ) + "px)";
        style.setProperty( "WebkitTransform", translate + " " + scale );
        style.setProperty( "MozTransform", translate + " " + scale );
        style.setProperty( "Transform", translate + " " + scale );
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
