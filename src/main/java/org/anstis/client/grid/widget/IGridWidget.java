package org.anstis.client.grid.widget;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Node;
import org.anstis.client.grid.model.Grid;

public interface IGridWidget<T extends Node<T>> extends IPrimitive<T> {

    public Grid getModel();

    public double getWidth();

    public double getHeight();

    void select();

    void deselect();

}
