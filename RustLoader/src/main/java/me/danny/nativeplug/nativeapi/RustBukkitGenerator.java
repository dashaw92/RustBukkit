package me.danny.nativeplug.nativeapi;

import me.danny.nativeplug.jextract_gen.RustBukkit;
import org.bukkit.Bukkit;

import java.lang.foreign.*;
import java.lang.invoke.*;

import static me.danny.nativeplug.jextract_gen.rustbukkit_h.C_INT;
import static me.danny.nativeplug.jextract_gen.rustbukkit_h.C_POINTER;

public final class RustBukkitGenerator {
    public static MemorySegment generate(Arena arena) {
        MemorySegment rbukkit = RustBukkit.allocate(arena);

        RustBukkit.broadcast_message_hnd(rbukkit, getBroadcastMessage(arena));
        return rbukkit;
    }

    public static int proxyBroadcast(MemorySegment ptr) {
        String msg = "Uh oh D:";
        try {
            msg = ptr.getUtf8String(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(msg);
        return Bukkit.broadcastMessage(msg);
    }

    private static MemorySegment getBroadcastMessage(Arena arena) {
        var type = FunctionDescriptor.of(C_INT, C_POINTER);
        MethodHandle handle;
        try {
            handle = MethodHandles.publicLookup()
                    .findStatic(RustBukkitGenerator.class, "proxyBroadcast", type.toMethodType());
        } catch(Exception ex) {
            ex.printStackTrace();
            handle = MethodHandles.empty(type.toMethodType());
        }

        return Linker.nativeLinker().upcallStub(handle, type, arena);
    }
}
