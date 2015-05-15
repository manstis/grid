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
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;

public abstract class BaseSingletonDOMElementFactory<T, E extends BaseDOMElement> implements IDOMElementFactory<T, E> {

    protected final GridLayer gridLayer;
    protected final BaseGridWidget<?, ?> gridWidget;
    protected final AbsolutePanel domElementContainer;
    protected final E container;

    public BaseSingletonDOMElementFactory( final GridLayer gridLayer,
                                           final BaseGridWidget<?, ?> gridWidget,
                                           final AbsolutePanel domElementContainer ) {
        this.gridLayer = gridLayer;
        this.gridWidget = gridWidget;
        this.domElementContainer = domElementContainer;
        this.container = createDomElement( gridLayer,
                                           gridWidget,
                                           domElementContainer );
    }

}