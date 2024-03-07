### Note  
Originally inspired by [NativeBukkit](https://github.com/jarcode-foss/NativeBukkit)

## RustBukkit  
Suite for implementing Bukkit (Spigot) plugins in native code with an idiomatic Rust API provided.  
This software is a heavy work in progress and probably shouldn't be used for anything remotely serious.  
Performance is most likely also worse than just using a native JVM language, considering all of the allocation
and lookups required for this to work.  

## Tour  
* /`RustLoader`  
  Bukkit plugin that registers a native library loader as a Bukkit plugin loader interface, granting the server the ability to load dynamic libraries as plugins (see [NativeBukkit](https://github.com/jarcode-foss/NativeBukkit)).
* /`TestPlugin`
  A simple plugin that tests all functionality provided by `rustbukkit-sys`. Target binary should be placed in the `plugins` folder of the Bukkit server.
* /`rustbukkit`  
  Idiomatic Rust bindings to `rustbukkit-sys`. Plugins written in Rust should depend on this crate as opposed to the sys crate. Plugins written in other languages will have to depend on sys. The compiled library `rustbukkit_sys.dll` needs to be placed in `plugins/RustLoader/` for this project to work.  
  * ./`rustbukkit-sys`  
    C-FFI compatible API surface provided to plugins. Only non-Rust plugins should depend on this crate (via the provided C header).  
    * ./`c_headers`  
      Non-Rust plugins should depend on `rustbukkit.h` from this folder. This header is generated via the `gen_bindings.ps1` script.
    * ./`gen_bindings.ps1`  
      Automatically generates both the C header and jextract Java sources for use in `RustLoader` (Java sources are automatically copied into the package `me.danny.nativeplug.jextract_gen`). Requires `jextract` (currently using the prebuilt binary provided by the OpenJDK website).

## Skeleton Rust Plugin  
In `Cargo.toml`:
```toml
[lib]
crate-type = ["cdylib"]

[dependencies]
# Use a specific version instead of "*"
rustbukkit = "*"
```

In `lib.rs`:
```rust
/// All API methods go through the RustBukkit value
use rustbukkit::RustBukkit;

/// JavaPlugin#onLoad
#[no_mangle]
pub fn on_load(bukkit: RustBukkit) {}

/// JavaPlugin#onEnable
#[no_mangle]
pub fn on_enable(bukkit: RustBukkit) {}

/// JavaPlugin#onDisable
#[no_mangle]
pub fn on_disable(bukkit: RustBukkit) {}
```

```bash
$ cargo build --release
$ cp target/release/$CRATE_NAME.dll $SERVER_PATH/plugins/
```

### Credits  
As stated above, this project was *originally* inspired by [NativeBukkit](https://github.com/jarcode-foss/NativeBukkit). Before [Project Panama](https://openjdk.org/projects/panama/) was in stable Java, this project used JNI ([623f1dd](https://github.com/dashaw92/RustBukkit/commit/623f1dd70d5f624d62afef92b72f3d92800321a1)), which is identical to how NativeBukkit works. After converting this to using Panama's FFM APIs, I feel this it's original enough to take full credit for it.  