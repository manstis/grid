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

public class MergableGridData extends BaseGridData<MergableGridRow, MergableGridColumn, MergableGridCell> implements IMergableGridData {

    @Override
    public void setCell( final int rowIndex,
                         final int columnIndex,
                         final String value ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return;
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return;
        }

        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = getRow( rowIndex );

        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final MergableGridCell currentRowCell = currentRow.getCells().get( _columnIndex );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( _columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            minRowIndex--;
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( _columnIndex );
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
            row.setCell( _columnIndex,
                         value );
        }

        assertMerging( rowIndex,
                       _columnIndex );
    }

    @Override
    public void groupCell( final int rowIndex,
                           final int columnIndex,
                           final boolean isGrouped ) {
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final MergableGridRow row = rows.get( rowIndex );
        final MergableGridCell cell = row.getCells().get( _columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( !cell.isMerged() ) {
            return;
        }
        assertGrouping( rowIndex,
                        _columnIndex,
                        isGrouped );
    }

    private void assertMerging( final int rowIndex,
                                final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        if ( currentRowCell != null ) {
            if ( !currentRowCell.isGrouped() ) {
                currentRowCell.setMergedCellCount( 0 );
            }
        }
        assertRowMergedCells( currentRow );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
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

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
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

        final MergableGridRow row = rows.get( minRowIndex );
        row.getCells().get( columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );
        assertRowMergedCells( row );
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

    private void assertGrouping( final int rowIndex,
                                 final int columnIndex,
                                 final boolean isGrouped ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        final int groupedCellCount = isGrouped ? 0 : 1;

        if ( currentRowCell != null ) {
            currentRowCell.setGroupedCellCount( groupedCellCount );
        }
        assertRowGroupedCells( currentRow );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                assertRowGroupedCells( previousRow );
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            previousRowCell.setGroupedCellCount( groupedCellCount );
            assertRowGroupedCells( previousRow );
            minRowIndex--;
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                assertRowGroupedCells( nextRow );
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            nextRowCell.setGroupedCellCount( groupedCellCount );
            assertRowGroupedCells( nextRow );
            maxRowIndex++;
        }

        if ( isGrouped ) {
            final MergableGridRow row = rows.get( minRowIndex );
            row.getCells().get( columnIndex ).setGroupedCellCount( maxRowIndex - minRowIndex );
            assertRowGroupedCells( row );

            for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
                rows.get( i ).increaseCollapseLevel();
            }

        } else {
            for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
                rows.get( i ).decreaseCollapseLevel();
            }
        }
    }

    private void assertRowGroupedCells( final MergableGridRow row ) {
        for ( MergableGridCell cell : row.getCells().values() ) {
            if ( cell.isGrouped() ) {
                row.setHasGroupedCells( true );
                return;
            }
        }
        row.setHasGroupedCells( false );
    }

}
