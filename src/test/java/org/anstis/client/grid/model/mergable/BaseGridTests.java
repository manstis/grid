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
package org.anstis.client.grid.model.mergable;

import static org.junit.Assert.*;

public abstract class BaseGridTests {

    public void assertGridIndexes( final MergableGridData data,
                                   final boolean[] expectedRowMergeStates,
                                   final boolean[] expectedRowCollapseStates,
                                   final Expected[][] expectedCellStates ) {
        if ( data.getRowCount() != expectedRowMergeStates.length ) {
            fail( "Size of parameter 'expectedRowMergeStates' differs to expected row count." );
        }
        if ( data.getRowCount() != expectedRowCollapseStates.length ) {
            fail( "Size of parameter 'expectedRowCollapseStates' differs to expected row count." );
        }
        if ( data.getRowCount() != expectedCellStates.length ) {
            fail( "Size of parameter 'expectedCellStates' differs to expected row count." );
        }
        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            final MergableGridRow row = data.getRow( rowIndex );
            assertEquals( "Row[" + rowIndex + "] actual isMerged() differs to expected.",
                          expectedRowMergeStates[ rowIndex ],
                          row.isMerged() );
            assertEquals( "Row[" + rowIndex + "] actual isCollapsed() differs to expected.",
                          expectedRowCollapseStates[ rowIndex ],
                          row.isCollapsed() );

            if ( data.getColumns().size() != expectedCellStates[ rowIndex ].length ) {
                fail( "Size of parameter 'expectedCellStates[" + rowIndex + "]' differs to expected column count." );
            }

            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final MergableGridCell cell = data.getCell( rowIndex,
                                                            columnIndex );
                assertEquals( "Cell[" + columnIndex + ", " + rowIndex + "] actual isMerged() differs to expected.",
                              expectedCellStates[ rowIndex ][ columnIndex ].isMerged,
                              cell.isMerged() );
                assertEquals( "Cell[" + columnIndex + ", " + rowIndex + "] actual getMergedCellCount() differs to expected.",
                              expectedCellStates[ rowIndex ][ columnIndex ].mergedCellCount,
                              cell.getMergedCellCount() );
                assertEquals( "Cell[" + columnIndex + ", " + rowIndex + "] actual getValue() differs to expected.",
                              expectedCellStates[ rowIndex ][ columnIndex ].value,
                              cell.getValue() );
            }
        }
    }

    public static class Expected {

        public static Expected build( final boolean isMerged,
                                      final int mergedCellCount,
                                      final String value ) {
            return new Expected( isMerged,
                                 mergedCellCount,
                                 value );
        }

        private final boolean isMerged;
        private int mergedCellCount;
        private String value;

        private Expected( final boolean isMerged,
                          final int mergedCellCount,
                          final String value ) {
            this.isMerged = isMerged;
            this.mergedCellCount = mergedCellCount;
            this.value = value;
        }

    }

}
