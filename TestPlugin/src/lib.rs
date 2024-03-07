use rustbukkit::{RustBukkit, broadcast_message};

#[no_mangle]
pub extern "C" fn on_load(bukkit: RustBukkit) {
    println!("Test plugin loaded! {bukkit:?}");
}

#[no_mangle]
pub extern "C" fn on_enable(bukkit: RustBukkit) {
    println!("Test plugin enabled!");
    let playercount = broadcast_message(&bukkit, "hello!");
    println!("Broadcast reached {playercount} players.");
}

#[no_mangle]
pub extern "C" fn on_disable(_: RustBukkit) {
    println!("Test plugin disabled!");
}
