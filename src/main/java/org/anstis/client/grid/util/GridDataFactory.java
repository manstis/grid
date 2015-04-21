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

import org.anstis.client.grid.model.basic.GridData;
import org.anstis.client.grid.model.basic.GridRow;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.model.mergable.MergableGridRow;

public class GridDataFactory {

    private static double FILL_FACTOR = 1.0;

    public static void populate( final GridData grid,
                                 final int rowCount ) {
        final int columnCount = grid.getColumns().size();
        for ( int rowIndex = 0; rowIndex < rowCount; rowIndex++ ) {
            final GridRow row = new GridRow();
            row.setHeight( getRowHeight() );
            grid.addRow( row );
            for ( int columnIndex = 0; columnIndex < columnCount; columnIndex++ ) {
                if ( Math.random() < FILL_FACTOR ) {
                    grid.setCell( rowIndex,
                                  columnIndex,
                                  "(" + columnIndex + ", " + rowIndex + ")" );
                }
            }
        }
    }

    public static void populate( final MergableGridData grid,
                                 final int rowCount ) {
        final int columnCount = grid.getColumns().size();
        for ( int rowIndex = 0; rowIndex < rowCount; rowIndex++ ) {
            final MergableGridRow row = new MergableGridRow( getRowHeight() );
            grid.addRow( row );
            for ( int columnIndex = 0; columnIndex < columnCount; columnIndex++ ) {
                if ( Math.random() < FILL_FACTOR ) {
                    grid.setCell( rowIndex,
                                  columnIndex,
                                  "(" + columnIndex + ", " + rowIndex + ")" );
                }
            }
        }
    }

    private static double getRowHeight() {
        final int r = (int) Math.round( Math.random() * 3 );
        switch ( r ) {
            case 0:
                return 20.0;
            case 1:
                return 40.0;
        }
        return 60.0;
    }

}
