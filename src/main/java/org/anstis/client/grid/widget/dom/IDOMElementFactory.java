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
package org.anstis.client.grid.widget.dom;

import org.anstis.client.grid.model.IGridCell;
import org.anstis.client.grid.model.IHasResources;
import org.anstis.client.grid.widget.context.GridCellRenderContext;

public interface IDOMElementFactory<T> extends IHasResources {

    void addCell( final IGridCell<T> cell,
                  final GridCellRenderContext context );

}
