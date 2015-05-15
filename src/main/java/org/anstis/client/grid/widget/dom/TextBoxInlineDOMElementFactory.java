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

import com.google.gwt.user.client.ui.AbsolutePanel;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public class TextBoxInlineDOMElementFactory extends BaseInlineDOMElementFactory<String, TextBoxDOMElement> {

    private TextBoxDOMElement e;
    private boolean isEdit = false;

    public TextBoxInlineDOMElementFactory( final GridLayer gridLayer,
                                           final BaseGridWidget<?, ?> gridWidget,
                                           final AbsolutePanel domElementContainer ) {
        super( gridLayer,
               gridWidget,
               domElementContainer );
    }

    @Override
    public TextBoxDOMElement getCell( final IGridCell<String> cell,
                                      final GridCellRenderContext context ) {
        isEdit = true;
        container.initialise( cell,
                              context );
        return container;
    }

    @Override
    public TextBoxDOMElement newElement( final GridLayer gridLayer,
                                         final BaseGridWidget<?, ?> gridWidget,
                                         final AbsolutePanel domElementContainer ) {
        if ( e == null ) {
            e = new TextBoxDOMElement( gridLayer,
                                       gridWidget,
                                       this,
                                       domElementContainer );
        }
        return e;
    }

    @Override
    public void initialiseResources() {
        if ( isEdit ) {
            destroyResources();
        }
    }

    @Override
    public void destroyResources() {
        isEdit = false;
        e.getWidget().setFocus( false );
        container.detach();
    }

    @Override
    public void freeResources() {
        if ( isEdit ) {
            destroyResources();
        }
    }

}
