#[no_mangle]
pub extern "C" fn onload() {
    println!("Test plugin loaded!");
}

#[no_mangle]
pub extern "C" fn onenable() {
    println!("Test plugin enabled!");
}

#[no_mangle]
pub extern "C" fn ondisable() {
    println!("Test plugin disabled!");
}
