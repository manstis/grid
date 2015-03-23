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
package org.anstis.client.grid.widget.renderers;

import com.ait.lienzo.client.core.shape.Group;
import org.anstis.client.grid.model.Grid;

public interface IGridRenderer {

    String getName();

    double getHeaderHeight();

    double getRowHeight();

    Group renderSelector( final double width,
                          final double height );

    Group renderHeader( final Grid model,
                        final int startColumnIndex,
                        final int endColumnIndex,
                        final double width );

    Group renderBody( final Grid model,
                      final int startColumnIndex,
                      final int endColumnIndex,
                      final int startRowIndex,
                      final int endRowIndex,
                      final double width );

}
