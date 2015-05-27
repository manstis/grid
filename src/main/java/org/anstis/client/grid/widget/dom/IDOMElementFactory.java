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
import org.anstis.client.grid.model.ICallback;
import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.model.IHasResources;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

/**
 * Definition of a Factor that can create DOMElements for GWT Widget based cell content.
 * DOMElements are transient in nature and only exist when required, such as when a column
 * and row is visible or when a cell is being edited.
 * @param <T> The data-type of the cell
 * @param <E> The DOMElement type that this Factory generates.
 */
public interface IDOMElementFactory<T, E> extends IHasResources {

    /**
     * Create a DOMElement.
     * @param gridLayer The Lienzo layer on which the Grid Widget is attached. DOMElements may need to redraw the Layer when their state changes.
     * @param gridWidget The GridWidget to which this DOMElement is to be associated.
     * @param domElementContainer The GWT container for the DOMElement.
     * @return
     */
    E createDomElement( final GridLayer gridLayer,
                        final BaseGridWidget<?, ?> gridWidget,
                        final AbsolutePanel domElementContainer );

    /**
     * Initialise a DOMElement for a cell. This does not attach the DOMElement to the GWT container.
     * It does not attach the DOMElement to the GWT container as other activities may need to be completed
     * after it has been attached but before the Layer is drawn, such as setting the Focus.
     * @param cell The cell requiring the DOMElement.
     * @param context The render context of the cell.
     * @param callback A callback that is invoked after the cell has been initialised.
     */
    void initialiseDomElement( final IGridCell<T> cell,
                               final GridCellRenderContext context,
                               final ICallback<E> callback );

}
