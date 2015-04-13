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

import java.util.Stack;

import org.anstis.client.grid.model.BaseGridRow;

public class MergableGridRow extends BaseGridRow<MergableGridCell> implements IMergableGridRow<MergableGridCell> {

    private boolean hasMergedCells = false;
    private int collapseLevel = 0;
    private Stack<Double> heights = new Stack<>();

    @Override
    public boolean isMerged() {
        return hasMergedCells;
    }

    @Override
    public boolean isCollapsed() {
        return collapseLevel > 0;
    }

    @Override
    public void storeHeight() {
        heights.push( height );
    }

    @Override
    public void restoreHeight() {
        height = heights.pop();
    }

    @Override
    public double peekHeight() {
        return heights.peek();
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

    void setHasMergedCells( final boolean hasMergedCells ) {
        this.hasMergedCells = hasMergedCells;
    }

    void increaseCollapseLevel() {
        this.collapseLevel++;
    }

    void decreaseCollapseLevel() {
        this.collapseLevel--;
    }

}
