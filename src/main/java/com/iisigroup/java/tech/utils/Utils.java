package com.iisigroup.java.tech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.controller.operation.FileQueueManager;

public class Utils {
    private Utils() {
    }
    
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileQueueManager.class);

    public static <T> void serialization(T aObject, File outFile) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(outFile));
            
            oos.writeObject(aObject);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    public static <T> T deserialization(InputStream inputStream) {
        T result = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(inputStream);
            result = (T) ois.readObject();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    public static <T> T deserialization(File inputFile) {
        FileInputStream is = null;
        T result = null;

        try {
            is = new FileInputStream(inputFile);
            result = deserialization(is);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } 
        return result;
    }
    /**
     * Gets the ipv4.
     *
     * @return the ipv4
     */
    public static String getIpv4() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback()
                        || current.isVirtual()){
                    continue;
                }
                   
                final Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                   final InetAddress current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress()){
                        continue;
                    }
                    if (current_addr instanceof Inet4Address) {
                        return current_addr.getHostAddress();
                    }

                }
            }
        } catch (SocketException e) {
            LOGGER.error(e.getMessage(), e);

        }
        return null;
    }
}
