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

public class GridMergingTests extends BaseGridTests {

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

        assertGridIndexes( data,
                           new boolean[]{ true, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "(0, X)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, X)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(0, X)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 2, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 0)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( false, 1, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ false, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 2, "(0, 1)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(0, 1)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 0)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(0, 0)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 2, "(0, 1)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 1)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( false, 1, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ false, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 2, "(0, 2)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "(0, 2)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 2)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, true },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "(a, b)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(a, b)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( true, 0, "(a, b)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 2, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 0)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( false, 1, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );

        //Update cell value
        data.setCell( 0,
                      0,
                      "<changed>" );

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 2, "<changed>" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "<changed>" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( false, 1, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );
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

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 2, "(0, 0)" ), Expected.build( false, 1, "(1, 0)" ) },
                                   { Expected.build( true, 0, "(0, 0)" ), Expected.build( false, 1, "(1, 1)" ) },
                                   { Expected.build( false, 1, "(0, 2)" ), Expected.build( false, 1, "(1, 2)" ) },
                           } );

        //Move column
        data.moveColumnTo( 1,
                           gc1 );

        //Update cell value
        data.setCell( 0,
                      1,
                      "<changed>" );

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "(1, 0)" ), Expected.build( true, 2, "<changed>" ) },
                                   { Expected.build( false, 1, "(1, 1)" ), Expected.build( true, 0, "<changed>" ) },
                                   { Expected.build( false, 1, "(1, 2)" ), Expected.build( false, 1, "(0, 2)" ) },
                           } );
    }

}
