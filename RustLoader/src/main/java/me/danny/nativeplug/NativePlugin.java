package me.danny.nativeplug;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

public record NativePlugin(String path, Arena arena, MethodHandle onLoadHnd, MethodHandle onEnableHnd,
                           MethodHandle onDisableHnd) {

    private static void invoke(MethodHandle handle, MemorySegment bukkitImpl) {
        try {
            handle.invokeExact(bukkitImpl);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void onLoad(MemorySegment bukkitImpl) {
        invoke(onLoadHnd, bukkitImpl);
    }

    public void onEnable(MemorySegment bukkitImpl) {
        invoke(onEnableHnd, bukkitImpl);
    }

    public void onDisable(MemorySegment bukkitImpl) {
        invoke(onDisableHnd, bukkitImpl);
    }
}
