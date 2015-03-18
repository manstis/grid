package org.anstis.client.grid.widget.dnd;

import org.anstis.client.grid.model.Grid;
import org.anstis.client.grid.model.GridColumn;

public class GridWidgetHandlersState {

    public static final int COLUMN_RESIZE_HANDLE_SENSITIVITY = 5;
    public static final double COLUMN_MIN_WIDTH = 100;

    private Grid grid = null;
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

    public Grid getGrid() {
        return grid;
    }

    public void setGrid( final Grid grid ) {
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

    public void setEventColumnHighlight( final GridWidgetColumnProxy eventColumnHighlight ) {
        this.eventColumnHighlight = eventColumnHighlight;
    }

}
