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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.anstis.client.grid.model.BaseGridCellValue;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;
import org.gwtbootstrap3.client.ui.TextBox;

public class TextBoxDOMElement extends BaseDOMElement<String, TextBox> {

    private static final int HEIGHT = 16;

    private static final Command NOP_COMMAND = new Command() {
        @Override
        public void execute() {
            //Do nothing
        }
    };

    private final TextBox tb = new TextBox();
    private GridCellRenderContext context;

    public TextBoxDOMElement( final GridLayer gridLayer,
                              final BaseGridWidget<?, ?> gridWidget,
                              final IDOMElementFactory<String, ?> factory,
                              final AbsolutePanel domElementContainer ) {
        super( gridLayer,
               gridWidget,
               factory,
               domElementContainer );
        final Style style = tb.getElement().getStyle();
        style.setWidth( 100,
                        Style.Unit.PCT );
        style.setHeight( HEIGHT,
                         Style.Unit.PX );
        style.setPaddingLeft( 2,
                              Style.Unit.PX );
        style.setPaddingRight( 2,
                               Style.Unit.PX );
        style.setFontSize( 10,
                           Style.Unit.PX );

        getContainer().getElement().getStyle().setPaddingLeft( 5,
                                                               Style.Unit.PX );
        getContainer().getElement().getStyle().setPaddingRight( 5,
                                                                Style.Unit.PX );
        getContainer().setWidget( tb );

        tb.addValueChangeHandler( new ValueChangeHandler<String>() {
            @Override
            public void onValueChange( final ValueChangeEvent event ) {
                flush( NOP_COMMAND );
            }
        } );
        tb.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur( final BlurEvent event ) {
                if ( context != null ) {
                    factory.freeUnusedResources();
                }
            }
        } );
    }

    @Override
    public void initialise( final IGridCell<String> cell,
                            final GridCellRenderContext context ) {
        this.context = context;
        tb.setText( cell == null ? "" : cell.getValue().getValue() );
        final Style style = tb.getElement().getStyle();
        style.setMarginTop( ( context.getHeight() - HEIGHT ) / 2,
                            Style.Unit.PX );
        transform( context );
    }

    @Override
    public TextBox getWidget() {
        return tb;
    }

    @Override
    public void flush( final Command command ) {
        if ( context != null ) {
            final BaseGridWidget<?, ?> widget = context.getWidget();
            final String text = tb.getText();
            if ( text.trim().isEmpty() ) {
                widget.getModel().deleteCell( context.getRowIndex(),
                                              context.getColumnIndex() );

            } else {
                widget.getModel().setCell( context.getRowIndex(),
                                           context.getColumnIndex(),
                                           new BaseGridCellValue<>( tb.getText() ) );
            }
            ( (GridLayer) widget.getLayer() ).draw( command );
        } else {
            command.execute();
        }
    }

}
