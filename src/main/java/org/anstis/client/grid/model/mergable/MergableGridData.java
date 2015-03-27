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
    public void setCell( final int rowIndex,
                         final int columnIndex,
                         final String value ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return;
        }

        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = getRow( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = getRow( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            minRowIndex--;
        }

        while ( maxRowIndex < getRowCount() ) {
            final MergableGridRow nextRow = getRow( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            maxRowIndex++;
        }

        for ( int i = minRowIndex; i < maxRowIndex; i++ ) {
            final MergableGridRow row = rows.get( i );
            row.setCell( columnIndex,
                         new MergableGridCell( value ) );
        }

        assertMerging( rowIndex,
                       columnIndex );
    }

    private void assertMerging( final int rowIndex,
                                final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = getRow( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        if ( currentRowCell != null ) {
            currentRowCell.setMergedCellCount( 0 );
        }
        assertRowMergedCells( currentRow );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = getRow( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                assertRowMergedCells( previousRow );
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            previousRowCell.setMergedCellCount( 0 );
            assertRowMergedCells( previousRow );
            minRowIndex--;
        }

        while ( maxRowIndex < getRowCount() ) {
            final MergableGridRow nextRow = getRow( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                assertRowMergedCells( nextRow );
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            nextRowCell.setMergedCellCount( 0 );
            assertRowMergedCells( nextRow );
            maxRowIndex++;
        }

        getCell( minRowIndex,
                 columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );
        assertRowMergedCells( getRow( minRowIndex ) );
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

}
