#[no_mangle]
pub extern "C" fn on_load() {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn on_enable() {
    println!("Test plugin enabled!");
    // let players = bukkit.broadcast_message("hello from TestPlugin!");
    // println!("Broadcast reached {players} player(s)!");
}

#[no_mangle]
pub extern "C" fn on_disable() {
    println!("Test plugin disabled!");
}
