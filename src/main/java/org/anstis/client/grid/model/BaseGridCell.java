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

/**
 * Base implementation of a grid cell to avoid boiler-plate for more specific implementations.
 * @param <T> The Type of value
 */
public abstract class BaseGridCell<T> implements IGridCell<T> {

    protected IGridCellValue<T> value;

    public BaseGridCell( final IGridCellValue<T> value ) {
        this.value = value;
    }

    @Override
    public IGridCellValue<T> getValue() {
        return value;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        BaseGridCell that = (BaseGridCell) o;

        if ( value != null ? !value.equals( that.value ) : that.value != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
