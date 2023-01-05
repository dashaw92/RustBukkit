use jni::{objects::JValue, JNIEnv};

pub trait Bukkit {
    fn broadcast_message(&self, message: &str) -> i32;
}

pub struct BukkitImpl<'jvm, 'a> {
    pub(crate) env: &'jvm JNIEnv<'a>,
}

impl<'jvm, 'a> Bukkit for BukkitImpl<'jvm, 'a> {
    fn broadcast_message(&self, message: &str) -> i32 {
        let jstring = self.env.new_string(message).unwrap();
        match self.env.call_static_method(
            "org/bukkit/Bukkit",
            "broadcastMessage",
            "(Ljava/lang/String;)I",
            &[JValue::Object(*jstring)],
        ) {
            Ok(JValue::Int(playerCount)) => playerCount,
            Ok(what) => {
                println!("static method worked, but we didn't get back an int...? {what:?}");
                1
            }
            Err(e) => {
                eprintln!("What? {:?}", e);
                0
            }
        }
    }
}
