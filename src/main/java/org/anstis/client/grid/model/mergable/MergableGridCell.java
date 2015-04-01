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

public class MergableGridCell extends BaseGridCell implements IMergableGridCell {

    private int mergedCellCount = 1;
    private boolean isGrouped = false;

    public MergableGridCell( final String value ) {
        super( value );
    }

    void setValue( final String value ) {
        this.value = value;
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
    public boolean isGrouped() {
        return isGrouped;
    }

    @Override
    public void setGrouped( final boolean isGrouped ) {
        this.isGrouped = isGrouped;
    }

    void setMergedCellCount( final int mergedCellCount ) {
        this.mergedCellCount = mergedCellCount;
    }

}
