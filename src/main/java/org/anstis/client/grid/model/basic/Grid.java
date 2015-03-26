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

import org.anstis.client.grid.model.BaseGrid;
import org.anstis.client.grid.model.GridColumn;

public class Grid extends BaseGrid<GridData> {

    public Grid() {
        setData( new GridData() );
    }

    /**
     * For testing. Use with caution!
     * @return The index of column positions to column definitions
     */
    List<Integer> getRelativeToAbsoluteIndex() {
        return this.indexRelativeToAbsolute;
    }

    /**
     * For testing. Use with caution!
     * @return The index of column definitions to column positions
     */
    List<Integer> getAbsoluteToRelativeIndex() {
        return this.indexAbsoluteToRelative;
    }

    /**
     * For testing. Use with caution!
     * @return Column definitions in the order they were first added to the table; i.e. with no regard for indexing.
     */
    List<GridColumn> getColumnDefinitions() {
        return this.columns;
    }

}
