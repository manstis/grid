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
package org.anstis.client.grid.model.basic;

import com.ait.lienzo.client.core.shape.Group;
import com.google.gwt.core.client.Callback;
import org.anstis.client.grid.model.BaseGridColumn;
import org.anstis.client.grid.model.IGridCellValue;

public abstract class GridColumn<T> extends BaseGridColumn<GridRow, GridCell<?>> {

    public GridColumn( final String title,
                       final int width ) {
        super( title,
               width );
    }

    @Override
    public Group renderRow( final GridRow row ) {
        final GridCell cell = row.getCells().get( getIndex() );
        if ( cell == null ) {
            return null;
        }
        final Group g = new Group();
        renderCell( g,
                    cell );
        return g;
    }

    public abstract void renderCell( final Group g,
                                     final GridCell<T> cell );

    public abstract void edit( final IGridCellValue<T> value,
                               final Callback<IGridCellValue<T>, IGridCellValue<T>> callback );

}
