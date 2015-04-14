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

        updateMergeMetaData( rowIndex,
                             _columnIndex );
    }

    @Override
    public void collapseCell( final int rowIndex,
                              final int columnIndex ) {
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final MergableGridRow row = rows.get( rowIndex );
        final MergableGridCell cell = row.getCells().get( _columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( !cell.isMerged() ) {
            return;
        }
        updateCollapseMetaData( rowIndex,
                                _columnIndex,
                                true );
    }

    @Override
    public void expandCell( final int rowIndex,
                            final int columnIndex ) {
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final MergableGridRow row = rows.get( rowIndex );
        final MergableGridCell cell = row.getCells().get( _columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( !cell.isCollapsed() ) {
            return;
        }
        updateCollapseMetaData( rowIndex,
                                _columnIndex,
                                false );
    }

    private void updateMergeMetaData( final int rowIndex,
                                      final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        if ( currentRowCell != null ) {
            if ( !currentRowCell.isCollapsed() ) {
                currentRowCell.setMergedCellCount( 0 );
            }
        }
        updateRowMergedCells( currentRow );

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                updateRowMergedCells( previousRow );
                break;
            }
            if ( previousRowCell.getCollapsedCellCount() == 0 ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            previousRowCell.setMergedCellCount( 0 );
            updateRowMergedCells( previousRow );
            minRowIndex--;
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                updateRowMergedCells( nextRow );
                break;
            }
            if ( nextRowCell.getCollapsedCellCount() > 1 ) {
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            nextRowCell.setMergedCellCount( 0 );
            updateRowMergedCells( nextRow );
            maxRowIndex++;
        }

        final MergableGridRow row = rows.get( minRowIndex );
        row.getCells().get( columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );
        updateRowMergedCells( row );
    }

    private void updateRowMergedCells( final MergableGridRow row ) {
        for ( MergableGridCell cell : row.getCells().values() ) {
            if ( cell.isMerged() ) {
                row.setHasMergedCells( true );
                return;
            }
        }
        row.setHasMergedCells( false );
    }

    private void updateCollapseMetaData( final int rowIndex,
                                         final int columnIndex,
                                         final boolean isCollapsed ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );
        final int collapsedCellCount = isCollapsed ? 0 : 1;

        if ( currentRowCell != null ) {
            currentRowCell.setCollapsedCellCount( collapsedCellCount );
        }

        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( previousRowCell.getCollapsedCellCount() == 0 || previousRowCell.getMergedCellCount() == 0 ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            previousRowCell.setCollapsedCellCount( collapsedCellCount );
            minRowIndex--;
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.getCollapsedCellCount() > 1 || nextRowCell.getMergedCellCount() > 1 ) {
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            nextRowCell.setCollapsedCellCount( collapsedCellCount );
            maxRowIndex++;
        }

        if ( isCollapsed ) {
            final MergableGridRow row = rows.get( minRowIndex );
            row.getCells().get( columnIndex ).setCollapsedCellCount( maxRowIndex - minRowIndex );

            for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
                rows.get( i ).increaseCollapseLevel();
            }

        } else {
            for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
                rows.get( i ).decreaseCollapseLevel();
            }
            updateMergeMetaData( rowIndex,
                                 columnIndex );
        }
    }

}
