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
package org.anstis.client.grid.widget.context;

import com.ait.lienzo.client.core.types.Transform;

public class GridHeaderRenderContext {

    private final int startColumnIndex;
    private final int endColumnIndex;
    private final double width;
    private final Transform transform;

    public GridHeaderRenderContext( final int startColumnIndex,
                                    final int endColumnIndex,
                                    final double width,
                                    final Transform transform ) {
        this.startColumnIndex = startColumnIndex;
        this.endColumnIndex = endColumnIndex;
        this.width = width;
        this.transform = transform;
    }

    public int getStartColumnIndex() {
        return startColumnIndex;
    }

    public int getEndColumnIndex() {
        return endColumnIndex;
    }

    public double getWidth() {
        return width;
    }

    public Transform getTransform() {
        return transform;
    }
}
