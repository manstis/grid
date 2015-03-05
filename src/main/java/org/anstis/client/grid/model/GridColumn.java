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

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof GridColumn ) ) {
            return false;
        }

        GridColumn column = (GridColumn) o;

        if ( width != column.width ) {
            return false;
        }
        if ( title != null ? !title.equals( column.title ) : column.title != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = ~~result;
        result = 31 * result + width;
        result = ~~result;
        return result;
    }
}
