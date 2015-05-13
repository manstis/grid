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
package org.anstis.client.grid.widget.basic;

import com.google.gwt.core.client.Callback;
import org.anstis.client.grid.model.IGridCellValue;
import org.anstis.client.grid.model.basic.GridCell;
import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.GridData;
import org.anstis.client.grid.widget.BaseGridWidgetMouseDoubleClickHandler;
import org.anstis.client.grid.widget.ISelectionManager;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public class GridWidgetMouseDoubleClickHandler extends BaseGridWidgetMouseDoubleClickHandler<GridWidget> {

    public GridWidgetMouseDoubleClickHandler( final GridWidget gridWidget,
                                              final ISelectionManager selectionManager,
                                              final IGridRenderer<GridData> renderer ) {
        super( gridWidget,
               selectionManager,
               renderer );
    }

    @Override
    protected void doEdit( final int rowIndex,
                           final int columnIndex ) {
        final GridCell cell = gridWidget.getModel().getCell( rowIndex,
                                                             columnIndex );
        final GridColumn column = gridWidget.getModel().getColumns().get( columnIndex );
        column.edit( cell == null ? null : cell.getValue(),
                     new Callback<IGridCellValue<?>, IGridCellValue<?>>() {

                         @Override
                         public void onFailure( final IGridCellValue<?> value ) {
                             //Do nothing
                         }

                         @Override
                         public void onSuccess( final IGridCellValue<?> value ) {
                             gridWidget.getModel().setCell( rowIndex,
                                                            columnIndex,
                                                            value );
                             gridWidget.getLayer().draw();
                         }
                     } );
    }

}
