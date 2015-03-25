package org.anstis.client.grid.model.mergable;

import java.util.ArrayList;
import java.util.List;

import org.anstis.client.grid.model.basic.GridColumn;
import org.anstis.client.grid.model.mergable.MergableGrid;
import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.model.mergable.MergableGridRow;
import org.junit.Test;

import static org.junit.Assert.*;

public class GridMergingTests {

    @Test
    public void testInitialSetup() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            assertFalse( data.getRow( rowIndex ).hasMergedCells() );
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                final MergableGridCell cell = data.getCell( rowIndex,
                                                            columnIndex );
                assertFalse( cell.isMerged() );
                assertEquals( 1,
                              cell.getMergedCellCount() );
            }
        }
    }

    @Test
    public void testMergeNext1() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new MergableGridCell( "(0, 0)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).hasMergedCells() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 2 ).hasMergedCells() );
        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNext2() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 2,
                      0,
                      new MergableGridCell( "(0, 1)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 1 ).hasMergedCells() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).hasMergedCells() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNext3() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new MergableGridCell( "(0, 0)" ) );
        data.setCell( 2,
                      0,
                      new MergableGridCell( "(0, 0)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).hasMergedCells() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).hasMergedCells() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious1() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 0,
                      0,
                      new MergableGridCell( "(0, 1)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).hasMergedCells() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 2 ).hasMergedCells() );
        assertFalse( data.getCell( 2,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious2() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new MergableGridCell( "(0, 2)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 1,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertFalse( data.getRow( 1 ).hasMergedCells() );
        assertFalse( data.getCell( 1,
                                   0 ).isMerged() );
        assertEquals( 2,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).hasMergedCells() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergePrevious3() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new MergableGridCell( "(0, 2)" ) );

        data.setCell( 0,
                      0,
                      new MergableGridCell( "(0, 2)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).hasMergedCells() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).hasMergedCells() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

    @Test
    public void testMergeNonSequential() {
        final MergableGrid grid = new MergableGrid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final MergableGridData data = new MergableGridData();
        final List<MergableGridRow> rows = new ArrayList<>();
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        rows.add( new MergableGridRow() );
        data.setRows( rows );
        grid.setData( data );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < grid.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new MergableGridCell( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 0,
                      0,
                      new MergableGridCell( "(a, b)" ) );

        data.setCell( 2,
                      0,
                      new MergableGridCell( "(a, b)" ) );

        data.setCell( 1,
                      0,
                      new MergableGridCell( "(a, b)" ) );

        assertFalse( data.getRow( 0 ).hasMergedCells() );
        assertFalse( data.getCell( 0,
                                   0 ).isMerged() );
        assertEquals( 3,
                      data.getCell( 0,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 1 ).hasMergedCells() );
        assertTrue( data.getCell( 1,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 1,
                                    0 ).getMergedCellCount() );

        assertTrue( data.getRow( 2 ).hasMergedCells() );
        assertTrue( data.getCell( 2,
                                  0 ).isMerged() );
        assertEquals( 0,
                      data.getCell( 2,
                                    0 ).getMergedCellCount() );
    }

}
