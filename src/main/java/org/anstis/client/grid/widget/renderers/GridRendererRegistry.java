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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Group;
import org.anstis.client.grid.model.IGrid;

public class GridRendererRegistry {

    private static IGridRenderer activeRenderer = null;
    private static Map<String, IGridRenderer<?>> renderers = new HashMap<>();

    public static double getHeaderHeight() {
        return activeRenderer.getHeaderHeight();
    }

    public static double getRowHeight() {
        return activeRenderer.getRowHeight();
    }

    public static Group renderSelector( final double width,
                                        final double height ) {
        return activeRenderer.renderSelector( width,
                                              height );
    }

    public static Group renderHeader( final IGrid<?> model,
                                      final int startColumnIndex,
                                      final int endColumnIndex,
                                      final double width ) {
        return activeRenderer.renderHeader( model,
                                            startColumnIndex,
                                            endColumnIndex,
                                            width );
    }

    public static Group renderBody( final IGrid<?> model,
                                    final int startColumnIndex,
                                    final int endColumnIndex,
                                    final int startRowIndex,
                                    final int endRowIndex,
                                    final double width ) {
        return activeRenderer.renderBody( model,
                                          startColumnIndex,
                                          endColumnIndex,
                                          startRowIndex,
                                          endRowIndex,
                                          width );
    }

    public static void addRenderer( final IGridRenderer<?> renderer ) {
        renderers.put( renderer.getName(),
                       renderer );
    }

    public static Collection<IGridRenderer<?>> getRenderers() {
        return renderers.values();
    }

    public static IGridRenderer getActiveRenderer() {
        return activeRenderer;
    }

    public static void setActiveStyleName( final String name ) {
        if ( !renderers.keySet().contains( name ) ) {
            throw new IllegalArgumentException( "Renderer '" + name + "' has not been added to the Registry." );
        }
        activeRenderer = renderers.get( name );
    }

}
