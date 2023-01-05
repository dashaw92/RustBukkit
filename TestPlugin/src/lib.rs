#![allow(improper_ctypes_definitions)]

use rustbukkit::bukkit::Bukkit;

#[no_mangle]
pub extern "C" fn onload(_: &dyn Bukkit) {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn onenable(bukkit: &dyn Bukkit) {
    println!("Test plugin enabled!");
    bukkit.broadcast_message("hello from TestPlugin!");
}

#[no_mangle]
pub extern "C" fn ondisable(_: &dyn Bukkit) {
    println!("Test plugin disabled!");
}
