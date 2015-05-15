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

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.types.Transform;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public abstract class BaseDOMElement<T, W extends Widget> {

    private static final double EPSILON = 0.0000001;
    private static final NumberFormat FORMAT = NumberFormat.getFormat( "0.0000" );

    protected final GridLayer gridLayer;
    protected final BaseGridWidget<?, ?> gridWidget;
    protected final IDOMElementFactory<T, ?> factory;
    protected final AbsolutePanel domElementContainer;
    protected final SimplePanel container = new SimplePanel();

    public BaseDOMElement( final GridLayer gridLayer,
                           final BaseGridWidget<?, ?> gridWidget,
                           final IDOMElementFactory<T, ?> factory,
                           final AbsolutePanel domElementContainer ) {
        this.gridLayer = gridLayer;
        this.gridWidget = gridWidget;
        this.factory = factory;
        this.domElementContainer = domElementContainer;

        final Style style = container.getElement().getStyle();
        style.setPosition( Style.Position.ABSOLUTE );

        //MouseEvents over absolutely positioned elements do not bubble through the DOM.
        //Consequentially Event Handlers on GridLayer do not receive notification of MouseMove
        //Events used during column resizing. Therefore we manually bubble events to GridLayer.
        container.addDomHandler( new MouseDownHandler() {
            @Override
            public void onMouseDown( final MouseDownEvent event ) {
                gridLayer.onNodeMouseDown( new NodeMouseDownEvent( event ) {

                    @Override
                    public int getX() {
                        //Adjust the x-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getX() + container.getElement().getOffsetLeft();
                    }

                    @Override
                    public int getY() {
                        //Adjust the y-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getY() + container.getElement().getOffsetTop();
                    }

                } );
            }
        }, MouseDownEvent.getType() );
        container.addDomHandler( new MouseMoveHandler() {
            @Override
            public void onMouseMove( final MouseMoveEvent event ) {
                //The DOM Element changes the Cursor, so set to the state determined by the MouseEvent Handlers on GridLayer
                style.setCursor( gridLayer.getGridWidgetHandlersState().getCursor() );

                gridLayer.onNodeMouseMove( new NodeMouseMoveEvent( event ) {

                    @Override
                    public int getX() {
                        //Adjust the x-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getX() + container.getElement().getOffsetLeft();
                    }

                    @Override
                    public int getY() {
                        //Adjust the y-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getY() + container.getElement().getOffsetTop();
                    }

                } );
            }
        }, MouseMoveEvent.getType() );
        container.addDomHandler( new MouseUpHandler() {
            @Override
            public void onMouseUp( final MouseUpEvent event ) {
                gridLayer.onNodeMouseUp( new NodeMouseUpEvent( event ) {

                    @Override
                    public int getX() {
                        //Adjust the x-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getX() + container.getElement().getOffsetLeft();
                    }

                    @Override
                    public int getY() {
                        //Adjust the y-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getY() + container.getElement().getOffsetTop();
                    }

                } );
            }
        }, MouseUpEvent.getType() );
        container.addDomHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                gridWidget.onNodeMouseClick( new NodeMouseClickEvent( event ) {

                    @Override
                    public int getX() {
                        //Adjust the x-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getX() + container.getElement().getOffsetLeft();
                    }

                    @Override
                    public int getY() {
                        //Adjust the y-coordinate (relative to the DOM Element) to be relative to the GridCanvas.
                        return super.getY() + container.getElement().getOffsetTop();
                    }

                } );
            }
        }, ClickEvent.getType() );
    }

    protected abstract void initialise( final IGridCell<T> cell,
                                        final GridCellRenderContext context );

    public abstract W getWidget();

    protected SimplePanel getContainer() {
        return container;
    }

    protected void transform( final GridCellRenderContext context ) {
        final Transform transform = context.getTransform();
        final double width = context.getWidth();
        final double height = context.getHeight();

        final Style style = container.getElement().getStyle();

        //Reposition and transform the DOM Element
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
        style.setProperty( "WebkitTransform",
                           translate + " " + scale );
        style.setProperty( "MozTransform",
                           translate + " " + scale );
        style.setProperty( "Transform",
                           translate + " " + scale );
    }

    private boolean isOne( final double value ) {
        return value >= 1.0 - EPSILON && value <= 1.0 + EPSILON;
    }

    public void attach() {
        final Iterator<Widget> itr = domElementContainer.iterator();
        while ( itr.hasNext() ) {
            if ( itr.next().equals( container ) ) {
                return;
            }
        }
        //When an Element is detached it's Position configuration is cleared, so reset it
        final Style style = container.getElement().getStyle();
        style.setPosition( Style.Position.ABSOLUTE );

        domElementContainer.add( container );
    }

    public void detach() {
        final Iterator<Widget> itr = domElementContainer.iterator();
        while ( itr.hasNext() ) {
            if ( itr.next().equals( container ) ) {
                itr.remove();
                return;
            }
        }
    }

}
