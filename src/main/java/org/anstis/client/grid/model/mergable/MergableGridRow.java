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

import org.anstis.client.grid.model.BaseGridRow;

public class MergableGridRow extends BaseGridRow<MergableGridCell> implements IMergableGridRow<MergableGridCell> {

    private boolean isGrouped = false;
    private boolean hasMergedCells = false;

    @Override
    public boolean hasMergedCells() {
        return hasMergedCells;
    }

    @Override
    public boolean isGrouped() {
        return isGrouped;
    }

    void setCell( final int columnIndex,
                  final String value ) {
        if ( !cells.containsKey( columnIndex ) ) {
            cells.put( columnIndex,
                       new MergableGridCell( value ) );
        } else {
            cells.get( columnIndex ).setValue( value );
        }
    }

    public void setIsGrouped( final boolean isGrouped ) {
        this.isGrouped = isGrouped;
    }

    void setHasMergedCells( final boolean hasMergedCells ) {
        this.hasMergedCells = hasMergedCells;
    }

}
