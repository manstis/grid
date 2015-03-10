package org.anstis.client.grid.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridDataFactory {

    private static double FILL_FACTOR = 0.75;

    public static List<Map<Integer, String>> makeData( final int columnCount,
                                                       final int rowCount ) {
        final List<Map<Integer, String>> data = new ArrayList<>();
        for ( int i = 0; i < rowCount; i++ ) {
            data.add( makeRow( columnCount,
                               i ) );
        }
        return data;
    }

    public static Map<Integer, String> makeRow( final int columnCount,
                                                final int rowIndex ) {
        final Map<Integer, String> row = new HashMap<>();
        for ( int i = 0; i < columnCount; i++ ) {
            if ( Math.random() < FILL_FACTOR ) {
                row.put( i,
                         "(" + i + ", " + rowIndex + ")" );
            }
        }
        return row;
    }

}
