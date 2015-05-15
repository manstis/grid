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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.anstis.client.grid.model.ICallback;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public class TextBoxSingletonDOMElementFactory extends BaseSingletonDOMElementFactory<String, TextBoxDOMElement> {

    private TextBoxDOMElement e;

    public TextBoxSingletonDOMElementFactory( final GridLayer gridLayer,
                                              final BaseGridWidget<?, ?> gridWidget,
                                              final AbsolutePanel domElementContainer ) {
        super( gridLayer,
               gridWidget,
               domElementContainer );
    }

    @Override
    public TextBoxDOMElement createDomElement( final GridLayer gridLayer,
                                               final BaseGridWidget<?, ?> gridWidget,
                                               final AbsolutePanel domElementContainer ) {
        e = new TextBoxDOMElement( gridLayer,
                                   gridWidget,
                                   this,
                                   domElementContainer );
        return e;
    }

    @Override
    public void getDomElementForCell( final IGridCell<String> cell,
                                      final GridCellRenderContext context,
                                      final ICallback<TextBoxDOMElement> callback ) {
        e.flush( new Command() {
            @Override
            public void execute() {
                container.initialise( cell,
                                      context );
                callback.callback( container );
            }
        } );
    }

    @Override
    public void initialiseResources() {
        destroyResources();
    }

    @Override
    public void destroyResources() {
        e.getWidget().setFocus( false );
        container.detach();
    }

    @Override
    public void freeUnusedResources() {
        destroyResources();
    }

}
