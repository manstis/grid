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
package org.anstis.client.grid.model;

public class GridColumn {

    private String title;
    private int width;
    private GridColumn link;

    public GridColumn( final String title,
                       final int width ) {
        this.title = title;
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth( final int width ) {
        this.width = width;
    }

    public boolean isLinked() {
        return link != null;
    }

    public GridColumn getLink() {
        return link;
    }

    public void setLink( final GridColumn link ) {
        this.link = link;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof GridColumn ) ) {
            return false;
        }

        GridColumn column = (GridColumn) o;

        if ( title != null ? !title.equals( column.title ) : column.title != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = ~~result;
        return result;
    }
}
