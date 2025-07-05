
# TikTokLikeApp

**TikTokLikeApp** is an Android application developed in Android Studio using Java and Groovy DSL. The app emulates the core functionality of a TikTok-like vertical video scrolling platform, allowing users to embed and play YouTube videos in a seamless, swipeable interface. It includes features for managing YouTube video links locally using SQLite, with a focus on smooth video playback, efficient data handling, and an enhanced user experience.

## Features

- **Vertical Video Scrolling**: Swipe through embedded YouTube videos in a vertical, looping ViewPager2 interface, similar to TikTok.
- **YouTube Video Playback**: Automatically plays videos when swiped to, with pause functionality for non-visible videos to optimize performance.
- **Video Preloading and Caching**: Preloads upcoming videos and caches video metadata using SharedPreferences for faster load times.
- **Link Management**: Add, update, and delete YouTube embed links in a dedicated screen, stored locally in an SQLite database.
- **Error Handling**: Robust handling for network issues, invalid YouTube URLs, and database errors with user-friendly feedback.
- **Enhanced UI/UX**: Material Design components, progress indicators, and confirmation dialogs for a polished and intuitive user experience.
- **Main Navigation Hub**: A central screen to navigate between video scrolling and link management activities.

## Prerequisites

To build and run the project, ensure you have the following:

- **Android Studio**: Version 2023.3.1 or later (e.g., Android Studio Iguana).
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Android SDK**: Minimum SDK API 21 (Lollipop), Target SDK API 34.
- **YouTube Data API Key**: Obtain a key from the [Google Cloud Console](https://console.cloud.google.com/) to enable YouTube video playback.
- **Internet Connection**: Required for YouTube video streaming.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/tiktoklikeapp.git
   cd tiktoklikeapp
   ```

2. **Open in Android Studio**:
   - Launch Android Studio and select **Open an existing project**.
   - Navigate to the `TikTokLikeApp` directory and open it.

3. **Configure YouTube API Key**:
   - Replace `"YOUR_YOUTUBE_API_KEY"` in `VideoFragment.java` with your YouTube Data API key:
     ```java
     youTubePlayerView.initialize("YOUR_YOUTUBE_API_KEY", ...);
     ```
   - Secure the API key in production using environment variables or obfuscation.

4. **Sync Project**:
   - Click **Sync Project with Gradle Files** in Android Studio to download dependencies.
   - Ensure all dependencies in `app/build.gradle` are resolved.

5. **Build and Run**:
   - Connect an Android device (API 21 or higher) or use an emulator.
   - Click **Run > Run 'app'** to build and install the app.

6. **Grant Permissions**:
   - The app requires `INTERNET` and `WRITE_EXTERNAL_STORAGE` permissions, declared in `AndroidManifest.xml`. Grant these when prompted on the device.

## Project Structure

The project is organized as follows:

```
TikTokLikeApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/tiktoklikeapp/
│   │   │   │       ├── DatabaseHelper.java         # SQLite database operations for link storage
│   │   │   │       ├── Link.java                  # Model class for YouTube links
│   │   │   │       ├── LinkAdapter.java           # RecyclerView adapter for link management
│   │   │   │       ├── LinkManagementActivity.java # Activity for managing YouTube links
│   │   │   │       ├── MainActivity.java          # Main navigation hub
│   │   │   │       ├── VideoActivity.java         # Activity for vertical video scrolling
│   │   │   │       ├── VideoFragment.java         # Fragment for individual video playback
│   │   │   │       ├── VideoPagerAdapter.java     # ViewPager2 adapter for video fragments
│   │   │   │       └── VideoCacheManager.java     # Caches video metadata for optimization
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   └── ic_launcher_background.xml # App icon background
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml          # Main activity layout
│   │   │   │   │   ├── activity_link_management.xml # Link management layout
│   │   │   │   │   ├── activity_video.xml         # Video scrolling layout
│   │   │   │   │   ├── fragment_video.xml         # Video fragment layout
│   │   │   │   │   ├── item_link.xml              # RecyclerView item layout
│   │   │   │   │   └── dialog_update_link.xml     # Dialog layout for updating links
│   │   │   │   ├── mipmap/
│   │   │   │   │   ├── ic_launcher.png
│   │   │   │   │   └── ic_launcher_round.png
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml                 # Color resources
│   │   │   │   │   ├── strings.xml                # String resources
│   │   │   │   │   ├── themes.xml                 # App theme
│   │   │   │   │   └── styles.xml                 # Custom UI styles
│   │   │   ├── AndroidManifest.xml                # App manifest with permissions and activities
│   │   ├── build.gradle                           # Module-level Gradle configuration
├── build.gradle                                   # Project-level Gradle configuration
├── gradle.properties                              # Gradle properties
└── settings.gradle                                # Project settings
```

## Dependencies

The app relies on the following libraries, defined in `app/build.gradle`:

- **AndroidX AppCompat**: `androidx.appcompat:appcompat:1.6.1`
- **ConstraintLayout**: `androidx.constraintlayout:constraintlayout:2.1.4`
- **ViewPager2**: `androidx.viewpager2:viewpager2:1.0.0`
- **RecyclerView**: `androidx.recyclerview:recyclerview:1.3.2`
- **YouTube Android Player API**: `com.google.android.youtube:youtube-android-player-api:1.2.2`
- **Material Components**: `com.google.android.material:material:1.12.0`

## Usage

1. **Main Screen**:
   - Launch the app to access the main screen with two buttons:
     - **Watch Videos**: Navigates to the vertical video scrolling screen.
     - **Manage Links**: Navigates to the link management screen.

2. **Video Scrolling Screen**:
   - Swipe vertically to view YouTube videos embedded from stored links.
   - Videos auto-play when visible and pause when swiped away.
   - A progress indicator displays during video loading.

3. **Link Management Screen**:
   - **Add Link**: Enter a video name and YouTube embed URL (e.g., `https://www.youtube.com/embed/VIDEO_ID`) to save to the SQLite database.
   - **View Links**: Displays a list of saved links with names and URLs.
   - **Update Link**: Click "Update" to modify a link’s name and URL via a dialog.
   - **Delete Link**: Click "Delete" to remove a link, with a confirmation dialog.

## Error Handling

- **Network Errors**: Displays Toast messages for YouTube playback or API initialization failures.
- **Invalid URLs**: Validates YouTube embed URLs before saving, with user feedback for invalid inputs.
- **Database Errors**: Handles SQLite exceptions and informs users of operation failures.
- **User Feedback**: Uses Toast messages and dialogs to communicate errors clearly.

## UI/UX Features

- **Material Design**: Implements Material Buttons, TextInputLayouts, and dialogs for a modern look.
- **Progress Indicators**: Shows loading spinners during video playback initialization.
- **Confirmation Dialogs**: Ensures users confirm updates and deletions to prevent accidental changes.
- **Consistent Styling**: Uses custom styles and colors defined in `res/values/styles.xml` and `res/values/colors.xml`.

## Testing

To ensure the app functions correctly:

1. **Unit Tests**:
   - Test `DatabaseHelper` methods (`addLink`, `updateLink`, `deleteLink`) for correct CRUD operations.
   - Validate `VideoCacheManager` caching and retrieval logic.

2. **UI Tests**:
   - Verify navigation between `MainActivity`, `VideoActivity`, and `LinkManagementActivity`.
   - Test video playback, auto-play, and pause functionality in `VideoFragment`.
   - Confirm dialogs and progress indicators display as expected.

3. **Edge Cases**:
   - Simulate network failures to test error handling.
   - Input invalid YouTube URLs to verify validation.
   - Test with a large number of links to ensure database and UI scalability.

Use Android Studio’s **Logcat** to debug issues and monitor performance.

## Deployment

1. **Generate Signed APK**:
   - In Android Studio, go to **Build > Generate Signed Bundle/APK**.
   - Follow the prompts to create a signed APK for distribution.

2. **Secure API Key**:
   - Avoid hardcoding the YouTube API key in production. Use build configurations or a secure vault.

3. **Performance Optimization**:
   - Monitor memory usage of `ViewPager2` and `YouTubePlayerView` to prevent leaks.
   - Consider pagination for large link lists in `LinkManagementActivity`.

## Limitations and Future Improvements

- **Video Caching**: Currently caches only video IDs. Future versions could cache video thumbnails or metadata using a more robust solution like Room or external storage.
- **Authentication**: Add user authentication to support personalized link collections.
- **Advanced Playback Controls**: Implement features like video seeking, volume control, or full-screen mode.
- **Offline Support**: Cache videos for offline playback using a local media store.

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request with a detailed description of your changes.

Please adhere to the project’s coding standards and include tests for new features.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or support, contact the project maintainer at [trandinhhung717@gmail.com](mailto:trandinhhung717@gmail.com).
