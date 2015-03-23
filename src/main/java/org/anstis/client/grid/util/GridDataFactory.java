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
