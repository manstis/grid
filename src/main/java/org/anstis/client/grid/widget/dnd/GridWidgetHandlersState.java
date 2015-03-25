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
package org.anstis.client.grid.widget.dnd;

import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.basic.IGrid;

public class GridWidgetHandlersState {

    private IGrid<?> grid = null;
    private GridColumn gridColumn = null;
    private GridWidgetHandlersOperation operation = GridWidgetHandlersOperation.NONE;

    private double eventInitialX = 0;
    private double eventInitialColumnWidth = 0;
    private GridWidgetColumnProxy eventColumnHighlight = new GridWidgetColumnProxy();

    public enum GridWidgetHandlersOperation {
        NONE,
        COLUMN_RESIZE_PENDING,
        COLUMN_RESIZE,
        COLUMN_MOVE_PENDING,
        COLUMN_MOVE
    }

    public IGrid<?> getGrid() {
        return grid;
    }

    public void setGrid( final IGrid<?> grid ) {
        this.grid = grid;
    }

    public GridColumn getGridColumn() {
        return gridColumn;
    }

    public void setGridColumn( final GridColumn gridColumn ) {
        this.gridColumn = gridColumn;
    }

    public GridWidgetHandlersOperation getOperation() {
        return operation;
    }

    public void setOperation( final GridWidgetHandlersOperation operation ) {
        this.operation = operation;
    }

    public double getEventInitialX() {
        return eventInitialX;
    }

    public void setEventInitialX( final double eventInitialX ) {
        this.eventInitialX = eventInitialX;
    }

    public double getEventInitialColumnWidth() {
        return eventInitialColumnWidth;
    }

    public void setEventInitialColumnWidth( final double eventInitialColumnWidth ) {
        this.eventInitialColumnWidth = eventInitialColumnWidth;
    }

    public GridWidgetColumnProxy getEventColumnHighlight() {
        return eventColumnHighlight;
    }

}
