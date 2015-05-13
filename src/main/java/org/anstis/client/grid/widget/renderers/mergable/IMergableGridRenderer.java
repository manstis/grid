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
package org.anstis.client.grid.widget.renderers.mergable;

import com.ait.lienzo.client.core.shape.Group;
import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.widget.renderers.IGridRenderer;

public interface IMergableGridRenderer extends IGridRenderer<MergableGridData> {

    Group renderGroupedCellToggle( final double containerWidth,
                                   final double containerHeight,
                                   final boolean isGrouped );

    Group renderMergedCellMixedValueHighlight( final double columnWidth,
                                               final double rowHeight );

    boolean onGroupingToggle( final double cellX,
                              final double cellY,
                              final double columnWidth,
                              final double rowHeight );
}
