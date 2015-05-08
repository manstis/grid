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

import com.google.gwt.dom.client.Style;
import org.anstis.client.grid.model.IGridColumn;
import org.anstis.client.grid.model.IGridData;

public class GridWidgetHandlersState {

    private IGridData grid = null;
    private IGridColumn gridColumn = null;
    private GridWidgetHandlersOperation operation = GridWidgetHandlersOperation.NONE;
    private Style.Cursor cursor = Style.Cursor.DEFAULT;

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

    public IGridData getGrid() {
        return grid;
    }

    public void setGrid( final IGridData grid ) {
        this.grid = grid;
    }

    public IGridColumn getGridColumn() {
        return gridColumn;
    }

    public void setGridColumn( final IGridColumn gridColumn ) {
        this.gridColumn = gridColumn;
    }

    public GridWidgetHandlersOperation getOperation() {
        return operation;
    }

    public void setOperation( final GridWidgetHandlersOperation operation ) {
        this.operation = operation;
    }

    public Style.Cursor getCursor() {
        return cursor;
    }

    public void setCursor( Style.Cursor cursor ) {
        this.cursor = cursor;
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
