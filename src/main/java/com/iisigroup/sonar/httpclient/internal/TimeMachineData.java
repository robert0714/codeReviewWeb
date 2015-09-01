package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
 
/**
 * The Class TimeMachineData.
 */
public class TimeMachineData implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6612085468519240873L;
    
    /** The column list. */
    private List<Column> columnList = null;
    
    /** The cell list. */
    private List<Cell> cellList = null;

    /**
     * Gets the column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        if (columnList == null) {
            columnList = new ArrayList<Column>();
        }
        return columnList;
    }

    /**
     * Sets the column list.
     *
     * @param columnList the new column list
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * Gets the cell list.
     *
     * @return the cell list
     */
    public List<Cell> getCellList() {
        if (cellList == null) {
            cellList = new ArrayList<Cell>();
        }
        return cellList;
    }

    /**
     * Sets the cell list.
     *
     * @param cellList the new cell list
     */
    public void setCellList(List<Cell> cellList) {
        this.cellList = cellList;
    }

}
