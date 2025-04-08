# parking
demo https://youtu.be/EY3dFg_rsgc
# parking with Compose/Room/Hilt
demo*2 https://youtu.be/qQlsci7Ocho

# Jetpack Compose UI Components for a Simple App

This code snippet showcases several Jetpack Compose UI components used to build the user interface for a basic application, likely involving login, a list of items (potentially parking spaces), and a details screen.

## Overview

The code defines composable functions that create different screens and UI elements:

* **`MainScreen()`:** The entry point for the main screen. It uses a `MainViewModel` (likely using Hilt for dependency injection) to handle login logic. It displays a login form (`ColumnEditText`) and observes the loading state.
* **`BasicsSurfaceView()`:** A reusable composable that provides a `Surface` with a background color and handles an onboarding flow. It conditionally displays an `OnboardScreen` or the provided `content`.
* **`OnboardScreen()`:** A simple welcome screen with a "Continue" button.
* **`ColumnEditText()`:** A composable that displays two `OutlinedTextField`s for username and password input, along with a "Login" button. It takes a lambda function (`onClick`) to handle the login action with the entered credentials.
* **`SetState(viewModel: MainViewModel)`:** A private composable that observes the `MainViewModel`'s `userData` and `onFailure` `LiveData` to navigate to the entry screen upon successful login or display an error message on failure.
* **`EntryScreen()`:** Displays a list of parking spaces fetched from an `EntryViewModel`. It uses `BasicsSurfaceView` to provide the basic layout and a custom `ParkingList` to show the data. It also includes an app bar with a back button and a settings button.
* **`BasicsSurfaceView(parkingList: List<Parking>)`:** A specific implementation of `BasicsSurfaceView` for the entry screen, displaying a `ParkingList`. It also handles navigation to the main screen and a details screen.
* **`ParkingList()`:** Uses `LazyColumn` to efficiently display a scrollable list of `Parking` items using the `EntryCardView`.
* **`EntryCardView()`:** Represents a single parking item in the list. It displays basic information and expands to show more details on click, using `animateContentSize` for a smooth animation.
* **`SetState(viewModel: EntryViewModel)`:** Observes the `EntryViewModel`'s `onFailure` `LiveData` to display an error and navigate back to the previous screen.
* **`DetailScreen()`:** Displays details related to a user (likely the logged-in user). It fetches data using `DetailViewModel` and shows information like phone number and a list of time zones for setting.
* **`BasicsSurfaceView(timeZoneList: List<TimeZone>, viewModel: DetailViewModel)`:** A specific implementation of `BasicsSurfaceView` for the details screen, displaying user information and a list of `TimeZone` items.
* **`Items()`:** Uses `LazyColumn` to display a scrollable list of `TimeZone` items using `BaseCardView`.
* **`BaseCardView()`:** A reusable card view to display a `TimeZone` item with a click action.
* **`SetState(viewModel: DetailViewModel)`:** Observes the `DetailViewModel`'s `updateUser` and `onFailure` `LiveData` to show success or error messages after updating user data.
* **`OnSuccess()`:** A composable to display a simple success alert dialog.

## Key Components and Concepts Used

* **`@Composable`:** Marks functions that define UI elements.
* **`ViewModel` and `hiltViewModel()`:** Used for managing UI-related data and dependency injection.
* **`MutableState` and `rememberSaveable`:** Used for managing and preserving UI state across recompositions.
* **`by viewModel.isLoading` and `collectAsState()`:** Used to observe `StateFlow` and `LiveData` from the `ViewModel` and trigger UI updates.
* **`Surface`, `Column`, `Button`, `Text`, `OutlinedTextField`, `Card`, `IconButton`, `Icon`:** Basic Jetpack Compose layout and UI elements.
* **`LazyColumn`:** An efficient way to display scrollable lists that only composes and lays out items that are currently visible.
* **`Modifier`:** Used to configure the appearance and behavior of composable functions (e.g., `fillMaxSize()`, `padding()`, `clickable()`).
* **`Arrangement` and `Alignment`:** Used to control the layout of items within `Column` and `Row`.
* **`Shape` and `ButtonDefaults`:** Used to customize the appearance of buttons and other shaped elements.
* **`TextFieldValue`:** A class to hold the state of a `TextField`.
* **`LocalContext.current`:** Provides access to the current `Context`.
* **`NavController` and Navigation Compose:** Used for handling in-app navigation between different screens.
* **`getNavController()`, `getArgs()`:** Helper functions (likely custom extensions) for accessing the `NavController` and navigation arguments within composables.
* **`animateContentSize` and `spring()`:** Used to create smooth animations when the size of a composable changes.
* **`ConstraintLayout`:** A layout that positions and sizes its children relative to each other in a flexible way.
* **`MaterialTheme` and `typography`:** Provide access to the application's defined theme and text styles.
* **`Icons.Filled`:** Provides access to built-in Material Design icons.
* **`observeAsState()`:** An extension function to observe `LiveData` as a Compose `State`.

## Potential Features and Functionality

Based on the code, the application likely has the following features:

* **User Login:** Allows users to enter their username and password.
* **Onboarding Screen:** A welcome screen displayed to new users (or on the first launch).
* **List Display:** Shows a scrollable list of parking spaces with basic information.
* **Expandable List Items:** Each parking item can be expanded to show more details.
* **Navigation:** Includes navigation between the main login screen, a list screen, and a details screen.
* **Settings/Details:** A settings or details screen accessible from the list screen, potentially showing user-related information like phone number and allowing time zone selection.
* **Loading and Error Handling:** Indicates loading states and displays error messages to the user.
* **Success Feedback:** Shows a success message after actions like updating user data.

## Further Development

To make this a complete application, further development would involve:

* Implementing the `MainViewModel`, `EntryViewModel`, and `DetailViewModel` to handle data fetching, business logic, and API calls.
* Defining the data classes `LOGIN_001_Rq`, `LOGIN_001_Rs`, `Parking`, `EntryFragmentArgs`, `DetailFragmentArgs`, `UPDATE_001_Rq`, and `TimeZone`.
* Implementing the `Loading.hide()` and `OnError()` composables.
* Providing the implementation for `getNavController()` and `getArgs()`.
* Adding more UI elements and functionality to the details screen.
* Implementing proper error handling and user feedback mechanisms.
* Adding unit and integration tests.

This README provides a good starting point for understanding the structure and UI components used in this Jetpack Compose application.
androidxTestJunit    : '1.1.5',
androidxTestEspresso : '3.5.1',
