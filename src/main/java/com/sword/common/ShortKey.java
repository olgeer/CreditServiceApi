package com.sword.common;

import java.util.UUID;

/**
 * Short uuid key in 8 bytes
 * @author max
 */
public class ShortKey {
    public static String newKey(){
        return UUID.randomUUID().toString().substring(24,36);
    }
}
