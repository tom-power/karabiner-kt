# karabiner-kt

Core classes/functions for [karabiner-kt](https://github.com/kaushikgopal/karabiner-kt)

### Installation

Import to your project using [source-dependencies](https://blog.gradle.org/introducing-source-dependencies).

`settings.gradle.kts`

```kotlin
sourceControl {
    gitRepository(URI("https://github.com/tom-power/karabiner-kt")) {
        producesModule("karabiner-kt:core")
    }
}
```

`build.gradle.kts`

```kotlin
implementation("karabiner-kt:core:1.1.1")
```