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

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGridData<R extends IGridRow<C>, C extends IGridCell> implements IGridData<R, C> {

    protected List<R> rows = new ArrayList<>();

    @Override
    public void setRows( final List<R> rows ) {
        this.rows = rows;
    }

    @Override
    public R getRow( final int index ) {
        return rows.get( index );
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public C getCell( final int rowIndex,
                      final int columnIndex ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return null;
        }
        return rows.get( rowIndex ).getCells().get( columnIndex );
    }

}
