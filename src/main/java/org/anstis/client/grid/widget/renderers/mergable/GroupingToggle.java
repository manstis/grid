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
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.ColorName;

public class GroupingToggle extends Group {

    private static final double SIZE = 15;
    private static final double PADDING = 2;

    private final Rectangle r = new Rectangle( SIZE,
                                               SIZE,
                                               5 );

    @SuppressWarnings("unused")
    public GroupingToggle( final double containerWidth,
                           final double containerHeight,
                           final boolean isGrouped ) {
        r.setFillColor( isGrouped ? ColorName.RED : ColorName.GREEN );
        r.setX( containerWidth - SIZE - PADDING );
        r.setY( PADDING );
        add( r );
    }

    public boolean onHotSpot( final double cellX,
                              final double cellY ) {
        if ( cellX - r.getX() > 0 && cellX - r.getX() < SIZE ) {
            if ( cellY > PADDING && cellY < PADDING + SIZE ) {
                return true;
            }
        }
        return false;
    }

}
