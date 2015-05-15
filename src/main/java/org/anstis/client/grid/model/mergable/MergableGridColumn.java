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
package org.anstis.client.grid.model.mergable;

import com.ait.lienzo.client.core.shape.Group;
import org.anstis.client.grid.model.BaseGridColumn;
import org.anstis.client.grid.model.ICallback;
import org.anstis.client.grid.model.IGridCellValue;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public abstract class MergableGridColumn<T> extends BaseGridColumn<MergableGridRow, MergableGridCell<?>> {

    public MergableGridColumn( final String title,
                               final int width ) {
        super( title,
               width );
    }

    @Override
    public Group renderRow( final MergableGridRow row,
                            final GridCellRenderContext context ) {
        final MergableGridCell cell = row.getCells().get( getIndex() );
        if ( cell == null ) {
            return null;
        }
        final Group g = new Group();
        renderCell( g,
                    cell,
                    context );
        return g;
    }

    public abstract void renderCell( final Group g,
                                     final MergableGridCell<T> cell,
                                     final GridCellRenderContext context );

    public void edit( final MergableGridCell<T> cell,
                      final GridCellRenderContext context,
                      final ICallback<IGridCellValue<T>> callback ) {
        //Do nothing by default
    }

}
