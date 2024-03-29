#[repr(C)]
#[derive(Debug)]
pub struct RustBukkit {
    broadcast_message_hnd: extern "C" fn(*const u8) -> i32,
}

#[no_mangle]
pub extern "C" fn broadcast_message(bukkit: &RustBukkit, msg: *const u8) -> i32 {
    (bukkit.broadcast_message_hnd)(msg)
}
