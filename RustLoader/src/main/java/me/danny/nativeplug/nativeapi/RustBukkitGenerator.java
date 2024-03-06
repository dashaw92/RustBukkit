package me.danny.nativeplug.nativeapi;

import me.danny.nativeplug.jextract_gen.RustBukkit;
import org.bukkit.Bukkit;

import java.lang.foreign.*;
import java.lang.invoke.*;

public final class RustBukkitGenerator {
    public static MemorySegment generate(Arena arena) {
        MemorySegment rbukkit = RustBukkit.allocate(arena);

        rbukkit.set(RustBukkit.broadcast_message_hnd$layout(), 0, getBroadcastMessage(arena));
        return rbukkit;
    }

    private static int proxyBroadcast(MemorySegment ptr) {
        String msg = "Uh oh D:";
        try {
            msg = ptr.getUtf8String(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Bukkit.broadcastMessage(msg);
    }

    private static MemorySegment getBroadcastMessage(Arena arena) {
        var type = FunctionDescriptor.of(ValueLayout.JAVA_INT, AddressLayout.ADDRESS.withTargetLayout(ValueLayout.JAVA_BYTE));
        MethodHandle handle;
        try {
            handle = MethodHandles.lookup()
                    .findStatic(RustBukkitGenerator.class, "proxyBroadcast", type.toMethodType());
        } catch(Exception ex) {
            ex.printStackTrace();
            handle = MethodHandles.empty(type.toMethodType());
        }

        return Linker.nativeLinker().upcallStub(handle, type, arena);
    }
}
