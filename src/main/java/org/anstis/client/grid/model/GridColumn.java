package org.anstis.client.grid.model;

public class GridColumn {

    private String title;
    private int width;
    private Grid link;

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

    public boolean isLinked() {
        return link != null;
    }

    public Grid getLink() {
        return link;
    }

    public void setLink( final Grid link ) {
        this.link = link;
    }

}
