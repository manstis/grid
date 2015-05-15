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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.anstis.client.grid.model.BaseGridCellValue;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;
import org.gwtbootstrap3.client.ui.CheckBox;

public class CheckBoxDOMElement extends BaseDOMElement<Boolean, CheckBox> {

    //Hack to centre CheckBox
    private static final int SIZE = 20;

    private static final Command NOP_COMMAND = new Command() {
        @Override
        public void execute() {
            //Do nothing
        }
    };

    private final CheckBox cb = new CheckBox();
    private GridCellRenderContext context;

    public CheckBoxDOMElement( final GridLayer gridLayer,
                               final BaseGridWidget<?, ?> gridWidget,
                               final IDOMElementFactory<Boolean, ?> factory,
                               final AbsolutePanel domElementContainer ) {
        super( gridLayer,
               gridWidget,
               factory,
               domElementContainer );
        final Style style = cb.getElement().getStyle();
        style.setMarginTop( 0,
                            Style.Unit.PX );
        style.setMarginLeft( 2,
                             Style.Unit.PX );
        style.setWidth( SIZE,
                        Style.Unit.PX );
        style.setHeight( SIZE,
                         Style.Unit.PX );

        getContainer().setWidget( cb );

        cb.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                flush( NOP_COMMAND );
            }
        } );
        cb.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur( final BlurEvent event ) {
                if ( context != null ) {
                    factory.freeUnusedResources();
                }
            }
        } );
    }

    @Override
    public void initialise( final IGridCell<Boolean> cell,
                            final GridCellRenderContext context ) {
        this.context = context;
        cb.setValue( cell == null ? false : cell.getValue().getValue() );
        final Style style = cb.getElement().getStyle();
        style.setLeft( ( context.getWidth() - SIZE ) / 2,
                       Style.Unit.PX );
        style.setTop( ( context.getHeight() - SIZE ) / 2,
                      Style.Unit.PX );
        transform( context );
    }

    @Override
    public CheckBox getWidget() {
        return cb;
    }

    @Override
    public void flush( final Command command ) {
        if ( context != null ) {
            final BaseGridWidget<?, ?> widget = context.getWidget();
            widget.getModel().setCell( context.getRowIndex(),
                                       context.getColumnIndex(),
                                       new BaseGridCellValue<>( cb.getValue() ) );
            ( (GridLayer) widget.getLayer() ).draw( command );
        } else {
            command.execute();
        }
    }

}
