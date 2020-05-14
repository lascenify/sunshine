# sunshine
Weather forecast app written in Kotlin (in progress)

## Project Setup
1. Download or clone the project
> git clone https://github.com/lascenify/sunshine.git
2. Create an [OpenWeatherMap](https://openweathermap.org/) account and get an API key. 
3. Create a new resource bundle on the root of the project called "apikey.properties"
4. Add OPEN_WEATHER_API_KEY="insertHereYourApiKey" to the apikey.properties file.
5. Sync project with Gradle Files

## Libraries and tools

- [Dagger 2](https://dagger.dev/) -> Dependency Injection
- [Android Debug Database](https://github.com/amitshekhariitbhu/Android-Debug-Database)

### Android Jetpack Components:
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Navigation Component](https://developer.android.com/topic/libraries/architecture/navigation/) -> Navigation between fragments
- [Shared Preferences](https://developer.android.com/training/data-storage/shared-preferences) -> Local data persistence (settings)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding)
- [Room](https://developer.android.com/topic/libraries/architecture/room) -> Local data persistence

### Connection with API
- [Retrofit](https://square.github.io/retrofit/) -> Network Calls
- [OkHttp](https://square.github.io/okhttp) -> HTTP Client
- [Moshi](https://github.com/square/moshi) -> JSON Library
- [Stetho](https://github.com/facebook/stetho) -> Debug bridge. Used to create an interceptor with OkHttp.

### Testing
- [JUnit4](https://github.com/junit-team/junit4)
- [Mockito](https://github.com/mockito/mockito)
- [Hamcrest](https://github.com/hamcrest/JavaHamcrest)

## Design
Icons made by[Freepik](https://www.flaticon.com/authors/freepik) from www.flaticon.com



