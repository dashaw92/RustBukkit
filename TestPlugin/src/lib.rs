use rustbukkit::RustBukkit;

#[no_mangle]
pub extern "C" fn on_load(_: &RustBukkit) {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn on_enable(bukkit: &RustBukkit) {
    println!("Test plugin enabled!");
    bukkit.broadcast_message("hello!".as_ptr());
}

#[no_mangle]
pub extern "C" fn on_disable(_: &RustBukkit) {
    println!("Test plugin disabled!");
}
