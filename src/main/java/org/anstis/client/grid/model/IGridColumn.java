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
package org.anstis.client.grid.model;

import com.ait.lienzo.client.core.shape.Group;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

/**
 * Interface defining a Column within a grid.
 * @param <R> The generic type of the Rows handled by the column
 * @param <V> The generic type of the Cells handled by the column
 */
public interface IGridColumn<R extends IGridRow<V>, V extends IGridCell<?>> extends IHasResources {

    /**
     * Get the column's title
     * @return
     */
    String getTitle();

    /**
     * Get the column's width
     * @return
     */
    int getWidth();

    /**
     * Set the columns width
     * @param width
     */
    void setWidth( final int width );

    /**
     * Flag indicating this column is linked to another
     * @return
     */
    boolean isLinked();

    /**
     * Get the column to which this column is linked
     * @return
     */
    IGridColumn getLink();

    /**
     * Set the column to which this column is linked
     * @param link
     */
    void setLink( final IGridColumn<R, V> link );

    /**
     * Get the logical index to which this column relates. Columns may be re-ordered and therefore, to
     * avoid manipulating the underlying row data, the logical index of the column may be different to their
     * physical index (i.e. the order in which they were added to the grid).
     * @return
     */
    int getIndex();

    /**
     * Set the logical index of the column, to support indirection of columns' access to row data.
     * @param index
     */
    void setIndex( final int index );

    /**
     * Render the column's Header..
     * @return
     */
    Group renderHeader();

    /**
     * Render a cell for the column for a row. Normally a column would use its logical index
     * to retrieve the corresponding element from the row to be rendered.
     * @param row A row of data
     * @param context Contextual information to support rendering
     * @return
     */
    Group renderCell( final R row,
                      final GridCellRenderContext context );

}
