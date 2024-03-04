#[repr(C)]
pub struct RustBukkit {
    broadcast_message_hnd: fn(*const u8) -> i32,
}

impl RustBukkit {
    #[no_mangle]
    pub extern "C" fn broadcast_message(&self, msg: *const u8) -> i32 {
        (self.broadcast_message_hnd)(msg)
    }
}
