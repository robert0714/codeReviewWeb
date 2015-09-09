package com.iisigroup.java.tech.servelet;

import com.iisigroup.java.tech.ldap.internal.Node;

public interface Filter { 
    boolean exclude(final Node node);
    boolean include(final Node node);
}
