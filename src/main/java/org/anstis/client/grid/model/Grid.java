package org.anstis.client.grid.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grid {

    private List<GridColumn> columns = new ArrayList<>();
    private List<Map<Integer, String>> data = new ArrayList<>();

    public List<GridColumn> getColumns() {
        return columns;
    }

    public List<Map<Integer, String>> getData() {
        return data;
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

        if ( data != grid.data ) {
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
        result = 31 * result + data.hashCode();
        result = ~~result;
        return result;
    }
}
