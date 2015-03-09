package org.anstis.client.grid.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridDataFactory {

    private static double FILL_FACTOR = 1.0;

    public static List<Map<Integer, String>> makeData( final int cols,
                                                       final int rows ) {
        final List<Map<Integer, String>> data = new ArrayList<>();
        for ( int i = 0; i < rows; i++ ) {
            data.add( makeRow( cols ) );
        }
        return data;
    }

    public static Map<Integer, String> makeRow( final int cols ) {
        final Map<Integer, String> row = new HashMap<>();
        for ( int i = 0; i < cols; i++ ) {
            if ( Math.random() < FILL_FACTOR ) {
                row.put( i,
                         "A" + (int) ( Math.random() * 10 ) );
            }
        }
        return row;
    }

}
