package me.danny.nativeplug.nativeapi;

import me.danny.nativeplug.jextract_gen.RustBukkit;
import org.bukkit.Bukkit;

import java.lang.foreign.*;
import java.lang.invoke.*;

public final class RustBukkitGenerator {
    public static MemorySegment generate(Arena arena) {
        MemorySegment rbukkit = RustBukkit.allocate(arena);

        //Uhhhhh
//        rbukkit.set(RustBukkit.broadcast_message_hnd$layout(), );
        return rbukkit;
    }

    private static MethodHandle getBroadcastMessage() {
        MethodType type = MethodType.methodType(int.class, String.class);
        MethodHandles.Lookup l = MethodHandles.publicLookup();
        try {
            return l.findStatic(Bukkit.class, "broadcastMessage", type);
        } catch(Exception ex) {
            ex.printStackTrace();
            return MethodHandles.empty(type);
        }
    }
}
