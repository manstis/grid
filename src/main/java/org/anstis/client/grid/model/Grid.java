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

}
