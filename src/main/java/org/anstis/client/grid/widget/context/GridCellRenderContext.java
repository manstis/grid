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
import org.anstis.client.grid.widget.BaseGridWidget;

public class GridCellRenderContext {

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final int rowIndex;
    private final int columnIndex;
    private final Transform transform;
    private final BaseGridWidget<?, ?> widget;

    public GridCellRenderContext( final double x,
                                  final double y,
                                  final double width,
                                  final double height,
                                  final int rowIndex,
                                  final int columnIndex,
                                  final Transform transform,
                                  final BaseGridWidget<?, ?> widget ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.transform = transform;
        this.widget = widget;
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

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public Transform getTransform() {
        return transform;
    }

    public BaseGridWidget<?, ?> getWidget() {
        return widget;
    }

}
