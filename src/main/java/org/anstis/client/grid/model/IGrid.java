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

import java.util.List;

public interface IGrid<D extends IGridData<? extends IGridRow<? extends IGridCell>, ? extends IGridCell>> {

    List<GridColumn> getColumns();

    void addColumn( final GridColumn column );

    void addColumn( final int index,
                    final GridColumn column );

    void removeColumn( final GridColumn column );

    void moveColumnTo( final int index,
                       final GridColumn column );

    D getData();

    void setData( final D data );

    int mapToAbsoluteIndex( final int relativeIndex );

    int mapToRelativeIndex( final int absoluteIndex );

    double getColumnOffset( final GridColumn gridColumn );

    double getColumnOffset( final int columnIndex );

}
