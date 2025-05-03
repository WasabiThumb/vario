# <img src="https://raw.githubusercontent.com/WasabiThumb/vario/refs/heads/master/doc/icon.png" style="height: 1em;" alt=""> vario
Java 8 library for reading/writing variable-length primitives (VarInt, VarUInt, VarFloat, VarDouble)

## Quick Start
### Declaration
#### Gradle (Kotlin)
```kotlin
dependencies {
    implementation("io.github.wasabithumb:vario:0.1.0")
}
```

#### Gradle (Groovy)
```groovy
dependencies {
    implementation 'io.github.wasabithumb:vario:0.1.0'
}
```

#### Maven
```xml
<dependencies>
    <dependency>
        <groupId>io.github.wasabithumb</groupId>
        <artifactId>vario</artifactId>
        <version>0.1.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### Basic Usage
```java
VarDataOutputStream vdos = new VarDataOutputStream(os);

// Signed 32-bit integers
vdos.writeVarInt32(100);
vdos.writeVarInt32(-100);

// Unsigned 32-bit integers
vdos.writeVarUInt32(50);
vdos.writeVarUInt32(200);

// Signed 64-bit integers
vdos.writeVarInt64(Long.MIN_VALUE);
vdos.writeVarInt64(Long.MAX_VALUE);

// Unsigned 64-bit integers
vdos.writeVarUInt64(0x400000000L);
vdos.writeVarUInt64(0x800000000L);

// 32-bit floats
vdos.writeFloat(0.75f);
vdos.writeFloat(Float.NaN);

// 64-bit floats
vdos.writeDouble(1.25d);
vdos.writeDouble(Double.NaN);
```

## Credit
- VarFloat/VarDouble format courtesy of [michaeljclark/vf128](https://github.com/michaeljclark/vf128)

## License
```text
Copyright 2025 Wasabi Codes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
