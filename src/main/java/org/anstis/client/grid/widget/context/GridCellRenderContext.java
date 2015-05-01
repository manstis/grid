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

public class GridCellRenderContext {

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final Transform transform;

    public GridCellRenderContext( final double x,
                                  final double y,
                                  final double width,
                                  final double height,
                                  final Transform transform ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.transform = transform;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Transform getTransform() {
        return transform;
    }

}
