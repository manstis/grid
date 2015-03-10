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
