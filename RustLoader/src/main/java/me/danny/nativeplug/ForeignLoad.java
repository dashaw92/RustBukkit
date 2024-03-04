package me.danny.nativeplug;

import java.io.File;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Optional;

public final class ForeignLoad {

    private static final Arena PLUGIN_ARENA;

    private static final FunctionDescriptor ON_LOAD_DESC = FunctionDescriptor.ofVoid();
    private static final FunctionDescriptor ON_ENABLE_DESC = FunctionDescriptor.ofVoid();
    private static final FunctionDescriptor ON_DISABLE_DESC = FunctionDescriptor.ofVoid();

    private static final MethodHandle DUMMY_ON_LOAD = MethodHandles.empty(ON_LOAD_DESC.toMethodType());
    private static final MethodHandle DUMMY_ON_ENABLE = MethodHandles.empty(ON_ENABLE_DESC.toMethodType());
    private static final MethodHandle DUMMY_ON_DISABLE = MethodHandles.empty(ON_DISABLE_DESC.toMethodType());

    static {
        PLUGIN_ARENA = Arena.ofShared();
    }

    static void close() {
        PLUGIN_ARENA.close();
    }

    static NativePlugin loadPlugin(File file) {
        System.out.println(STR."Loading plugin from \{file.getAbsolutePath()}");
        Linker linker = Linker.nativeLinker();
        SymbolLookup lib = SymbolLookup.libraryLookup(file.toPath(), PLUGIN_ARENA);

        var onLoad = get(linker, lib, "on_load", ON_LOAD_DESC).orElse(DUMMY_ON_LOAD);
        var onEnable = get(linker, lib, "on_enable", ON_ENABLE_DESC).orElse(DUMMY_ON_ENABLE);
        var onDisable = get(linker, lib, "on_disable", ON_DISABLE_DESC).orElse(DUMMY_ON_DISABLE);
        return new NativePlugin(file.getName(), PLUGIN_ARENA, onLoad, onEnable, onDisable);
    }

    private static Optional<MethodHandle> get(Linker linker, SymbolLookup lib, String name, FunctionDescriptor desc) {
        return lib.find(name).map(segment -> linker.downcallHandle(segment, desc));
    }

}
