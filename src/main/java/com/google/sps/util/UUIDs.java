package com.google.sps.util;

import java.util.UUID;

//version 4 UUID
public class UUIDs {
    public static long generateID() {
        return UUID.randomUUID().hashCode();
    }
}
