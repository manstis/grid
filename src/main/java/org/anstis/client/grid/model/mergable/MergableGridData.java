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

import org.anstis.client.grid.model.BaseGridData;

public class MergableGridData extends BaseGridData<MergableGridRow, MergableGridCell> {

    @Override
    public MergableGridCell newCell( final String value ) {
        return new MergableGridCell( value );
    }

    @Override
    public void setCell( final int rowIndex,
                         final int columnIndex,
                         final MergableGridCell cell ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return;
        }
        final MergableGridRow row = rows.get( rowIndex );
        row.setCell( columnIndex,
                     cell );
        assertMergeUpwards( rowIndex,
                            columnIndex );
        assertMergeDownwards( rowIndex,
                              columnIndex );
        assertMergedCellCount( rowIndex,
                               columnIndex );
    }

    private void assertMergeUpwards( final int rowIndex,
                                     final int columnIndex ) {
        if ( rowIndex <= 0 ) {
            return;
        }
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridRow previousRow = rows.get( rowIndex - 1 );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
        if ( currentRowCell == null ) {
            return;
        }
        currentRowCell.setMerged( previousRowCell == null ? false : currentRowCell.getValue().equals( previousRowCell.getValue() ) );
        assertRowMergedCells( currentRow );
        assertMergeUpwards( rowIndex - 1,
                            columnIndex );
    }

    private void assertMergeDownwards( final int rowIndex,
                                       final int columnIndex ) {
        if ( rowIndex >= rows.size() - 1 ) {
            return;
        }
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridRow nextRow = rows.get( rowIndex + 1 );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
        if ( nextRowCell == null ) {
            return;
        }
        nextRowCell.setMerged( currentRowCell == null ? nextRowCell.isMerged() : nextRowCell.getValue().equals( currentRowCell.getValue() ) );
        assertRowMergedCells( currentRow );
        assertMergeUpwards( rowIndex + 1,
                            columnIndex );
    }

    private void assertRowMergedCells( final MergableGridRow row ) {
        for ( MergableGridCell cell : row.getCells().values() ) {
            if ( cell.isMerged() ) {
                row.setHasMergedCells( true );
                return;
            }
        }
        row.setHasMergedCells( false );
    }

    private void assertMergedCellCount( final int rowIndex,
                                        final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex;
        MergableGridCell cell = getCell( rowIndex,
                                         columnIndex );
        if ( cell.isMerged() ) {
            while ( minRowIndex > 0 && cell != null && cell.isMerged() ) {
                minRowIndex--;
                cell.setMergedCellCount( 0 );
                cell = getCell( minRowIndex,
                                columnIndex );
            }
            do {
                maxRowIndex++;
                cell.setMergedCellCount( 0 );
            }
            while ( maxRowIndex < getRowCount() && ( cell = getCell( maxRowIndex,
                                                                     columnIndex ) ) != null && cell.isMerged() );
            getCell( minRowIndex,
                     columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );

        } else {
            if ( ( cell = getCell( maxRowIndex + 1,
                                   columnIndex ) ) != null && cell.isMerged() ) {
                do {
                    maxRowIndex++;
                    cell.setMergedCellCount( 0 );
                }
                while ( maxRowIndex < getRowCount() && ( cell = getCell( maxRowIndex,
                                                                         columnIndex ) ) != null && cell.isMerged() );
                getCell( minRowIndex,
                         columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );
            }
        }
    }

}
