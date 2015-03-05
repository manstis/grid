package org.anstis.client.grid.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<GridColumn> columns = new ArrayList<>();
    private int rows;

    public List<GridColumn> getColumns() {
        return columns;
    }

    public int getData() {
        return rows;
    }

    public void setData( final int rows ) {
        this.rows = rows;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Grid ) ) {
            return false;
        }

        Grid grid = (Grid) o;

        if ( rows != grid.rows ) {
            return false;
        }
        if ( !columns.equals( grid.columns ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = columns.hashCode();
        result = ~~result;
        result = 31 * result + rows;
        result = ~~result;
        return result;
    }
}
