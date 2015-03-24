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
package org.anstis.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import org.anstis.client.grid.widget.GridShowcaseWidget;
import org.anstis.client.grid.widget.renderers.BlueGridRenderer;
import org.anstis.client.grid.widget.renderers.CustomGridRenderer;
import org.anstis.client.grid.widget.renderers.GreenGridRenderer;
import org.anstis.client.grid.widget.renderers.GridRendererRegistry;
import org.anstis.client.grid.widget.renderers.IGridRenderer;
import org.anstis.client.grid.widget.renderers.RedGridRenderer;

public class GridShowcase implements EntryPoint {

    public void onModuleLoad() {
        final IGridRenderer custom = new CustomGridRenderer();
        GridRendererRegistry.addRenderer( custom );
        GridRendererRegistry.addRenderer( new RedGridRenderer() );
        GridRendererRegistry.addRenderer( new GreenGridRenderer() );
        GridRendererRegistry.addRenderer( new BlueGridRenderer() );

        GridRendererRegistry.setActiveStyleName( custom.getName() );

        final GridShowcaseWidget container = new GridShowcaseWidget();
        RootPanel.get().add( container );
    }

}
