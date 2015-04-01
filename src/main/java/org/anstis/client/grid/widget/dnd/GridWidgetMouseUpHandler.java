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
package org.anstis.client.grid.widget.dnd;

import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.google.gwt.dom.client.Style;
import org.anstis.client.grid.model.IGridData;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.GridLayer;

public class GridWidgetMouseUpHandler implements NodeMouseUpHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState state;
    private final Map<IGridData<?, ?, ?>, BaseGridWidget<?>> selectables;

    public GridWidgetMouseUpHandler( final GridLayer layer,
                                     final GridWidgetHandlersState state,
                                     final Map<IGridData<?, ?, ?>, BaseGridWidget<?>> selectables ) {
        this.layer = layer;
        this.state = state;
        this.selectables = selectables;
    }

    @Override
    public void onNodeMouseUp( final NodeMouseUpEvent event ) {
        state.setGrid( null );
        state.setGridColumn( null );
        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
        layer.getViewport().getElement().getStyle().setCursor( Style.Cursor.DEFAULT );
        layer.remove( state.getEventColumnHighlight() );
        layer.draw();
    }

}
