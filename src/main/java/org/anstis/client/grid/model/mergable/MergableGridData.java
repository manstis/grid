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

import java.util.ArrayList;
import java.util.List;

import org.anstis.client.grid.model.BaseGridData;

public class MergableGridData extends BaseGridData<MergableGridRow, MergableGridColumn, MergableGridCell> implements IMergableGridData {

    protected int rowCount = 0;
    protected List<Integer> index = new ArrayList<>();

    @Override
    public void addRow( final MergableGridRow row ) {
        index.add( index.size() );
        rowCount++;
        super.addRow( row );
    }

    @Override
    public void addRow( final int rowIndex,
                        final MergableGridRow row ) {
        index.add( rowIndex,
                   rowIndex );
        for ( int i = rowIndex + 1; i < this.index.size(); i++ ) {
            this.index.set( i,
                            this.index.get( i ) + 1 );
        }
        rowCount++;
        super.addRow( rowIndex,
                      row );
    }

    @Override
    public MergableGridRow getRow( final int rowIndex ) {
        final int _rowIndex = index.get( rowIndex );
        return super.getRow( _rowIndex );
    }

    @Override
    public MergableGridCell getCell( final int rowIndex,
                                     final int columnIndex ) {
        final int _rowIndex = index.get( rowIndex );
        return super.getCell( _rowIndex,
                              columnIndex );
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public void setCell( final int rowIndex,
                         final int columnIndex,
                         final String value ) {
        final int _rowIndex = index.get( rowIndex );
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        if ( _rowIndex < 0 || _rowIndex > rows.size() - 1 ) {
            return;
        }

        int minRowIndex = _rowIndex;
        int maxRowIndex = _rowIndex + 1;
        final MergableGridRow currentRow = getRow( _rowIndex );
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

        assertMerging( _rowIndex,
                       _columnIndex );
    }

    @Override
    public void groupCell( final int rowIndex,
                           final int columnIndex,
                           final boolean isGrouped ) {
        final MergableGridCell cell = getCell( rowIndex,
                                               columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( !cell.isMerged() ) {
            return;
        }
        if ( !cell.isGrouped() ) {
            doGroupCell( rowIndex,
                         columnIndex,
                         cell );
        } else {
            doUngroupCell( rowIndex,
                           columnIndex,
                           cell );
        }
    }

    private void doGroupCell( final int rowIndex,
                              final int columnIndex,
                              final MergableGridCell cell ) {
        cell.setGrouped( true );
        final int cellCountAdjustment = cell.getMergedCellCount() - 1;
        for ( int i = rowIndex + 1; i < this.index.size(); i++ ) {
            this.index.set( i,
                            this.index.get( i ) + cellCountAdjustment );
        }
        rowCount = rowCount - cellCountAdjustment;
    }

    private void doUngroupCell( final int rowIndex,
                                final int columnIndex,
                                final MergableGridCell cell ) {
        cell.setGrouped( false );
        final int cellCountAdjustment = cell.getMergedCellCount() - 1;
        for ( int i = rowIndex + 1; i < this.index.size(); i++ ) {
            this.index.set( i,
                            this.index.get( i ) - cellCountAdjustment );
        }
        rowCount = rowCount + cellCountAdjustment;
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

}
