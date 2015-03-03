package org.anstis.client.grid.widget;

import java.util.HashSet;
import java.util.Set;

import com.ait.lienzo.client.core.shape.Layer;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

public class GridLayer extends Layer implements ISelectionManager {

    private Set<ISelectable> selectables = new HashSet<>();

    @Override
    public void draw() {
        Scheduler.get().scheduleFinally( new Command() {
            @Override
            public void execute() {
                GridLayer.super.draw();
            }
        } );
    }

    @Override
    public void select( final ISelectable selectable ) {
        for ( ISelectable s : selectables ) {
            s.deselect();
        }
        selectables.add( selectable );
        selectable.select();
        draw();
    }

}
