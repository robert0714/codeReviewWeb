package com.iisigroup.java.tech.sonar.business;

public class OSvalidator {
    private OSvalidator(){}
    /**
     * Checks if is windows.
     *
     * @return true, if is windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("win") >= 0;
    }

    /**
     * Checks if is mac.
     *
     * @return true, if is mac
     */
    public static  boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return os.indexOf("mac") >= 0;
    }

    /**
     * Checks if is unix.
     *
     * @return true, if is unix
     */
    public static  boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
    }

    /**
     * Checks if is solaris.
     *
     * @return true, if is solaris
     */
    public static  boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return os.indexOf("sunos") >= 0;
    }
}
