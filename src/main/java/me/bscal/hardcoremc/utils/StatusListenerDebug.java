package me.bscal.hardcoremc.utils;

import me.bscal.hardcoremc.App;

public class StatusListenerDebug {
    
    /// Prints debug info about a certain status listener
    /// name - Whatever name you want the statuslistener to have
    /// success - weather the listener successed for the status
    /// data - Object array that prints in pairs on line. formatted by name then object
    public static void Print(String name, boolean success, Object... data) {
        App.Logger.info(String.format("[StatusListener-%s] Fired. Success: %b", name, success));
        for (int i = 0; i < data.length; i++) {
            if (i%2 == 0) // if even
                App.Logger.info(String.format("  %s - %s (%s),", data[i], data[i+1], data[i+1].getClass().getTypeName()));
            else 
                continue;
        }
    }

}