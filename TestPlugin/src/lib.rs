use rustbukkit::{RustBukkit, broadcast_message};

#[no_mangle]
pub extern "C" fn on_load(_: &RustBukkit) {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn on_enable(bukkit: &RustBukkit) {
    println!("Test plugin enabled!");
    broadcast_message(bukkit, "hello!");
}

#[no_mangle]
pub extern "C" fn on_disable(_: &RustBukkit) {
    println!("Test plugin disabled!");
}
