package org.anstis.client.grid.widget;

import org.anstis.client.grid.model.GridColumn;

public class GridWidgetConnector {

    private GridColumn sourceColumn;
    private GridColumn targetColumn;
    private Direction direction;

    public enum Direction {
        EAST_WEST,
        WEST_EAST
    }

    public GridWidgetConnector( final GridColumn sourceColumn,
                                final GridColumn targetColumn,
                                final Direction direction ) {
        this.sourceColumn = sourceColumn;
        this.targetColumn = targetColumn;
        this.direction = direction;
    }

    public GridColumn getSourceColumn() {
        return sourceColumn;
    }

    public GridColumn getTargetColumn() {
        return targetColumn;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        GridWidgetConnector connector = (GridWidgetConnector) o;

        if ( !sourceColumn.equals( connector.sourceColumn ) ) {
            return false;
        }
        if ( !targetColumn.equals( connector.targetColumn ) ) {
            return false;
        }
        if ( !direction.equals( connector.direction ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceColumn.hashCode();
        result = ~~result;
        result = 31 * result + targetColumn.hashCode();
        result = ~~result;
        result = 31 * result + direction.hashCode();
        result = ~~result;
        return result;
    }

}
