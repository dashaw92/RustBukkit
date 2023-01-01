package me.danny.nativeplug;

public final class JNIPlugin {

    private final String name;
    private final long handle;

    public JNIPlugin(long handle, RustPlugin plugin) {
        this.name = plugin.getName();
        this.handle = handle;
    }

    public static native long open(String path);

    public native void onEnable();
    public native void onLoad();
    public native void onDisable();
    public native void close();


}