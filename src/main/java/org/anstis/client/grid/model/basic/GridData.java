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

import java.util.ArrayList;
import java.util.List;

public class GridData implements IGridData<GridRow, GridCell> {

    private List<GridRow> rows = new ArrayList<>();

    @Override
    public void setRows( final List<GridRow> rows ) {
        this.rows = rows;
    }

    @Override
    public GridRow getRow( final int index ) {
        return rows.get( index );
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public GridCell newCell( final String value ) {
        return new GridCell( value );
    }

    @Override
    public GridCell getCell( final int rowIndex,
                             final int columnIndex ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return null;
        }
        return rows.get( rowIndex ).getCells().get( columnIndex );
    }

    @Override
    public void setCell( final int rowIndex,
                         final int columnIndex,
                         final GridCell cell ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return;
        }
        final GridRow row = rows.get( rowIndex );
        row.setCell( columnIndex,
                     cell );
    }

}
