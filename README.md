# Taming Karabiner with Kotlin

[Karabiner](https://karabiner-elements.pqrs.org/) is a Mac essential. It remaps keyboard strokes via `.json` rules. Powerful, yes. But `.json` for complex rules? Unwieldy. Quickly.

## Beyond JSON: The Configuration Maze

I spent years with [Goku](https://github.com/yqrashawn/GokuRakuJoudo). Its [EDN](https://github.com/edn-format/edn) format was terse—often too terse, becoming inscrutable.

Then, an AI deep-dive (via Raycast Pro) led me to a [video](https://www.youtube.com/watch?v=m5MDv9qwhU8) with [Max Stoiber](https://mxstbr.com/). He had a [TypeScript](https://github.com/mxstbr/karabiner) take on Karabiner configurations. His abstraction was simpler, more intuitive, and closer to the `karabiner.json` output.

## The Kotlin Leap: From AI Tweaks to a Full Rewrite

My journey involved a few steps:
1.  **AI-Powered Porting:** I first used AI to adapt Max's TypeScript to my long-standing Karabiner setup.
2.  **Refining Abstractions:** With AI's help, I further tweaked the TypeScript, aiming for a cleaner structure tailored to my needs.
3.  **The Kotlin Choice:** Ultimately, TypeScript wasn't clicking. Learning and refactoring it felt like a drag. Kotlin is my native tongue. I knew I could build something better, faster.

This repo is the result: my personal Karabiner setup, entirely in Kotlin.

## Get Started

```shell
brew install gradle
make
```