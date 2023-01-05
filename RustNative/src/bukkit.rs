use jni::{objects::JValue, JNIEnv};

pub struct Bukkit<'jvm, 'a> {
    pub(crate) env: &'jvm JNIEnv<'a>,
}

#[allow(dead_code)]
impl<'jvm, 'a> Bukkit<'jvm, 'a> {
    pub fn broadcast_message(&self, message: &str) -> i32 {
        let Ok(jstring) = self.env.new_string(message) else {
            eprintln!("Failed to get a string object from Java!");
            return -1;
        };

        match self.env.call_static_method(
            "org/bukkit/Bukkit",
            "broadcastMessage",
            "(Ljava/lang/String;)I",
            &[jstring.into()],
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
