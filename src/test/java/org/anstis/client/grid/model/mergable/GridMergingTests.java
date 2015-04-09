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

import org.junit.Test;

import static org.junit.Assert.*;

public class GridMergingTests {

    @Test
    public void testInitialSetup1() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            assertFalse( data.getRow( rowIndex ).isMerged() );
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final MergableGridCell cell = data.getCell( rowIndex,
                                                            columnIndex );
                assertFalse( cell.isMerged() );
                assertEquals( 1,
                              cell.getMergedCellCount() );
            }
        }
    }

    @Test
    public void testInitialSetup2() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + ( columnIndex == 0 ? "X" : rowIndex ) + ")" );
            }
        }

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getCell( 0,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getMergedCellCount() );

        assertFalse( data.getCell( 1,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getMergedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNext1() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 0)" );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 2 ).isMerged() );
        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNext2() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 2,
                      0,
                      "(0, 1)" );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNext3() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 0)" );
        data.setCell( 2,
                      0,
                      "(0, 0)" );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious1() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 0,
                      0,
                      "(0, 1)" );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 2 ).isMerged() );
        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious2() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 2)" );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious3() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 2)" );

        data.setCell( 0,
                      0,
                      "(0, 2)" );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNonSequential() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 0,
                      0,
                      "(a, b)" );

        data.setCell( 2,
                      0,
                      "(a, b)" );

        data.setCell( 1,
                      0,
                      "(a, b)" );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergedUpdateCellValue() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 0)" );
        assertEquals( 3,
                      data.getRowCount() );
        assertEquals( "(0, 0)",
                      data.getCell( 0,
                                    0 ).getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 1,
                                    0 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    0 ).getValue() );

        //Update cell value
        data.setCell( 0,
                      0,
                      "<changed>" );
        assertEquals( 3,
                      data.getRowCount() );
        assertEquals( "<changed>",
                      data.getCell( 0,
                                    0 ).getValue() );
        assertEquals( "<changed>",
                      data.getCell( 1,
                                    0 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    0 ).getValue() );

        //Check flags
        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getCell( 0,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getMergedCellCount() );

        assertFalse( data.getCell( 1,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getMergedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getMergedCellCount() );
    }

    @Test
    public void testMergedMovedColumnUpdateCellValue() {
        final MergableGridData data = new MergableGridData();
        final MergableGridColumn gc1 = new MergableGridColumn( "col1",
                                                               100 );
        final MergableGridColumn gc2 = new MergableGridColumn( "col2",
                                                               100 );
        data.addColumn( gc1 );
        data.addColumn( gc2 );

        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );
        data.addRow( new MergableGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              "(" + columnIndex + ", " + rowIndex + ")" );
            }
        }

        data.setCell( 1,
                      0,
                      "(0, 0)" );
        assertEquals( 3,
                      data.getRowCount() );
        assertEquals( "(0, 0)",
                      data.getCell( 0,
                                    0 ).getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 1,
                                    0 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    0 ).getValue() );

        //Move column
        data.moveColumnTo( 1,
                           gc1 );

        //Update cell value
        data.setCell( 0,
                      1,
                      "<changed>" );
        assertEquals( 3,
                      data.getRowCount() );
        assertEquals( "<changed>",
                      data.getCell( 0,
                                    1 ).getValue() );
        assertEquals( "<changed>",
                      data.getCell( 1,
                                    1 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    1 ).getValue() );

        //Check flags
        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    1 ).getMergedCellCount() );

        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getMergedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getMergedCellCount() );
    }

}
