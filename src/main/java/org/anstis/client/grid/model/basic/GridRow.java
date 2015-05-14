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

import org.anstis.client.grid.model.BaseGridRow;
import org.anstis.client.grid.model.IGridCellValue;

public class GridRow extends BaseGridRow<GridCell<?>> {

    void setCell( final int columnIndex,
                  final IGridCellValue value ) {
        if ( !cells.containsKey( columnIndex ) ) {
            cells.put( columnIndex,
                       new GridCell( value ) );
        } else {
            cells.get( columnIndex ).setValue( value );
        }
    }

    void deleteCell( final int columnIndex ) {
        cells.remove( columnIndex );
    }

}
