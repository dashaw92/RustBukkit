pub use rustbukkit_sys::RustBukkit;

pub fn broadcast_message<S>(bukkit: &RustBukkit, msg: S) -> i32 
where S: AsRef<str> {
    let msg = msg.as_ref();
    rustbukkit_sys::broadcast_message(bukkit, msg.as_ptr())
}
