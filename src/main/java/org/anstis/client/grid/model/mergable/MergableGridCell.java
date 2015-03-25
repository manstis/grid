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

public class MergableGridCell implements IMergableGridCell {

    private String value;
    private boolean isMerged = false;
    private long mergedCellCount = 1;

    public MergableGridCell( final String value ) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isMerged() {
        return isMerged;
    }

    @Override
    public long getMergedCellCount() {
        return mergedCellCount;
    }

    void setMerged( boolean isMerged ) {
        this.isMerged = isMerged;
    }

    void setMergedCellCount( long mergedCellCount ) {
        this.mergedCellCount = mergedCellCount;
    }

}
