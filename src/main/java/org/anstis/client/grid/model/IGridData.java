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

public interface IGridData<R extends IGridRow<V>, C extends IGridColumn<R, V>, V extends IGridCell<?>> {

    List<C> getColumns();

    void addColumn( final C column );

    void addColumn( final int index,
                    final C column );

    void removeColumn( final C column );

    void moveColumnTo( final int index,
                       final C column );

    double getColumnOffset( final C gridColumn );

    double getColumnOffset( final int columnIndex );

    double getRowOffset( final R gridRow );

    double getRowOffset( final int rowIndex );

    void addRow( final R row );

    void addRow( final int rowIndex,
                 final R row );

    R getRow( final int rowIndex );

    int getRowCount();

    V getCell( final int rowIndex,
               final int columnIndex );

    void setCell( final int rowIndex,
                  final int columnIndex,
                  final IGridCellValue<?> value );

}
