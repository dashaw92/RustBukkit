use jni::{
    objects::{JClass, JObject, JString, JValue},
    sys::jlong,
    JNIEnv,
};
use libloading::{Library, Symbol};

struct Plug {
    lib: Library,
}

impl Plug {
    fn call(&self, name: &[u8]) -> Result<(), libloading::Error> {
        let symbol: Result<Symbol<fn()>, _> = unsafe { self.lib.get(name) };
        match symbol {
            Ok(fp) => {
                fp();
                Ok(())
            }
            Err(e) => Err(e),
        }
    }

    fn onload(&self) -> Result<(), libloading::Error> {
        self.call(b"onload\0")
    }

    fn onenable(&self) -> Result<(), libloading::Error> {
        self.call(b"onenable\0")
    }

    fn ondisable(&self) -> Result<(), libloading::Error> {
        self.call(b"ondisable\0")
    }
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_open(
    env: JNIEnv,
    _class: JClass,
    path: JString,
) -> jlong {
    let path: String = match env.get_string(path) {
        Ok(path) => path.into(),
        Err(e) => {
            println!("Failed to read string...? {e:#?}");
            return 0;
        }
    };

    let lib = match unsafe { Library::new(&path) } {
        Ok(lib) => lib,
        Err(e) => {
            eprintln!("Failed to load library '{path}'\n{e}");
            panic!();
        }
    };

    let plug = Box::new(Plug { lib });
    Box::into_raw(plug) as *const i64 as jlong
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_close(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    drop(api);
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onLoad(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    if let Err(e) = api.onload() {
        eprintln!("error in onload: {e}");
    }

    let _ = api.onenable();
    let _ = api.ondisable();
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onEnable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    if let Err(e) = api.onenable() {
        eprintln!("error in onenable {e}");
    }
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onDisable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    if let Err(e) = api.ondisable() {
        eprintln!("error in ondisable {e}");
    }
}

fn get_api(env: &JNIEnv, this: &JObject) -> Box<Plug> {
    let JValue::Long(handle) = env
        .get_field(*this, "handle", "J")
        .expect("Failed to read ptr from instance!") else {
            panic!("Handle is not a long!");
        };

    unsafe { Box::from_raw(handle as *const i64 as *mut Plug) }
}
