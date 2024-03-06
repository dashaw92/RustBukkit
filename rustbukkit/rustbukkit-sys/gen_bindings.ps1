Write-Host "src\*.rs -> .\c_headers\rustbukkit.h"
cbindgen.exe -l c . -o .\c_headers\rustbukkit.h
Write-Host "c_headers\rustbukkit.h -> java_generated\*.java"
.\jextract-22\bin\jextract.bat -t "me.danny.nativeplug.jextract_gen" --output java_generated .\c_headers\rustbukkit.h
Write-Host "cp -R .\java_generated\* -> ..\..\RustLoader\src\main\java\me\danny\nativeplug\*"
Copy-Item -Recurse .\java_generated\me\danny\nativeplug\jextract_gen\ ..\..\RustLoader\src\main\java\me\danny\nativeplug\ -Force
Write-Host "Done"
