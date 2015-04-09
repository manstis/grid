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

public class GridGroupingTests {

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
            final MergableGridRow row = data.getRow( rowIndex );
            assertFalse( row.isMerged() );
            assertFalse( row.isCollapsed() );
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final MergableGridCell cell = data.getCell( rowIndex,
                                                            columnIndex );
                assertFalse( cell.isGrouped() );
            }
        }

        assertEquals( 3,
                      data.getRowCount() );
    }

    @Test
    public void testGroup1() {
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

        //Group cells
        data.groupCell( 0,
                        0,
                        true );

        assertEquals( 3,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertFalse( data.getRow( 2 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isGrouped() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isGrouped() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );

        assertEquals( "(0, 0)",
                      data.getCell( 0,
                                    0 ).getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 1,
                                    0 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    0 ).getValue() );

        //Ungroup cells
        data.groupCell( 0,
                        0,
                        false );

        assertEquals( 3,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

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

        assertEquals( "(0, 0)",
                      data.getCell( 0,
                                    0 ).getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 1,
                                    0 ).getValue() );
        assertEquals( "(0, 2)",
                      data.getCell( 2,
                                    0 ).getValue() );
    }

    @Test
    public void testGroupOverlap1() {
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
                      "g1" );
        data.setCell( 1,
                      0,
                      "g1" );
        data.setCell( 2,
                      0,
                      "g1" );
        data.setCell( 3,
                      0,
                      "g1" );
        data.setCell( 4,
                      0,
                      "g1" );

        data.setCell( 1,
                      1,
                      "g2" );
        data.setCell( 2,
                      1,
                      "g2" );
        data.setCell( 3,
                      1,
                      "g2" );

        //Group g2
        data.groupCell( 1,
                        1,
                        true );
        assertGroupOverlap1_g2( data );

        //Group g1
        data.groupCell( 0,
                        0,
                        true );
        assertGroupOverlap1_g1( data );

        //Ungroup g1
        data.groupCell( 0,
                        0,
                        false );
        assertGroupOverlap1_g2( data );
    }

    private void assertGroupOverlap1_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertTrue( data.getRow( 3 ).isGrouped() );
        assertTrue( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 4,
                                  0 ).isGrouped() );

        assertFalse( data.getCell( 0,
                                   1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 5,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 3,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    private void assertGroupOverlap1_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertTrue( data.getRow( 3 ).isGrouped() );
        assertFalse( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   0 ).isGrouped() );

        assertFalse( data.getCell( 0,
                                   1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 3,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    @Test
    public void testGroupOverlap2() {
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
                      "g1" );
        data.setCell( 3,
                      0,
                      "g1" );
        data.setCell( 4,
                      0,
                      "g1" );

        data.setCell( 0,
                      1,
                      "g2" );
        data.setCell( 1,
                      1,
                      "g2" );
        data.setCell( 2,
                      1,
                      "g2" );

        //Group g2
        data.groupCell( 0,
                        1,
                        true );
        assertGroupOverlap2_g2( data );

        //Group g1
        data.groupCell( 2,
                        0,
                        true );
        assertGroupOverlap2_g1( data );

        //Ungroup g1
        data.groupCell( 2,
                        0,
                        false );
        assertGroupOverlap2_g2( data );
    }

    private void assertGroupOverlap2_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertTrue( data.getRow( 3 ).isGrouped() );
        assertTrue( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 4,
                                  0 ).isGrouped() );

        assertTrue( data.getCell( 0,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    private void assertGroupOverlap2_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertFalse( data.getRow( 3 ).isGrouped() );
        assertFalse( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   0 ).isGrouped() );

        assertTrue( data.getCell( 0,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    @Test
    public void testGroupOverlap3() {
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
                      "g1" );
        data.setCell( 3,
                      0,
                      "g1" );
        data.setCell( 4,
                      0,
                      "g1" );

        data.setCell( 0,
                      1,
                      "g2" );
        data.setCell( 1,
                      1,
                      "g2" );
        data.setCell( 2,
                      1,
                      "g2" );

        //Group g2
        data.groupCell( 0,
                        1,
                        true );
        assertGroupOverlap3_g2( data );

        //Group g1
        data.groupCell( 2,
                        0,
                        true );
        assertGroupOverlap3_g1( data );

        //Ungroup g2
        data.groupCell( 0,
                        1,
                        false );
        assertGroupOverlap3_g3( data );
    }

    private void assertGroupOverlap3_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertTrue( data.getRow( 3 ).isGrouped() );
        assertTrue( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 4,
                                  0 ).isGrouped() );

        assertTrue( data.getCell( 0,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    private void assertGroupOverlap3_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertFalse( data.getRow( 3 ).isGrouped() );
        assertFalse( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   0 ).isGrouped() );

        assertTrue( data.getCell( 0,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 1,
                                  1 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  1 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    private void assertGroupOverlap3_g3( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isGrouped() );
        assertFalse( data.getRow( 1 ).isGrouped() );
        assertTrue( data.getRow( 2 ).isGrouped() );
        assertTrue( data.getRow( 3 ).isGrouped() );
        assertTrue( data.getRow( 4 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertTrue( data.getCell( 2,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 3,
                                  0 ).isGrouped() );
        assertTrue( data.getCell( 4,
                                  0 ).isGrouped() );

        assertFalse( data.getCell( 0,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 1,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 2,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 3,
                                   1 ).isGrouped() );
        assertFalse( data.getCell( 4,
                                   1 ).isGrouped() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getGroupedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getGroupedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getGroupedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getGroupedCellCount() );
    }

    @Test
    public void testGroupUpdateCellValue() {
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

        //Group cells
        data.groupCell( 0,
                        0,
                        true );
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

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertFalse( data.getRow( 2 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        //Update cell value
        data.setCell( 0,
                      0,
                      "<changed>" );

        //Ungroup cells
        data.groupCell( 0,
                        0,
                        false );
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
        assertFalse( data.getRow( 0 ).isGrouped() );
        assertFalse( data.getRow( 1 ).isGrouped() );
        assertFalse( data.getRow( 2 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 0,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );

        assertFalse( data.getCell( 1,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
    }

    @Test
    public void testGroupMovedColumnUpdateCellValue() {
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

        //Group cells
        data.groupCell( 0,
                        0,
                        true );
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

        assertTrue( data.getRow( 0 ).isGrouped() );
        assertTrue( data.getRow( 1 ).isGrouped() );
        assertFalse( data.getRow( 2 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        //Move column
        data.moveColumnTo( 1,
                           gc1 );

        //Update cell value
        data.setCell( 0,
                      1,
                      "<changed>" );

        //Ungroup cells
        data.groupCell( 0,
                        1,
                        false );
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
        assertFalse( data.getRow( 0 ).isGrouped() );
        assertFalse( data.getRow( 1 ).isGrouped() );
        assertFalse( data.getRow( 2 ).isGrouped() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getGroupedCellCount() );

        assertFalse( data.getCell( 0,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getGroupedCellCount() );

        assertFalse( data.getCell( 1,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getGroupedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isGrouped() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getGroupedCellCount() );
    }

}