package org.anstis.client.grid.model.basic;

import java.util.List;

import org.anstis.client.grid.model.basic.Grid;
import org.anstis.client.grid.model.basic.GridColumn;
import org.junit.Test;

import static org.junit.Assert.*;

public class GridIndexingTests {

    @Test
    public void testAddInitialColumns() {
        final Grid grid = new Grid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final List<Integer> indexAbsolute = grid.getRelativeToAbsoluteIndex();
        assertEquals( 2,
                      indexAbsolute.size() );
        assertEquals( 0,
                      (int) indexAbsolute.get( 0 ) );
        assertEquals( 1,
                      (int) indexAbsolute.get( 1 ) );

        final List<Integer> indexRelative = grid.getAbsoluteToRelativeIndex();
        assertEquals( 2,
                      indexRelative.size() );
        assertEquals( 0,
                      (int) indexRelative.get( 0 ) );
        assertEquals( 1,
                      (int) indexRelative.get( 1 ) );

        final List<GridColumn> definitions = grid.getColumnDefinitions();
        final List<GridColumn> columns = grid.getColumns();

        assertEquals( columns.get( 0 ),
                      definitions.get( 0 ) );
        assertEquals( columns.get( 1 ),
                      definitions.get( 1 ) );

        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc2 );
    }

    @Test
    public void testAddColumn() {
        final Grid grid = new Grid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        final GridColumn gc3 = new GridColumn( "col3",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( 1,
                        gc3 );

        final List<Integer> indexAbsolute = grid.getRelativeToAbsoluteIndex();
        assertEquals( 3,
                      indexAbsolute.size() );
        assertEquals( 0,
                      (int) indexAbsolute.get( 0 ) );
        assertEquals( 1,
                      (int) indexAbsolute.get( 1 ) );
        assertEquals( 2,
                      (int) indexAbsolute.get( 2 ) );

        final List<Integer> indexRelative = grid.getAbsoluteToRelativeIndex();
        assertEquals( 3,
                      indexRelative.size() );
        assertEquals( 0,
                      (int) indexRelative.get( 0 ) );
        assertEquals( 1,
                      (int) indexRelative.get( 1 ) );
        assertEquals( 2,
                      (int) indexRelative.get( 2 ) );

        final List<GridColumn> definitions = grid.getColumnDefinitions();
        final List<GridColumn> columns = grid.getColumns();

        assertEquals( columns.get( 0 ),
                      definitions.get( 0 ) );
        assertEquals( columns.get( 1 ),
                      definitions.get( 1 ) );
        assertEquals( columns.get( 2 ),
                      definitions.get( 2 ) );

        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc2 );
    }

    @Test
    public void testRemoveColumn() {
        final Grid grid = new Grid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        final GridColumn gc3 = new GridColumn( "col3",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );

        grid.removeColumn( gc2 );

        final List<Integer> indexAbsolute = grid.getRelativeToAbsoluteIndex();
        assertEquals( 2,
                      indexAbsolute.size() );
        assertEquals( 0,
                      (int) indexAbsolute.get( 0 ) );
        assertEquals( 1,
                      (int) indexAbsolute.get( 1 ) );

        final List<Integer> indexRelative = grid.getAbsoluteToRelativeIndex();
        assertEquals( 2,
                      indexRelative.size() );
        assertEquals( 0,
                      (int) indexRelative.get( 0 ) );
        assertEquals( 1,
                      (int) indexRelative.get( 1 ) );

        final List<GridColumn> definitions = grid.getColumnDefinitions();
        final List<GridColumn> columns = grid.getColumns();

        assertEquals( 2,
                      columns.size() );
        assertEquals( 2,
                      definitions.size() );
        assertEquals( columns.get( 0 ),
                      definitions.get( 0 ) );
        assertEquals( columns.get( 1 ),
                      definitions.get( 1 ) );

        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
    }

    @Test
    public void testMoveColumnToLeft() {
        final Grid grid = new Grid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        final GridColumn gc3 = new GridColumn( "col3",
                                               100 );
        final GridColumn gc4 = new GridColumn( "col4",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );
        grid.addColumn( gc4 );

        grid.moveColumnTo( 1,
                           gc4 );

        final List<Integer> indexAbsolute = grid.getRelativeToAbsoluteIndex();
        assertEquals( 4,
                      indexAbsolute.size() );
        assertEquals( 0,
                      (int) indexAbsolute.get( 0 ) );
        assertEquals( 3,
                      (int) indexAbsolute.get( 1 ) );
        assertEquals( 1,
                      (int) indexAbsolute.get( 2 ) );
        assertEquals( 2,
                      (int) indexAbsolute.get( 3 ) );

        final List<Integer> indexRelative = grid.getAbsoluteToRelativeIndex();
        assertEquals( 4,
                      indexRelative.size() );
        assertEquals( 0,
                      (int) indexRelative.get( 0 ) );
        assertEquals( 2,
                      (int) indexRelative.get( 1 ) );
        assertEquals( 3,
                      (int) indexRelative.get( 2 ) );
        assertEquals( 1,
                      (int) indexRelative.get( 3 ) );

        final List<GridColumn> definitions = grid.getColumnDefinitions();
        final List<GridColumn> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 4,
                      definitions.size() );
        assertEquals( columns.get( 0 ),
                      definitions.get( 0 ) );
        assertEquals( columns.get( 1 ),
                      definitions.get( 3 ) );
        assertEquals( columns.get( 2 ),
                      definitions.get( 1 ) );
        assertEquals( columns.get( 3 ),
                      definitions.get( 2 ) );

        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc4 );
        assertEquals( columns.get( 2 ),
                      gc2 );
        assertEquals( columns.get( 3 ),
                      gc3 );
    }

    @Test
    public void testMoveColumnToRight() {
        final Grid grid = new Grid();
        final GridColumn gc1 = new GridColumn( "col1",
                                               100 );
        final GridColumn gc2 = new GridColumn( "col2",
                                               100 );
        final GridColumn gc3 = new GridColumn( "col3",
                                               100 );
        final GridColumn gc4 = new GridColumn( "col4",
                                               100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );
        grid.addColumn( gc4 );

        grid.moveColumnTo( 3,
                           gc1 );

        final List<Integer> indexAbsolute = grid.getRelativeToAbsoluteIndex();
        assertEquals( 4,
                      indexAbsolute.size() );
        assertEquals( 1,
                      (int) indexAbsolute.get( 0 ) );
        assertEquals( 2,
                      (int) indexAbsolute.get( 1 ) );
        assertEquals( 3,
                      (int) indexAbsolute.get( 2 ) );
        assertEquals( 0,
                      (int) indexAbsolute.get( 3 ) );

        final List<Integer> indexRelative = grid.getAbsoluteToRelativeIndex();
        assertEquals( 4,
                      indexRelative.size() );
        assertEquals( 3,
                      (int) indexRelative.get( 0 ) );
        assertEquals( 0,
                      (int) indexRelative.get( 1 ) );
        assertEquals( 1,
                      (int) indexRelative.get( 2 ) );
        assertEquals( 2,
                      (int) indexRelative.get( 3 ) );

        final List<GridColumn> definitions = grid.getColumnDefinitions();
        final List<GridColumn> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 4,
                      definitions.size() );
        assertEquals( columns.get( 0 ),
                      definitions.get( 1 ) );
        assertEquals( columns.get( 1 ),
                      definitions.get( 2 ) );
        assertEquals( columns.get( 2 ),
                      definitions.get( 3 ) );
        assertEquals( columns.get( 3 ),
                      definitions.get( 0 ) );

        assertEquals( columns.get( 0 ),
                      gc2 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc4 );
        assertEquals( columns.get( 3 ),
                      gc1 );
    }

}
