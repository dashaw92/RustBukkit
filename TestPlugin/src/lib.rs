use rustbukkit::bukkit::Bukkit;

#[no_mangle]
pub extern "C" fn onload(_: &Bukkit) {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn onenable(bukkit: &Bukkit) {
    println!("Test plugin enabled!");
    let players = bukkit.broadcast_message("hello from TestPlugin!");
    println!("Broadcast reached {players} player(s)!");
}

#[no_mangle]
pub extern "C" fn ondisable(_: &Bukkit) {
    println!("Test plugin disabled!");
}
