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
package org.anstis.client.grid.model.basic;

import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import com.google.gwt.core.client.Callback;
import org.anstis.client.grid.model.IGridCellValue;
import org.anstis.client.grid.widget.context.GridCellRenderContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class GridIndexingTests {

    @Test
    public void testAddInitialColumns() {
        final GridData grid = new GridData();
        final GridColumn<String> gc1 = new MockGridColumn<>( "col1",
                                                             100 );
        final GridColumn<String> gc2 = new MockGridColumn<>( "col2",
                                                             100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );

        final List<GridColumn<?>> columns = grid.getColumns();

        assertEquals( 2,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc2 );
    }

    @Test
    public void testAddColumn() {
        final GridData grid = new GridData();
        final GridColumn<String> gc1 = new MockGridColumn<>( "col1",
                                                             100 );
        final GridColumn<String> gc2 = new MockGridColumn<>( "col2",
                                                             100 );
        final GridColumn<String> gc3 = new MockGridColumn<>( "col3",
                                                             100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( 1,
                        gc3 );

        final List<GridColumn<?>> columns = grid.getColumns();

        assertEquals( 3,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc2 );
    }

    @Test
    public void testRemoveColumn() {
        final GridData grid = new GridData();
        final GridColumn<String> gc1 = new MockGridColumn<>( "col1",
                                                             100 );
        final GridColumn<String> gc2 = new MockGridColumn<>( "col2",
                                                             100 );
        final GridColumn<String> gc3 = new MockGridColumn<>( "col3",
                                                             100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );

        grid.removeColumn( gc2 );

        final List<GridColumn<?>> columns = grid.getColumns();

        assertEquals( 2,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
    }

    @Test
    public void testMoveColumnToLeft() {
        final GridData grid = new GridData();
        final GridColumn<String> gc1 = new MockGridColumn<>( "col1",
                                                             100 );
        final GridColumn<String> gc2 = new MockGridColumn<>( "col2",
                                                             100 );
        final GridColumn<String> gc3 = new MockGridColumn<>( "col3",
                                                             100 );
        final GridColumn<String> gc4 = new MockGridColumn<>( "col4",
                                                             100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );
        grid.addColumn( gc4 );

        grid.moveColumnTo( 1,
                           gc4 );

        final List<GridColumn<?>> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( 3,
                      gc4.getIndex() );
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
        final GridData grid = new GridData();
        final GridColumn<String> gc1 = new MockGridColumn<>( "col1",
                                                             100 );
        final GridColumn<String> gc2 = new MockGridColumn<>( "col2",
                                                             100 );
        final GridColumn<String> gc3 = new MockGridColumn<>( "col3",
                                                             100 );
        final GridColumn<String> gc4 = new MockGridColumn<>( "col4",
                                                             100 );
        grid.addColumn( gc1 );
        grid.addColumn( gc2 );
        grid.addColumn( gc3 );
        grid.addColumn( gc4 );

        grid.moveColumnTo( 3,
                           gc1 );

        final List<GridColumn<?>> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( 3,
                      gc4.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc2 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc4 );
        assertEquals( columns.get( 3 ),
                      gc1 );
    }

    static class MockGridColumn<T> extends GridColumn<T> {

        MockGridColumn( final String title,
                        final int width ) {
            super( title,
                   width );
        }

        @Override
        public void renderCell( final Group g,
                                final GridCell<T> cell,
                                final GridCellRenderContext context ) {
            //Do nothing
        }

        @Override
        public void edit( final IGridCellValue<T> value,
                          final Callback<IGridCellValue<T>, IGridCellValue<T>> callback ) {
            //Do nothing
        }

    }

}
