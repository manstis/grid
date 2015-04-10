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

import com.google.gwt.core.client.Callback;
import org.anstis.client.grid.model.mergable.MergableGridCell;
import org.anstis.client.grid.widget.BaseGridWidgetMouseDoubleClickHandler;
import org.anstis.client.grid.widget.IEditManager;
import org.anstis.client.grid.widget.ISelectionManager;
import org.anstis.client.grid.widget.renderers.mergable.IMergableGridRenderer;

public class MergableGridWidgetMouseDoubleClickHandler extends BaseGridWidgetMouseDoubleClickHandler<MergableGridWidget> {

    public MergableGridWidgetMouseDoubleClickHandler( final MergableGridWidget grid,
                                                      final IEditManager editManager,
                                                      final ISelectionManager selectionManager,
                                                      final IMergableGridRenderer renderer ) {
        super( grid,
               editManager,
               selectionManager,
               renderer );
    }

    @Override
    protected void doEdit( final int rowIndex,
                           final int columnIndex ) {
        final MergableGridCell cell = grid.getModel().getCell( rowIndex,
                                                               columnIndex );
        editManager.edit( cell == null ? "" : cell.getValue(),
                          new Callback<String, String>() {
                              @Override
                              public void onFailure( final String value ) {
                                  //Do nothing
                              }

                              @Override
                              public void onSuccess( final String value ) {
                                  grid.getModel().setCell( rowIndex,
                                                           columnIndex,
                                                           value );
                                  grid.getLayer().draw();
                              }
                          } );
    }

}