use dlopen::wrapper::{Container, WrapperApi};

use jni::{
    objects::{JClass, JObject, JString, JValue},
    sys::jlong,
    JNIEnv,
};

#[derive(WrapperApi)]
struct Api {
    onload: fn(),
    onenable: fn(),
    ondisable: fn(),
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

    println!(
        "\n... got path to native plugin '{}', attempting to load.",
        &path
    );
    let plug: Box<Container<Api>> =
        Box::new(unsafe { Container::load(path) }.expect("Failed to load plugin library!"));

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
    api.onload();
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onEnable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    api.onenable();
}

#[no_mangle]
pub extern "C" fn Java_me_danny_nativeplug_JNIPlugin_onDisable(env: JNIEnv, this: JObject) {
    let api = get_api(&env, &this);
    api.ondisable();
}

fn get_api(env: &JNIEnv, this: &JObject) -> Box<Container<Api>> {
    let JValue::Long(handle) = env
        .get_field(*this, "handle", "J")
        .expect("Failed to read ptr from instance!") else {
            panic!("Handle is not a long!");
        };

    unsafe { Box::from_raw(handle as *const i64 as *mut Container<Api>) }
}
