package com.google.sps.util;

import java.util.UUID;

//version 4 UUID
public class UUIDs {
    public static long generateID() {
        return UUID.randomUUID().hashCode();
    }

    public static boolean validateUUID(String ID) {
        return ID.matches("/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i");
    }
}
