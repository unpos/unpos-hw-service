/*
 * Developed by Sokolov Alexey aka scream3r for 7pikes inc., 2014.
 *
 * scream3r.org@gmail.com
 * http://scream3r.org
 *
 * http://7pikes.com
 *
 * © Sokolov Alexey, 7pikes inc., 2014.
 */
package ru.unpos.hw.service.vfd;

import jssc.SerialPortList;

/**
 *
 * @author scream3r
 */
public class DisplayUtils {

    private static final String libVersion = "0.20";  //23.04.2015
    private static final String libMinorSuffix = "1"; //23.04.2015

    public static String getLibraryVersion() {
        return libVersion + "." + libMinorSuffix;
    }

    public static String getLibraryBaseVersion() {
        return libVersion;
    }

    public static String getLibraryMinorSuffix() {
        return libMinorSuffix;
    }

    public static String[] getPortNames() {
        return SerialPortList.getPortNames();
    }

}
