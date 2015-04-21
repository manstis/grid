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
    private Stack<Double> heights = new Stack<>();
    private int collapseLevel = 0;

    public MergableGridRow() {
        this( 20 );
    }

    public MergableGridRow( final double height ) {
        this.height = height;
        this.heights.push( height );
    }

    @Override
    public boolean isMerged() {
        return hasMergedCells;
    }

    @Override
    public boolean isCollapsed() {
        return collapseLevel > 0;
    }

    @Override
    public void collapse() {
        collapseLevel++;
        heights.push( height );
        for ( MergableGridCell cell : cells.values() ) {
            cell.collapse();
        }
    }

    @Override
    public void expand() {
        if ( collapseLevel == 0 ) {
            return;
        }
        collapseLevel--;
        height = heights.pop();
        for ( MergableGridCell cell : cells.values() ) {
            cell.expand();
        }
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

}
