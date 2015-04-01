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

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.anstis.client.grid.model.BaseGridColumn;

public class GridColumn extends BaseGridColumn<GridRow, GridCell> {

    public GridColumn( final String title,
                       final int width ) {
        super( title,
               width );
    }

    @Override
    public Group renderRow( final GridRow row ) {
        final Group g = new Group();
        final Text t = new Text( row.getCells().get( getIndex() ).getValue() )
                .setFillColor( ColorName.GREY )
                .setFontSize( 12 )
                .setFontFamily( "serif" )
                .setListening( false )
                .setTextBaseLine( TextBaseLine.MIDDLE )
                .setTextAlign( TextAlign.CENTER );
        g.add( t );
        return g;
    }

}
