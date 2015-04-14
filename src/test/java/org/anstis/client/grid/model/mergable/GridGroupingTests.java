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
                assertFalse( cell.isMerged() );
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
        data.collapseCell( 0,
                           0 );

        assertEquals( 3,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

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
        data.expandCell( 0,
                         0 );

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
    public void testGroupNotCombineWhenCellsValuesUpdatedAbove() {
        //Tests that cells with the same value do not combine into existing collapsed blocks
        //Test #1 - Update cells above the existing collapsed block
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

        // [ (0,0) (1,0) ]
        // [ (0,1) (1,1) ]
        // [ (0,2) (1,2) ]
        // [ (0,2) (1,3) ]
        // [ (0,4) (1,4) ]

        data.setCell( 3,
                      0,
                      "(0, 2)" );

        //Group cells
        data.collapseCell( 2,
                           0 );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertFalse( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Set cell above existing block (should not affect existing block)
        data.setCell( 1,
                      0,
                      "(0, 2)" );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertFalse( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Set cell above existing block (should create a new block)
        data.setCell( 0,
                      0,
                      "(0, 2)" );

        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Ungroup cell (should result in a single block spanning 4 rows)
        data.expandCell( 2,
                         0 );

        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 4,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );
    }

    @Test
    public void testGroupNotCombineWhenCellsValuesUpdatedBelow() {
        //Tests that cells with the same value do not combine into existing collapsed blocks
        //Test #2 - Update cells below the existing collapsed block
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

        // [ (0,0) (1,0) ]
        // [ (0,1) (1,1) ]
        // [ (0,1) (1,2) ]
        // [ (0,3) (1,3) ]
        // [ (0,4) (1,4) ]

        data.setCell( 2,
                      0,
                      "(0, 1)" );

        //Group cells
        data.collapseCell( 1,
                           0 );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertFalse( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 3,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Set cell below existing block (should not affect existing block)
        data.setCell( 3,
                      0,
                      "(0, 1)" );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertFalse( data.getRow( 3 ).isMerged() );
        assertFalse( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 3,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 4,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Set cell below existing block (should create a new block)
        data.setCell( 4,
                      0,
                      "(0, 1)" );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        //Ungroup cell (should result in a single block spanning 4 rows)
        data.expandCell( 1,
                         0 );

        assertEquals( 5,
                      data.getRowCount() );

        assertFalse( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 4,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getMergedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );
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
        data.collapseCell( 1,
                           1 );
        assertGroupOverlap1_g2( data );

        //Group g1
        data.collapseCell( 0,
                           0 );
        assertGroupOverlap1_g1( data );

        //Ungroup g1
        data.expandCell( 0,
                         0 );
        assertGroupOverlap1_g2( data );
    }

    private void assertGroupOverlap1_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertFalse( data.getCell( 0,
                                   1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 5,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 3,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
    }

    private void assertGroupOverlap1_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertFalse( data.getCell( 0,
                                   1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 3,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
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
        data.collapseCell( 0,
                           1 );
        assertGroupOverlap2_g2( data );

        //Group g1
        data.collapseCell( 2,
                           0 );
        assertGroupOverlap2_g1( data );

        //Ungroup g1
        data.expandCell( 2,
                         0 );
        assertGroupOverlap2_g2( data );
    }

    private void assertGroupOverlap2_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 3,
                                   1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
    }

    private void assertGroupOverlap2_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 3,
                                   1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
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
        data.collapseCell( 0,
                           1 );
        assertGroupOverlap3_g2( data );

        //Group g1
        data.collapseCell( 2,
                           0 );
        assertGroupOverlap3_g1( data );

        //Ungroup g2
        data.expandCell( 0,
                         1 );
        assertGroupOverlap3_g3( data );
    }

    private void assertGroupOverlap3_g1( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 3,
                                   1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
    }

    private void assertGroupOverlap3_g2( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertTrue( data.getRow( 2 ).isCollapsed() );
        assertFalse( data.getRow( 3 ).isCollapsed() );
        assertFalse( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 3,
                                   1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 3,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
    }

    private void assertGroupOverlap3_g3( final MergableGridData data ) {
        assertEquals( 5,
                      data.getRowCount() );

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertTrue( data.getRow( 2 ).isMerged() );
        assertTrue( data.getRow( 3 ).isMerged() );
        assertTrue( data.getRow( 4 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );
        assertTrue( data.getRow( 3 ).isCollapsed() );
        assertTrue( data.getRow( 4 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 3,
                                  0 ).isMerged() );
        assertTrue( data.getCell( 4,
                                  0 ).isMerged() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertTrue( data.getCell( 2,
                                  1 ).isMerged() );
        assertFalse( data.getCell( 3,
                                   1 ).isMerged() );
        assertFalse( data.getCell( 4,
                                   1 ).isMerged() );

        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 3,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 3,
                                    0 ).getCollapsedCellCount() );
        assertEquals( 0,
                      data.getCell( 4,
                                    0 ).getCollapsedCellCount() );

        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 3,
                                    1 ).getCollapsedCellCount() );
        assertEquals( 1,
                      data.getCell( 4,
                                    1 ).getCollapsedCellCount() );
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
        data.collapseCell( 0,
                           0 );
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

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertTrue( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        //Update cell value
        data.setCell( 0,
                      0,
                      "<changed>" );

        //Ungroup cells
        data.expandCell( 0,
                         0 );
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

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertTrue( data.getCell( 0,
                                  0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 0,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 1,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
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
        data.collapseCell( 0,
                           0 );
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

        assertTrue( data.getRow( 0 ).isMerged() );
        assertTrue( data.getRow( 1 ).isMerged() );
        assertFalse( data.getRow( 2 ).isMerged() );

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
        data.expandCell( 0,
                         1 );
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

        assertFalse( data.getRow( 0 ).isCollapsed() );
        assertFalse( data.getRow( 1 ).isCollapsed() );
        assertFalse( data.getRow( 2 ).isCollapsed() );

        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    0 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 0,
                                  1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    1 ).getCollapsedCellCount() );

        assertTrue( data.getCell( 1,
                                  1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 1,
                                    1 ).getCollapsedCellCount() );

        assertFalse( data.getCell( 2,
                                   1 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    1 ).getCollapsedCellCount() );
    }

}