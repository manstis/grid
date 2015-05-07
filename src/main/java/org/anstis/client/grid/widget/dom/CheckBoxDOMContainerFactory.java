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
package org.anstis.client.grid.widget.dom;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class CheckBoxDOMContainerFactory {

    private final List<CheckBoxDOMContainer> containers = new ArrayList<>();
    private final AbsolutePanel parent;

    private int consumed = 0;

    public CheckBoxDOMContainerFactory( final AbsolutePanel parent ) {
        this.parent = parent;
    }

    public GridCellDOMContainer getContainer() {
        CheckBoxDOMContainer container;
        if ( consumed + 1 > containers.size() ) {
            container = new CheckBoxDOMContainer( parent );
            containers.add( container );
        } else {
            container = containers.get( consumed );
        }
        consumed++;
        container.attach();
        return container;
    }

    public void initialiseResources() {
        consumed = 0;
    }

    public void destroyResources() {
        for ( GridCellDOMContainer container : containers ) {
            container.detach();
        }
        containers.clear();
        consumed = 0;
    }

    public void freeResources() {
        final List<CheckBoxDOMContainer> freed = new ArrayList<>();
        for ( int i = consumed; i < containers.size(); i++ ) {
            final CheckBoxDOMContainer container = containers.get( i );
            container.detach();
            freed.add( container );
        }
        for ( CheckBoxDOMContainer container : freed ) {
            containers.remove( container );
        }
    }

}
