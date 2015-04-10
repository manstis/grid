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
package org.anstis.client.grid.widget.mergable;

import org.anstis.client.grid.model.mergable.MergableGridData;
import org.anstis.client.grid.widget.BaseGridWidget;
import org.anstis.client.grid.widget.IEditManager;
import org.anstis.client.grid.widget.ISelectionManager;
import org.anstis.client.grid.widget.renderers.mergable.IMergableGridRenderer;

public class MergableGridWidget extends BaseGridWidget<MergableGridData> {

    public MergableGridWidget( final MergableGridData model,
                               final IEditManager editManager,
                               final ISelectionManager selectionManager,
                               final IMergableGridRenderer renderer ) {
        super( model,
               editManager,
               selectionManager,
               renderer );

        //Click handler
        addNodeMouseClickHandler( new MergableGridWidgetMouseClickHandler( this,
                                                                           editManager,
                                                                           selectionManager,
                                                                           renderer ) );

        //Double-click handler
        addNodeMouseDoubleClickHandler( new MergableGridWidgetMouseDoubleClickHandler( this,
                                                                                       editManager,
                                                                                       selectionManager,
                                                                                       renderer ) );
    }

}