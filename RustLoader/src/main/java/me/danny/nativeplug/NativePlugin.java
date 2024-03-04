package me.danny.nativeplug;

import java.lang.foreign.Arena;
import java.lang.invoke.MethodHandle;

public record NativePlugin(String path, Arena arena, MethodHandle onLoadHnd, MethodHandle onEnableHnd,
                           MethodHandle onDisableHnd) {

    private static void invoke(MethodHandle handle) {
        try {
            handle.invoke();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void onLoad() {
        invoke(onLoadHnd);
    }

    public void onEnable() {
        invoke(onEnableHnd);
    }

    public void onDisable() {
        invoke(onDisableHnd);
    }
}
