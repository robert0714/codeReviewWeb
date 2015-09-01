package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;
 
/**
 * The Class PageInfo.
 */
public class PageInfo implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1060526134742696751L;

    /** The page index. */
    private int pageIndex;
    
    /** The page size. */
    private int pageSize;
    
    /** The total. */
    private int total;
    
    /** The total. */
    private float fTotal;
    
    /** The pages. */
    private int pages;
    
    /**
     * Gets the page index.
     *
     * @return the page index
     */
    public int getPageIndex() {
        return pageIndex;
    }
    
    /**
     * Sets the page index.
     *
     * @param pageIndex the new page index
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    
    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * Sets the page size.
     *
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * Gets the total.
     *
     * @return the total
     */
    public int getTotal() {
        return total;
    }
    
    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(int total) {
        this.total = total;
    }
    
    /**
     * Gets the f total.
     *
     * @return the f total
     */
    public float getfTotal() {
        return fTotal;
    }
    
    /**
     * Sets the f total.
     *
     * @param fTotal the new f total
     */
    public void setfTotal(float fTotal) {
        this.fTotal = fTotal;
    }
    
    /**
     * Gets the pages.
     *
     * @return the pages
     */
    public int getPages() {
        return pages;
    }
    
    /**
     * Sets the pages.
     *
     * @param pages the new pages
     */
    public void setPages(int pages) {
        this.pages = pages;
    }
    

}
