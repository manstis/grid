package org.anstis.client.grid.widget;

import com.ait.lienzo.client.core.shape.Layer;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

public class GridLayer extends Layer {

    @Override
    public void draw() {
        Scheduler.get().scheduleFinally( new Command() {
            @Override
            public void execute() {
                GridLayer.super.draw();
            }
        } );
    }

}
