#[no_mangle]
pub extern "system" fn onenable() {
    println!("Test plugin enabled!");
}

#[no_mangle]
pub extern "system" fn ondisable() {
    println!("Test plugin disabled!");
}

#[no_mangle]
pub extern "system" fn onload() {
    println!("Test plugin loaded!");
}
