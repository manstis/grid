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
package org.anstis.client.grid.widget;

import org.anstis.client.grid.model.IGridColumn;

/**
 * Meta-data for a Connector between two "linked" GridWidgets.
 */
public class GridWidgetConnector {

    private IGridColumn<?, ?> sourceColumn;
    private IGridColumn<?, ?> targetColumn;
    private Direction direction;

    /**
     * Really simply direction indicator; to ensure Connectors
     * connect to the left or right sides of a GridWidget when
     * the GridWidget is re-sized in response to columns being
     * re-sized.
     */
    public enum Direction {
        EAST_WEST,
        WEST_EAST
    }

    public GridWidgetConnector( final IGridColumn<?, ?> sourceColumn,
                                final IGridColumn<?, ?> targetColumn,
                                final Direction direction ) {
        this.sourceColumn = sourceColumn;
        this.targetColumn = targetColumn;
        this.direction = direction;
    }

    /**
     * Get the source Column from which the Connector originates.
     * @return
     */
    public IGridColumn<?, ?> getSourceColumn() {
        return sourceColumn;
    }

    /**
     * Get the target Column to which the Connector terminates.
     * @return
     */
    public IGridColumn<?, ?> getTargetColumn() {
        return targetColumn;
    }

    /**
     * Get the direction of the Connector.
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        GridWidgetConnector connector = (GridWidgetConnector) o;

        if ( !sourceColumn.equals( connector.sourceColumn ) ) {
            return false;
        }
        if ( !targetColumn.equals( connector.targetColumn ) ) {
            return false;
        }
        if ( !direction.equals( connector.direction ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceColumn.hashCode();
        result = ~~result;
        result = 31 * result + targetColumn.hashCode();
        result = ~~result;
        result = 31 * result + direction.hashCode();
        result = ~~result;
        return result;
    }

}
