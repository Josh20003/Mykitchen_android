# MyKitchen Android Project - File Structure

## Complete Project Tree

```
Mykitchen/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mykitchen/
│   │   │   │   ├── data/
│   │   │   │   │   ├── Recipe.kt                          # Recipe data model
│   │   │   │   │   └── User.kt                            # User data model
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── LoginScreen.kt                 # Login/Sign In UI
│   │   │   │   │   │   ├── SignUpScreen.kt                # Registration UI
│   │   │   │   │   │   ├── DashboardScreen.kt             # Main recipe list UI
│   │   │   │   │   │   ├── AddRecipeScreen.kt             # Create recipe form
│   │   │   │   │   │   └── ProfileScreen.kt               # User profile & stats
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── Components.kt                  # Reusable UI components
│   │   │   │   │   │   │   ├── LogoSection
│   │   │   │   │   │   │   ├── RecipeCard
│   │   │   │   │   │   │   └── StatisticItem
│   │   │   │   │   │   └── BottomNavigation.kt            # Bottom nav bar
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   ├── Screen.kt                      # Navigation route definitions
│   │   │   │   │   │   └── AppNavGraph.kt                 # Navigation graph setup
│   │   │   │   │   ├── viewmodel/
│   │   │   │   │   │   └── AppViewModel.kt                # App state management
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt                       # Color definitions
│   │   │   │   │       ├── Theme.kt                       # Material3 theme setup
│   │   │   │   │       └── Type.kt                        # Typography definitions
│   │   │   │   ├── BakingScreen.kt                        # (Legacy - AI recipe helper)
│   │   │   │   ├── BakingViewModel.kt                     # (Legacy)
│   │   │   │   ├── UiState.kt                             # (Legacy)
│   │   │   │   └── MainActivity.kt                        # App entry point
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── baked_goods_1.jpg
│   │   │   │   │   ├── baked_goods_2.jpg
│   │   │   │   │   ├── baked_goods_3.jpg
│   │   │   │   │   ├── ic_launcher_background.xml
│   │   │   │   │   └── ic_launcher_foreground.xml
│   │   │   │   ├── mipmap-anydpi-v26/
│   │   │   │   │   ├── ic_launcher_round.xml
│   │   │   │   │   └── ic_launcher.xml
│   │   │   │   ├── mipmap-hdpi/
│   │   │   │   │   ├── ic_launcher_round.webp
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-mdpi/
│   │   │   │   │   ├── ic_launcher_round.webp
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xhdpi/
│   │   │   │   │   ├── ic_launcher_round.webp
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xxhdpi/
│   │   │   │   │   ├── ic_launcher_round.webp
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xxxhdpi/
│   │   │   │   │   ├── ic_launcher_round.webp
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml                         # Color resources
│   │   │   │   │   ├── strings.xml                        # String resources
│   │   │   │   │   └── themes.xml                         # Theme configuration
│   │   │   │   └── xml/
│   │   │   │       ├── backup_rules.xml
│   │   │   │       └── data_extraction_rules.xml
│   │   │   └── AndroidManifest.xml                        # App manifest
│   │   ├── test/
│   │   │   └── java/com/example/mykitchen/
│   │   │       └── ExampleUnitTest.kt                     # Unit tests
│   │   └── androidTest/
│   │       └── java/com/example/mykitchen/
│   │           └── ExampleInstrumentedTest.kt             # Instrumented tests
│   ├── build.gradle.kts                                  # App-level build config
│   └── proguard-rules.pro                                # ProGuard rules
├── gradle/
│   ├── libs.versions.toml                                # Dependency versions
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts                                     # Root-level build config
├── gradlew                                              # Gradle wrapper (Unix)
├── gradlew.bat                                          # Gradle wrapper (Windows)
├── local.properties                                     # Local SDK configuration
├── settings.gradle.kts                                  # Project settings
├── README.md                                            # Main documentation
├── IMPLEMENTATION_SUMMARY.md                            # Implementation details
├── API_INTEGRATION_GUIDE.md                             # Backend integration guide
└── PROJECT_STRUCTURE.md                                 # This file
```

## Key Directories

### `/app/src/main/java/com/example/mykitchen/`
**Purpose**: Main Kotlin source code  
**Contains**: Activities, ViewModels, Screens, and Components

### `/app/src/main/res/`
**Purpose**: Android resources  
**Contains**: Colors, strings, drawables, layout XMLs

### `/app/src/test/`
**Purpose**: Unit tests  
**Contains**: JUnit test classes

### `/app/src/androidTest/`
**Purpose**: Instrumented tests  
**Contains**: AndroidX test classes for device/emulator testing

### `/gradle/`
**Purpose**: Gradle configuration  
**Contains**: Version catalog and wrapper files

## Important Files

### Configuration Files
- `build.gradle.kts` - Root project gradle configuration
- `app/build.gradle.kts` - App module gradle configuration
- `settings.gradle.kts` - Project settings and module inclusion
- `gradle/libs.versions.toml` - Centralized dependency management
- `local.properties` - Local development settings
- `gradlew` / `gradlew.bat` - Gradle wrapper scripts

### Manifest
- `AndroidManifest.xml` - App permissions, activities, and metadata

### Resources
- `colors.xml` - App color palette definitions
- `strings.xml` - All UI text strings (for localization support)
- `themes.xml` - Theme configuration

### Documentation
- `README.md` - User-facing documentation
- `IMPLEMENTATION_SUMMARY.md` - Technical implementation details
- `API_INTEGRATION_GUIDE.md` - Backend integration instructions
- `PROJECT_STRUCTURE.md` - This file

## Package Organization

```
com.example.mykitchen/
├── data/                    # Data models and repositories
├── ui/                      # User interface components
│   ├── screens/             # Full screen composables
│   ├── components/          # Reusable UI components
│   ├── navigation/          # Navigation setup
│   ├── viewmodel/           # ViewModel classes
│   └── theme/               # Theme and styling
└── MainActivity             # Application entry point
```

## Build Output Locations

```
app/
├── build/
│   ├── outputs/
│   │   ├── apk/
│   │   │   ├── debug/       # Debug APK files
│   │   │   └── release/     # Release APK files
│   │   └── bundle/          # Play Store bundles
│   ├── reports/
│   │   └── lint-results-*.html
│   └── ...
```

## Gradle Build System

### Build Configuration
- **Build Tool**: Gradle 8.11.1
- **Kotlin Version**: 2.0.21
- **Android Gradle Plugin**: 8.9.1
- **Target Gradle Version**: Latest in Compose catalog

### Dependency Management
Central version management through `libs.versions.toml`:
- All library versions defined in one place
- Easy to update multiple dependencies
- Promotes consistency across the project

## File Statistics

| Category | Count |
|----------|-------|
| Kotlin Files (Main) | 13 |
| Kotlin Files (Tests) | 2 |
| XML Resource Files | 5 |
| Image Assets | 13 |
| Configuration Files | 4 |
| Documentation Files | 3 |
| **Total** | **40+** |

## Code Organization Best Practices

### By Layer
1. **Data Layer** (`data/`)
   - Models (Recipe, User)
   - Repositories (future)
   - Network clients (future)

2. **UI Layer** (`ui/`)
   - Screens (Full-screen composables)
   - Components (Reusable widgets)
   - ViewModels (State management)
   - Theme (Styling and colors)

3. **Navigation** (`ui/navigation/`)
   - Screen routes
   - Navigation graph setup

### Naming Conventions
- **Screens**: `*Screen.kt` (e.g., `LoginScreen.kt`)
- **ViewModels**: `*ViewModel.kt` (e.g., `AppViewModel.kt`)
- **Components**: `*Component.kt` or grouped in `Components.kt`
- **Packages**: lowercase, descriptive names
- **Classes**: PascalCase
- **Functions/Variables**: camelCase

## Scalability Notes

### Current Structure Supports
- ✅ Adding new screens
- ✅ Adding new data models
- ✅ Expanding navigation
- ✅ Adding new UI components
- ✅ Offline-first with Room DB (when added)
- ✅ Multiple feature modules

### Growth Path
As the app grows, consider:
1. Breaking UI layer into feature modules
2. Adding dependency injection (Hilt)
3. Separating data layer with repositories
4. Adding network layer (Retrofit + OkHttp)
5. Implementing local database (Room)

## Maintenance Notes

### Regular Updates
- Monitor dependency versions in `libs.versions.toml`
- Update Kotlin compiler regularly
- Keep Compose version current
- Review Android gradle plugin updates

### Testing Strategy
- Unit tests in `/test/`
- Integration tests in `/androidTest/`
- Compose UI tests for screens
- ViewModel tests for state management

### Code Quality
- Follow Kotlin style guide
- Use IDE inspections
- Run lint checks: `./gradlew lint`
- Keep functions small and focused
- Comment complex logic

---

**Created**: March 18, 2026  
**Last Updated**: March 18, 2026  
**Status**: ✅ Complete and Buildable

