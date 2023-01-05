use jni::{
    objects::{JClass, JObject, JString, JValue},
    sys::jlong,
    JNIEnv,
};
use libloading::{Library, Symbol};

use crate::bukkit::{Bukkit, BukkitImpl};

struct LoadedPlugin {
    lib: Library,
}

type PluginHook = fn(&dyn Bukkit);

impl LoadedPlugin {
    fn call(&self, name: &[u8], bukkit: impl Bukkit) -> Result<(), libloading::Error> {
        let symbol: Result<Symbol<PluginHook>, _> = unsafe { self.lib.get(name) };
        match symbol {
            Ok(fp) => {
                fp(&bukkit);
                Ok(())
            }
            Err(e) => Err(e),
        }
    }

    fn onload(&self, bukkit: impl Bukkit) -> Result<(), libloading::Error> {
        self.call(b"onload\0", bukkit)
    }

    fn onenable(&self, bukkit: impl Bukkit) -> Result<(), libloading::Error> {
        self.call(b"onenable\0", bukkit)
    }

    fn ondisable(&self, bukkit: impl Bukkit) -> Result<(), libloading::Error> {
        self.call(b"ondisable\0", bukkit)
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

    let plug = Box::new(LoadedPlugin { lib });
    Box::into_raw(plug) as jlong
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_close(env: JNIEnv, this: JObject) {
    let JValue::Long(handle) = env
        .get_field(this, "handle", "J")
        .expect("Failed to read ptr from instance!") else {
            panic!("Handle is not a long!");
        };

    let heap = unsafe { Box::from_raw(handle as *mut LoadedPlugin) };
    drop(heap);
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onLoad(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);

    let bukkit = BukkitImpl { env: &env };
    if let Err(e) = api.onload(bukkit) {
        eprintln!("error in onload: {e}");
    }
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onEnable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);

    let bukkit = BukkitImpl { env: &env };
    if let Err(e) = api.onenable(bukkit) {
        eprintln!("error in onenable: {e}");
    }
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onDisable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);

    let bukkit = BukkitImpl { env: &env };
    if let Err(e) = api.ondisable(bukkit) {
        eprintln!("error in ondisable: {e}");
    }
}

fn get_api(env: &JNIEnv, this: &JObject) -> &'static LoadedPlugin {
    let JValue::Long(handle) = env
        .get_field(*this, "handle", "J")
        .expect("Failed to read ptr from JNIPlugin instance!") else {
            panic!("Handle is not a long!");
        };

    let plug = handle as *const LoadedPlugin;
    if plug.is_null() {
        panic!("handle -> null! Aborting.");
    }

    unsafe { &*plug }
}
