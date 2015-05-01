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

public class GridBodyRenderContext {

    private final double x;
    private final double y;
    private final double width;
    private final int startColumnIndex;
    private final int endColumnIndex;
    private final int startRowIndex;
    private final int endRowIndex;
    private final Transform transform;

    public GridBodyRenderContext( final double x,
                                  final double y,
                                  final double width,
                                  final int startColumnIndex,
                                  final int endColumnIndex,
                                  final int startRowIndex,
                                  final int endRowIndex,
                                  final Transform transform ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.startColumnIndex = startColumnIndex;
        this.endColumnIndex = endColumnIndex;
        this.startRowIndex = startRowIndex;
        this.endRowIndex = endRowIndex;
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

    public int getStartColumnIndex() {
        return startColumnIndex;
    }

    public int getEndColumnIndex() {
        return endColumnIndex;
    }

    public int getStartRowIndex() {
        return startRowIndex;
    }

    public int getEndRowIndex() {
        return endRowIndex;
    }

    public Transform getTransform() {
        return transform;
    }
}
