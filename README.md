# InspectorViewApp

## Overview
InspectorViewApp is a modern Android application scaffolded with a multi-module architecture, using Kotlin, Jetpack Compose for UI, and Hilt for dependency injection. This project is currently in a minimal state, displaying a simple "Hello World" screen as a clean foundation for future development.

## Project Structure
- **:app** — Main application module (entry point, UI, DI setup)
- **:data** — (Empty) Reserved for future data sources (e.g., Room, network)
- **:domain** — (Empty) Reserved for business logic and use cases
- **:ui** — (Empty) Reserved for reusable UI components

## Tech Stack
- **Kotlin** — Modern, concise programming language for Android
- **Jetpack Compose** — Declarative UI framework
- **Hilt** — Dependency injection
- **JUnit** — Unit testing (minimal setup)
- **ktlint, Android Lint** — Code quality tools

## Current Features
- Minimal Compose UI: Displays a centered "Hello World" on launch
- Multi-module structure for scalable development
- Hilt setup for dependency injection

## Getting Started
1. **Clone the repository**
2. **Open in Android Studio** (Giraffe or newer recommended)
3. **Build the project** (Gradle sync should succeed with no errors)
4. **Run on an emulator or device** — You should see a centered "Hello World" message

## How to Expand
- Add features by implementing code in the `data`, `domain`, and `ui` modules
- Use Hilt for dependency injection in new ViewModels, repositories, etc.
- Follow MVVM and modular best practices

## License
This project is provided as a template for rapid Android development. Add your own license as needed.
