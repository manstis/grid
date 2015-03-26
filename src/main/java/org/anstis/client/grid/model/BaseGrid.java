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
package org.anstis.client.grid.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseGrid<D extends IGridData<? extends IGridRow<?>, ? extends IGridCell>> implements IGrid<D> {

    protected List<Integer> indexRelativeToAbsolute = new ArrayList<>();
    protected List<Integer> indexAbsoluteToRelative = new ArrayList<>();

    protected List<GridColumn> columns = new ArrayList<>();

    protected D data;

    public List<GridColumn> getColumns() {
        final List<GridColumn> indexedColumns = new ArrayList<>( columns.size() );
        for ( int i : indexRelativeToAbsolute ) {
            indexedColumns.add( columns.get( i ) );
        }
        return Collections.unmodifiableList( indexedColumns );
    }

    public void addColumn( final GridColumn column ) {
        columns.add( column );
        indexRelativeToAbsolute.add( indexRelativeToAbsolute.size() );
        indexAbsoluteToRelative.add( indexAbsoluteToRelative.size() );
    }

    public void addColumn( final int index,
                           final GridColumn column ) {
        columns.add( index,
                     column );
        this.indexRelativeToAbsolute.add( index,
                                          columns.indexOf( column ) );
        this.indexAbsoluteToRelative.add( index,
                                          columns.indexOf( column ) );
        for ( int i = index + 1; i < this.indexRelativeToAbsolute.size(); i++ ) {
            this.indexRelativeToAbsolute.set( i,
                                              this.indexRelativeToAbsolute.get( i ) + 1 );
        }
        for ( int i = index + 1; i < this.indexAbsoluteToRelative.size(); i++ ) {
            this.indexAbsoluteToRelative.set( i,
                                              this.indexAbsoluteToRelative.get( i ) + 1 );
        }
    }

    public void removeColumn( final GridColumn column ) {
        final int indexColumn = columns.indexOf( column );
        final int indexColumnToRemove = indexAbsoluteToRelative.get( indexColumn );

        columns.remove( column );
        this.indexRelativeToAbsolute.remove( indexColumnToRemove );
        this.indexAbsoluteToRelative.remove( indexColumn );
        for ( int i = indexColumnToRemove; i < this.indexRelativeToAbsolute.size(); i++ ) {
            this.indexRelativeToAbsolute.set( i,
                                              this.indexRelativeToAbsolute.get( i ) - 1 );
        }
        for ( int i = indexColumn; i < this.indexAbsoluteToRelative.size(); i++ ) {
            this.indexAbsoluteToRelative.set( i,
                                              this.indexAbsoluteToRelative.get( i ) - 1 );
        }
    }

    public void moveColumnTo( final int index,
                              final GridColumn column ) {
        final int indexColumn = columns.indexOf( column );
        final int indexColumnToRemove = indexAbsoluteToRelative.get( indexColumn );

        if ( index == indexColumnToRemove ) {
            return;
        }

        this.indexRelativeToAbsolute.remove( indexColumnToRemove );
        this.indexRelativeToAbsolute.add( index,
                                          indexColumn );

        final Integer[] reverse = new Integer[ columns.size() ];
        for ( int i = 0; i < indexRelativeToAbsolute.size(); i++ ) {
            reverse[ indexRelativeToAbsolute.get( i ) ] = i;
        }
        this.indexAbsoluteToRelative = Arrays.asList( reverse );
    }

    public D getData() {
        return data;
    }

    public void setData( final D data ) {
        this.data = data;
    }

    public int mapToAbsoluteIndex( final int relativeIndex ) {
        return indexRelativeToAbsolute.get( relativeIndex );
    }

    public int mapToRelativeIndex( final int absoluteIndex ) {
        return indexAbsoluteToRelative.get( absoluteIndex );
    }

    public double getColumnOffset( final GridColumn gridColumn ) {
        final int columnIndex = getColumns().indexOf( gridColumn );
        return getColumnOffset( columnIndex );
    }

    public double getColumnOffset( final int columnIndex ) {
        double columnOffset = 0;
        final List<GridColumn> columns = getColumns();
        for ( int i = 0; i < columnIndex; i++ ) {
            final GridColumn column = columns.get( i );
            columnOffset = columnOffset + column.getWidth();
        }
        return columnOffset;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof BaseGrid ) ) {
            return false;
        }

        BaseGrid grid = (BaseGrid) o;

        if ( !columns.equals( grid.columns ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = columns.hashCode();
        result = ~~result;
        return result;
    }
}