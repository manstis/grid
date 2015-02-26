package org.anstis.client.grid.widget;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Node;

public interface IGridWidget<T extends Node<T>> extends IPrimitive<T> {

    public double getWidth();

}
