# karabiner-kt

A Kotlin DSL for generating complex [Karabiner-Elements](https://karabiner-elements.pqrs.org/) configurations with ease and clarity.

## The Challenge with `karabiner.json`

Karabiner is incredibly powerful for keyboard customization on macOS. However, its native `.json` configuration can become unwieldy and difficult to manage as your rules grow in complexity. Readability suffers, and maintaining intricate setups becomes a chore.

## The Journey to a Kotlin DSL

Many users have sought better ways to manage Karabiner configurations:
*   **[Goku](https://github.com/yqrashawn/GokuRakuJoudo):** Offered a terse [EDN](https://github.com/edn-format/edn) format, which, while concise, could sometimes be too opaque.
*   **TypeScript Abstractions:** Inspired by projects like [Max Stoiber\'s TypeScript solution](https://github.com/mxstbr/karabiner) (highlighted in [this video](https://www.youtube.com/watch?v=m5MDv9qwhU8)), the idea of a higher-level language for configuration showed promise. Initial explorations involved AI-assisted porting and refinement of TypeScript abstractions.

Ultimately, the familiarity and expressive power of Kotlin led to the development of `karabiner-kt`. Kotlin allows for a more intuitive and maintainable way to define sophisticated keyboard mappings compared to raw JSON or even other scripting approaches.

## How It Works

With `karabiner-kt`, you define your Karabiner rules using a Kotlin-based Domain Specific Language (DSL). This allows you to leverage the full power of Kotlin (types, functions, variables, control flow) to structure your configuration logically.

**Example (Illustrative Kotlin DSL):**

```kotlin
// In your Kotlin configuration file (e.g., src/main/kotlin/Main.kt)
import KarabinerKt.* // Fictional import for the DSL

fun main() {
    karabinerConfig {
        profile(name = "Default Profile") {
            simpleModification {
                from(key_code = "caps_lock")
                to(key_code = "escape")
            }
            // Add more complex rules, layers, and conditional modifications here
            // For example:
            // rule("Hyper Key with Caps Lock") {
            //     manipulator {
            //         from(key_code = "caps_lock", modifiers(optional = ["any"]))
            //         to(key_code = "left_shift", modifiers = ["left_control", "left_option", "left_command"])
            //     }
            //     manipulator { // Allow Caps Lock to function as Escape if tapped alone
            //         from(key_code = "caps_lock")
            //         to(key_code = "escape")
            //         conditions = [condition(type = "variable_if", name = "caps_lock_pressed_alone", value = 1)]
            //     }
            // }
        }
    }
}

// Note: The actual DSL structure will be defined by this project.
// The `make` command will compile this Kotlin code and generate the final karabiner.json.
```

Running the build process (typically via `make`) compiles your Kotlin code and generates the `karabiner.json` file, which Karabiner-Elements then uses.

## Getting Started

### Prerequisites
*   [Karabiner-Elements](https://karabiner-elements.pqrs.org/) installed.
*   [Gradle](https://gradle.org/) for building the Kotlin project.

### Installation & Setup

```shell
brew install --cask karabiner-elements
brew install gradle
rm ~/.config/karabiner
mkdir -p ~/.config/karabiner
git clone https://github.com/kaushikgopal/karabiner-kt.git ~/.config/karabiner/karabiner-kt
cd ~/.config/karabiner/karabiner-kt
make
launchctl kickstart -k gui/(id -u)/org.pqrs.service.agent.karabiner_console_user_server
```


This repository represents a personal setup and an approach to managing Karabiner configurations. Feel free to adapt it to your needs!