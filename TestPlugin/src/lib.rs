#[no_mangle]
pub extern "C" fn on_load() {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn on_enable() {
    println!("Test plugin enabled!");
}

#[no_mangle]
pub extern "C" fn on_disable() {
    println!("Test plugin disabled!");
}
