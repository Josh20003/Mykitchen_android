# MyKitchen - Mobile Recipe Management App

A comprehensive Android mobile application for managing and sharing dorm-friendly recipes. This is the mobile version of the MyKitchen web application, built with Jetpack Compose and modern Android development practices.

## Screenshots

### Register Screen
New users can create an account with their name, email, and password.

![Register Screen](/screenshot2.png)

### Login Screen
Existing users can sign in with their email and password.

![Login Screen](/screenshot.png)

### Dashboard Screen
The main dashboard displays user statistics and quick actions.

![Dashboard Screen](/screenshot3.png)

### Profile Screen
View user profile information and access account settings.

![Profile Screen](/current_screen.png)

### Update Profile Screen
Update user profile information including name and email.

![Update Profile Screen](/app_design.png)

### Change Password Screen
Securely change account password with current password verification.

![Change Password Screen](/login_design.png)

---

## Features

### 📱 Authentication
- **Login Screen**: Existing users can sign in with email and password
- **Sign Up Screen**: New users can create an account with email, username, and password
- Simple authentication flow with form validation

### 🏠 Dashboard
- **Recipe List**: View all saved recipes in a scrollable list
- **Search Functionality**: Filter recipes by title or description
- **Quick Recipe Addition**: Easy button to add new recipes
- **Empty State**: Helpful message and CTA when no recipes are saved yet
- **User Header**: Display logged-in user information with avatar

### ➕ Add Recipe
- **Form Validation**: Required fields for recipe details
- **Recipe Title**: Name of the recipe
- **Description**: Detailed instructions and ingredients
- **Cook Time**: Duration in minutes with numeric input
- **Category Selection**: Choose from:
  - Fried
  - Microwave
  - No-Cook
- **Save & Cancel**: Options to save or cancel the form

### 👤 Profile
- **User Information**: Display username and email
- **Edit Profile**: Ability to update profile information (UI ready)
- **Statistics Dashboard**: Visual display of recipe collection stats:
  - Total Recipes: Count of all saved recipes
  - Microwave Recipes: Count of microwave-friendly recipes
  - No-Cook Recipes: Count of no-prep recipes
- **Logout**: Securely log out from the app

## Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Compose Navigation
- **State Management**: ViewModel with StateFlow
- **Target SDK**: 36
- **Min SDK**: 24

### Project Structure

```
com.example.mykitchen/
├── data/
│   ├── Recipe.kt         # Recipe data model
│   └── User.kt           # User data model
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt
│   │   ├── SignUpScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── AddRecipeScreen.kt
│   │   └── ProfileScreen.kt
│   ├── components/
│   │   ├── Components.kt        # Shared UI components (LogoSection, RecipeCard, StatisticItem)
│   │   └── BottomNavigation.kt   # Navigation controls
│   ├── navigation/
│   │   ├── Screen.kt           # Navigation routes
│   │   └── AppNavGraph.kt       # Navigation graph
│   ├── viewmodel/
│   │   └── AppViewModel.kt      # Main app state management
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```

## Key Components

### Data Models

#### Recipe
```kotlin
data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val cookTime: Int = 0,
    val category: String = "Fried",
    val userId: String = ""
)
```

#### User
```kotlin
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val totalRecipes: Int = 0,
    val microwaveRecipes: Int = 0,
    val noCookRecipes: Int = 0
)
```

### ViewModel
The `AppViewModel` handles:
- User authentication (login/signup)
- Recipe management (add/delete)
- User statistics calculation
- Application state management

## Navigation Flow

```
Login Screen ──→ Dashboard ──→ Profile
       ↓              ↓
   Sign Up ──→ Add Recipe
```

- **Login/Sign Up**: Authentication flow
- **Dashboard**: Main screen showing recipes with search
- **Add Recipe**: Form to create new recipes
- **Profile**: User info and statistics

---

## API Documentation

### Base URL
```
https://api.example.com/
```

### Authentication
All API endpoints (except login and register) require Bearer token authentication:
```
Authorization: Bearer {token}
```

### Endpoints

#### Authentication

##### POST /api/login
Login with email and password.

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "created_at": "2026-03-28T10:00:00Z",
    "updated_at": "2026-03-28T10:00:00Z"
  }
}
```

##### POST /api/register
Register a new user account.

**Request:**
```json
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password123",
  "password_confirmation": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "created_at": "2026-03-28T10:00:00Z",
    "updated_at": "2026-03-28T10:00:00Z"
  }
}
```

##### POST /api/logout
Logout the current user.

**Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

#### Profile

##### GET /api/profile
Get the current user's profile information.

**Response:**
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "created_at": "2026-03-28T10:00:00Z",
    "updated_at": "2026-03-28T10:00:00Z"
  }
}
```

##### PUT /api/profile
Update the current user's profile.

**Request:**
```json
{
  "name": "John Updated",
  "email": "newemail@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "user": {
    "id": 1,
    "name": "John Updated",
    "email": "newemail@example.com",
    "created_at": "2026-03-28T10:00:00Z",
    "updated_at": "2026-03-28T12:00:00Z"
  }
}
```

##### PUT /api/profile/password
Change the current user's password.

**Request:**
```json
{
  "current_password": "oldpassword123",
  "new_password": "newpassword123",
  "new_password_confirmation": "newpassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Password changed successfully"
}
```

#### Dashboard

##### GET /api/dashboard
Get dashboard data including user info and statistics.

**Response:**
```json
{
  "success": true,
  "message": "Dashboard data retrieved successfully",
  "data": {
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "user@example.com"
    },
    "stats": {
      "recipes": 12,
      "favorites": 5,
      "total_cook_time": 180
    }
  }
}
```

### Error Responses

All endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "errors": {
    "field_name": ["Error message for this field"]
  }
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| 200  | Success |
| 201  | Created |
| 400  | Bad Request - Invalid input data |
| 401  | Unauthorized - Invalid or missing token |
| 403  | Forbidden - Insufficient permissions |
| 404  | Not Found - Resource not found |
| 422  | Validation Error - Input validation failed |
| 500  | Server Error - Internal server error |

---

## Color Scheme

- **Primary Orange**: #FFEE7B00
- **Dark Orange**: #FFC95C00
- **Light Gray**: #FFF5F5F5
- **Dark Gray**: #FF666666

## Building & Running

### Prerequisites
- Android Studio with Kotlin support
- Android SDK 36 or higher
- Gradle 8.11.1 or compatible

### Build Steps
```bash
# Clone the repository
git clone https://github.com/Josh20003/Mykitchen.git

# Navigate to project
cd Mykitchen

# Build the project
./gradlew build

# Run on emulator
./gradlew installDebug
```

### Build Variants
- **Debug**: Development build with debugging tools enabled
- **Release**: Optimized production build with ProGuard obfuscation

## Future Enhancements

- **Backend Integration**: Connect to REST API for persistent data storage
- **Image Upload**: Allow users to add recipe photos
- **Favorites**: Mark recipes as favorites
- **Share Recipes**: Share recipes with other users
- **Recipe Ratings**: Rate and review recipes
- **Nutrition Info**: Display nutritional information
- **Offline Support**: Cache recipes for offline access
- **Dark Mode**: Full dark theme support
- **Firebase Integration**: Real-time database and authentication

## Dependencies

### Core
- androidx.core:core-ktx:1.18.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.10.0
- androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0
- androidx.activity:activity-compose:1.13.0

### Compose
- androidx.compose.bom:2024.09.00
- androidx.compose.ui:ui
- androidx.compose.material3:material3
- androidx.navigation:navigation-compose:2.7.7

### Testing
- junit:junit:4.13.2
- androidx.test.ext:junit:1.3.0
- androidx.test.espresso:espresso-core:3.7.0

## File Structure

```
app/
├── build.gradle.kts
├── proguard-rules.pro
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/mykitchen/
│   │   └── res/
│   │       ├── drawable/
│   │       ├── mipmap-*/
│   │       ├── values/
│   │       │   ├── colors.xml
│   │       │   ├── strings.xml
│   │       │   └── themes.xml
│   │       └── xml/
│   ├── test/
│   └── androidTest/
```

## License

This project is part of the MyKitchen application ecosystem. 

## Contact

For questions or feedback, please contact: josh.contact@example.com

---

**Version**: 1.0.0  
**Last Updated**: March 2026  
**Target SDK**: 36  
**Min SDK**: 24

