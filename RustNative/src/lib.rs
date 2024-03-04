use std::os::raw::c_char;

#[repr(C)]
struct RustBukkit {
    broadcast_message_hnd: fn(*const c_char) -> i32,
}

impl RustBukkit {
    #[no_mangle]
    pub extern "C" fn broadcast_message(&self, msg: *const c_char) -> i32 {
        (self.broadcast_message_hnd)(msg)
    }
}
