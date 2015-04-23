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

import org.anstis.client.grid.model.BaseGridCell;
import org.anstis.client.grid.model.IGridCellValue;

public class MergableGridCell<T> extends BaseGridCell<T> implements IMergableGridCell<T> {

    private int mergedCellCount = 1;
    private int collapseLevel = 0;

    public MergableGridCell( final IGridCellValue<T> value ) {
        super( value );
    }

    @Override
    public boolean isMerged() {
        return getMergedCellCount() != 1;
    }

    @Override
    public int getMergedCellCount() {
        return mergedCellCount;
    }

    @Override
    public boolean isCollapsed() {
        return collapseLevel > 0;
    }

    @Override
    public void collapse() {
        collapseLevel++;
    }

    @Override
    public void expand() {
        collapseLevel--;
    }

    @Override
    public void reset() {
        mergedCellCount = 1;
        collapseLevel = 0;
    }

    void setValue( final IGridCellValue<T> value ) {
        this.value = value;
    }

    void setMergedCellCount( final int mergedCellCount ) {
        this.mergedCellCount = mergedCellCount;
    }

}
