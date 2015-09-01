package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;
 
/**
 * The Class Col.
 */
public class Column implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6118051056167920548L;
    
    /** The metric. */
    private String metric;

    /**
     * Gets the metric.
     *
     * @return the metric
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Sets the metric.
     *
     * @param metric the new metric
     */
    public void setMetric(String metric) {
        this.metric = metric;
    }
    
}
