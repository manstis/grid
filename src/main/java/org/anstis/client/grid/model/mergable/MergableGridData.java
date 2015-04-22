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

        //Find cell's current value
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final int _columnIndex = columns.get( columnIndex ).getIndex();

        final MergableGridRow currentRow = getRow( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( _columnIndex );

        //Find minimum row with a cell containing the same value as that being updated
        boolean foundTopSplitMarker = currentRowCell == null ? false : currentRowCell.getMergedCellCount() > 0;
        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( _columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( previousRowCell.isCollapsed() && foundTopSplitMarker ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( previousRowCell.getMergedCellCount() > 0 ) {
                foundTopSplitMarker = true;
            }
            minRowIndex--;
        }

        //Find maximum row with a cell containing the same value as that being updated
        boolean foundBottomSplitMarker = false;
        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( _columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                maxRowIndex--;
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( nextRowCell.getMergedCellCount() > 0 ) {
                foundBottomSplitMarker = true;
            }
            maxRowIndex++;
        }

        //Update all rows' value
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
        updateCollapseMetaDataOnCollapse( rowIndex,
                                          _columnIndex );
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
        updateCollapseMetaDataOnExpand( rowIndex,
                                        _columnIndex );
    }

    private void updateMergeMetaData( final int rowIndex,
                                      final int columnIndex ) {
        //Find cell's current value
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = getRow( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );

        if ( currentRowCell == null ) {
            return;
        }

        //Find minimum row with a cell containing the same value as that being updated
        boolean foundTopSplitMarker = currentRowCell.getMergedCellCount() > 0;
        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( previousRowCell.isCollapsed() && foundTopSplitMarker ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( previousRowCell.getMergedCellCount() > 0 ) {
                foundTopSplitMarker = true;
            }
            minRowIndex--;
        }

        //Find maximum row with a cell containing the same value as that being updated
        boolean foundBottomSplitMarker = false;
        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                maxRowIndex--;
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( nextRowCell.getMergedCellCount() > 0 ) {
                foundBottomSplitMarker = true;
            }
            maxRowIndex++;
        }

        //Update merge meta-data
        for ( int i = minRowIndex; i < maxRowIndex; i++ ) {
            final MergableGridRow row = rows.get( i );
            row.getCells().get( columnIndex ).setMergedCellCount( 0 );
            updateRowMergedCells( row );
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

    private void updateCollapseMetaDataOnCollapse( final int rowIndex,
                                                   final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );

        if ( currentRowCell == null ) {
            return;
        }

        if ( currentRowCell.getMergedCellCount() == 0 ) {
            do {
                minRowIndex--;
                final MergableGridRow previousRow = rows.get( minRowIndex );
                final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
                if ( previousRowCell.getMergedCellCount() > 0 ) {
                    break;
                }
            }
            while ( minRowIndex > 0 );
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.getMergedCellCount() > 0 ) {
                break;
            }
            maxRowIndex++;
        }

        for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
            rows.get( i ).collapse();
        }

        for ( int i = 0; i < getColumns().size(); i++ ) {
            if ( i == columnIndex ) {
                continue;
            }
            updateMergeMetaDataOnCollapseTopSplitRows( minRowIndex,
                                                       maxRowIndex,
                                                       i );
            updateMergeMetaDataOnCollapseBottomSplitRows( minRowIndex,
                                                          maxRowIndex,
                                                          i );
        }
    }

    private void updateMergeMetaDataOnCollapseTopSplitRows( final int minRowIndex,
                                                            final int maxRowIndex,
                                                            final int columnIndex ) {
        if ( minRowIndex < 1 ) {
            return;
        }

        final MergableGridRow checkTopRow = getRow( minRowIndex - 1 );
        final MergableGridCell checkTopCell = checkTopRow.getCells().get( columnIndex );

        if ( checkTopCell == null ) {
            return;
        }

        if ( checkTopCell.getMergedCellCount() == 1 ) {
            return;
        }

        // Scan from the first row before the start of collapsed block downwards to the end of the
        // collapsed block. If any cell is not identical to first then we need to split the cell.
        boolean splitTopSection = false;
        for ( int collapsedRowIndex = minRowIndex; collapsedRowIndex < maxRowIndex; collapsedRowIndex++ ) {
            final MergableGridRow collapsedRow = getRow( collapsedRowIndex );
            final MergableGridCell collapsedCell = collapsedRow.getCells().get( columnIndex );
            if ( collapsedCell == null ) {
                break;
            }
            if ( !collapsedCell.equals( checkTopCell ) ) {
                break;
            }
            splitTopSection = collapsedRowIndex < maxRowIndex - 1;
        }

        if ( splitTopSection ) {

            //Find minimum row with a cell containing the same value as the split-point
            int checkMinRowIndex = minRowIndex - 1;
            if ( checkTopCell.getMergedCellCount() == 0 ) {
                while ( checkMinRowIndex > 0 ) {
                    final MergableGridRow previousRow = rows.get( checkMinRowIndex );
                    final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
                    if ( previousRowCell.getMergedCellCount() > 0 ) {
                        break;
                    }
                    checkMinRowIndex--;
                }
            }

            //Update merge meta-data for top part of split cell
            for ( int i = checkMinRowIndex; i < minRowIndex; i++ ) {
                final MergableGridRow row = rows.get( i );
                row.getCells().get( columnIndex ).setMergedCellCount( 0 );
                updateRowMergedCells( row );
            }

            final MergableGridRow topSplitRow = rows.get( checkMinRowIndex );
            topSplitRow.getCells().get( columnIndex ).setMergedCellCount( minRowIndex - checkMinRowIndex );
            updateRowMergedCells( topSplitRow );

            //Find maximum row with a cell containing the same value as the split-point
            int checkMaxRowIndex = minRowIndex;
            boolean foundBottomSplitMarker = false;
            while ( checkMaxRowIndex < rows.size() ) {
                final MergableGridRow nextRow = rows.get( checkMaxRowIndex );
                final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
                if ( nextRowCell == null ) {
                    break;
                }
                if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                    checkMaxRowIndex--;
                    break;
                }
                if ( !nextRowCell.equals( checkTopCell ) ) {
                    break;
                }
                if ( nextRowCell.getMergedCellCount() > 0 ) {
                    foundBottomSplitMarker = true;
                }
                checkMaxRowIndex++;
            }

            //Update merge meta-data for bottom part of split cell
            for ( int i = minRowIndex; i < checkMaxRowIndex; i++ ) {
                final MergableGridRow row = rows.get( i );
                row.getCells().get( columnIndex ).setMergedCellCount( 0 );
                updateRowMergedCells( row );
            }

            final MergableGridRow bottomSplitRow = rows.get( minRowIndex );
            bottomSplitRow.getCells().get( columnIndex ).setMergedCellCount( checkMaxRowIndex - minRowIndex );
            updateRowMergedCells( bottomSplitRow );
        }
    }

    private void updateMergeMetaDataOnCollapseBottomSplitRows( final int minRowIndex,
                                                               final int maxRowIndex,
                                                               final int columnIndex ) {
        if ( maxRowIndex == rows.size() ) {
            return;
        }

        final MergableGridRow checkBottomRow = getRow( maxRowIndex );
        final MergableGridCell checkBottomCell = checkBottomRow.getCells().get( columnIndex );

        if ( checkBottomCell == null ) {
            return;
        }

        if ( checkBottomCell.getMergedCellCount() == 1 ) {
            return;
        }

        // Scan from the first row after the end of collapsed block upwards to the beginning of the
        // collapsed block. If any cell is not identical to first then we need to split the cell.
        boolean splitBottomSection = false;
        for ( int collapsedRowIndex = maxRowIndex - 1; collapsedRowIndex >= minRowIndex; collapsedRowIndex-- ) {
            final MergableGridRow collapsedRow = getRow( collapsedRowIndex );
            final MergableGridCell collapsedCell = collapsedRow.getCells().get( columnIndex );
            if ( collapsedCell == null ) {
                break;
            }
            if ( !collapsedCell.equals( checkBottomCell ) ) {
                break;
            }
            splitBottomSection = collapsedRowIndex > minRowIndex;
        }

        if ( splitBottomSection ) {

            //Find minimum row with a cell containing the same value as the split-point
            int checkMinRowIndex = maxRowIndex - 1;
            if ( checkBottomCell.getMergedCellCount() == 0 ) {
                while ( checkMinRowIndex > 0 ) {
                    final MergableGridRow previousRow = rows.get( checkMinRowIndex );
                    final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
                    if ( previousRowCell.getMergedCellCount() > 0 ) {
                        break;
                    }
                    checkMinRowIndex--;
                }
            }

            //Update merge meta-data for top part of split cell
            for ( int i = checkMinRowIndex; i < maxRowIndex; i++ ) {
                final MergableGridRow row = rows.get( i );
                row.getCells().get( columnIndex ).setMergedCellCount( 0 );
                updateRowMergedCells( row );
            }

            final MergableGridRow topSplitRow = rows.get( checkMinRowIndex );
            topSplitRow.getCells().get( columnIndex ).setMergedCellCount( maxRowIndex - checkMinRowIndex );
            updateRowMergedCells( topSplitRow );

            //Find maximum row with a cell containing the same value as the split-point
            int checkMaxRowIndex = maxRowIndex;
            boolean foundBottomSplitMarker = false;
            while ( checkMaxRowIndex < rows.size() ) {
                final MergableGridRow nextRow = rows.get( checkMaxRowIndex );
                final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
                if ( nextRowCell == null ) {
                    break;
                }
                if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                    checkMaxRowIndex--;
                    break;
                }
                if ( !nextRowCell.equals( checkBottomCell ) ) {
                    break;
                }
                if ( nextRowCell.getMergedCellCount() > 0 ) {
                    foundBottomSplitMarker = true;
                }
                checkMaxRowIndex++;
            }

            //Update merge meta-data for bottom part of split cell
            for ( int i = maxRowIndex; i < checkMaxRowIndex; i++ ) {
                final MergableGridRow row = rows.get( i );
                row.getCells().get( columnIndex ).setMergedCellCount( 0 );
                updateRowMergedCells( row );
            }

            //Only split bottom if it isn't already split
            final MergableGridRow bottomSplitRow = rows.get( maxRowIndex );
            if ( bottomSplitRow.getCells().get( columnIndex ).getMergedCellCount() == 0 ) {
                bottomSplitRow.getCells().get( columnIndex ).setMergedCellCount( checkMaxRowIndex - maxRowIndex );
                updateRowMergedCells( bottomSplitRow );
            }
        }
    }

    private void updateCollapseMetaDataOnExpand( final int rowIndex,
                                                 final int columnIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = rows.get( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );

        if ( currentRowCell == null ) {
            return;
        }

        if ( currentRowCell.getMergedCellCount() == 0 ) {
            do {
                minRowIndex--;
                final MergableGridRow previousRow = rows.get( minRowIndex );
                final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
                if ( previousRowCell.getMergedCellCount() > 0 ) {
                    break;
                }
            }
            while ( minRowIndex > 0 );
        }

        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.getMergedCellCount() > 0 ) {
                break;
            }
            maxRowIndex++;
        }

        for ( int i = minRowIndex + 1; i < maxRowIndex; i++ ) {
            rows.get( i ).expand();
        }

        for ( int i = 0; i < getColumns().size(); i++ ) {
            final int _columnIndex = getColumns().get( i ).getIndex();
            updateMergeMetaDataOnExpand( minRowIndex,
                                         _columnIndex,
                                         minRowIndex,
                                         maxRowIndex );
            updateMergeMetaDataOnExpand( maxRowIndex - 1,
                                         _columnIndex,
                                         minRowIndex,
                                         maxRowIndex );
        }
    }

    private void updateMergeMetaDataOnExpand( final int rowIndex,
                                              final int columnIndex,
                                              final int expandMinRowIndex,
                                              final int expandMaxRowIndex ) {
        //Find cell's current value
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + 1;
        final MergableGridRow currentRow = getRow( rowIndex );
        final MergableGridCell currentRowCell = currentRow.getCells().get( columnIndex );

        if ( currentRowCell == null ) {
            return;
        }

        //Find minimum row with a cell containing the same value as that being updated
        boolean foundTopSplitMarker = currentRowCell.getMergedCellCount() > 0;
        while ( minRowIndex > 0 ) {
            final MergableGridRow previousRow = rows.get( minRowIndex - 1 );
            final MergableGridCell previousRowCell = previousRow.getCells().get( columnIndex );
            if ( previousRowCell == null ) {
                break;
            }
            if ( previousRowCell.isCollapsed() && foundTopSplitMarker ) {
                break;
            }
            if ( !previousRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( previousRowCell.getMergedCellCount() > 0 ) {
                foundTopSplitMarker = true;
            }
            minRowIndex--;
        }

        //Find maximum row with a cell containing the same value as that being updated
        boolean foundBottomSplitMarker = false;
        while ( maxRowIndex < rows.size() ) {
            final MergableGridRow nextRow = rows.get( maxRowIndex );
            final MergableGridCell nextRowCell = nextRow.getCells().get( columnIndex );
            if ( nextRowCell == null ) {
                break;
            }
            if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                maxRowIndex--;
                break;
            }
            if ( !nextRowCell.equals( currentRowCell ) ) {
                break;
            }
            if ( nextRowCell.getMergedCellCount() > 0 ) {
                foundBottomSplitMarker = true;
            }
            maxRowIndex++;
        }

        //Update merge meta-data
        for ( int i = minRowIndex; i < maxRowIndex; i++ ) {
            final MergableGridRow row = rows.get( i );
            row.getCells().get( columnIndex ).setMergedCellCount( 0 );
            updateRowMergedCells( row );
        }

        final MergableGridRow row = rows.get( minRowIndex );
        row.getCells().get( columnIndex ).setMergedCellCount( maxRowIndex - minRowIndex );
        updateRowMergedCells( row );

        //If merged block is partially collapsed split it
        if ( maxRowIndex > expandMaxRowIndex ) {
            final MergableGridRow bottomSplitRow = rows.get( expandMaxRowIndex );
            if ( bottomSplitRow.isCollapsed() ) {
                bottomSplitRow.getCells().get( columnIndex ).setMergedCellCount( maxRowIndex - expandMaxRowIndex );
                updateRowMergedCells( bottomSplitRow );
                bottomSplitRow.expand();
                row.getCells().get( columnIndex ).setMergedCellCount( expandMaxRowIndex - minRowIndex );
                updateRowMergedCells( row );
            }
        }
    }

}
