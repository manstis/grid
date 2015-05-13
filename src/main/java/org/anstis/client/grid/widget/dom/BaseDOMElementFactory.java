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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AbsolutePanel;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public abstract class BaseDOMElementFactory<T, E extends BaseDOMElement> implements IDOMElementFactory<T, E> {

    private final GridLayer gridLayer;
    private final BaseGridWidget<?, ?> gridWidget;
    private final AbsolutePanel domElementContainer;
    private final List<E> containers = new ArrayList<>();

    private int consumed = 0;

    public BaseDOMElementFactory( final GridLayer gridLayer,
                                  final BaseGridWidget<?, ?> gridWidget,
                                  final AbsolutePanel domElementContainer ) {
        this.gridLayer = gridLayer;
        this.gridWidget = gridWidget;
        this.domElementContainer = domElementContainer;
    }

    @Override
    public void addCell( final IGridCell<T> cell,
                         final GridCellRenderContext context ) {
        E container;
        if ( consumed + 1 > containers.size() ) {
            container = newElement( gridLayer,
                                    gridWidget,
                                    domElementContainer );
            containers.add( container );
        } else {
            container = containers.get( consumed );
        }
        consumed++;
        container.attach();
        container.initialise( cell,
                              context );
    }

    @Override
    public void initialiseResources() {
        consumed = 0;
    }

    @Override
    public void destroyResources() {
        for ( E container : containers ) {
            container.detach();
        }
        containers.clear();
        consumed = 0;
    }

    @Override
    public void freeResources() {
        final List<E> freed = new ArrayList<>();
        for ( int i = consumed; i < containers.size(); i++ ) {
            final E container = containers.get( i );
            container.detach();
            freed.add( container );
        }
        for ( E container : freed ) {
            containers.remove( container );
        }
    }

}
