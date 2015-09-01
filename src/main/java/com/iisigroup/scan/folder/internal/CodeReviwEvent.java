package com.iisigroup.scan.folder.internal;

import java.io.Serializable;

/**
 * The Class codeReviwEvent.
 */
public class CodeReviwEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7642215139925459054L;

    /** The visible. */
    private boolean visible;
    
    /** The watchers. */
    private String[] watchers;

    /**
     * Checks if is visible.
     *
     * @return true, if is visible
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visible.
     *
     * @param visible the new visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the watchers.
     *
     * @return the watchers
     */
    public String[] getWatchers() {
        if(this.watchers == null ){
            this.watchers = new String[0];
        }
        return this.watchers;
    }

    /**
     * Sets the watchers.
     *
     * @param watchers the new watchers
     */
    public void setWatchers(String[] watchers) {
        if(watchers == null ){
            this.watchers =null;
        }else {
            this.watchers = new String[watchers.length];
            System.arraycopy(watchers, 0,  this.watchers, 0, watchers.length);
        }
    }
    

}
