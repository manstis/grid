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
import java.util.List;

import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.model.mergable.MergableGridRow;

public class GridDataFactory {

    private static double FILL_FACTOR = 0.75;

    public static MergableGridData makeData( final int columnCount,
                                             final int rowCount ) {
        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        data.setRows( rows );
        for ( int rowIndex = 0; rowIndex < rowCount; rowIndex++ ) {
            rows.add( new MergableGridRow() );
            for ( int columnIndex = 0; columnIndex < columnCount; columnIndex++ ) {
                if ( Math.random() < FILL_FACTOR ) {
                    data.setCell( rowIndex,
                                  columnIndex,
                                  new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
                }
            }
        }
        return data;
    }

}
