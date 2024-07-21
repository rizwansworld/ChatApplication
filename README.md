**Decisions:**

1. Persist chats between users to Room Database.
   - **Why:**
     - Android DataStore or SharedPref is not an option as the data to persist is not small in size.
     - Also DataStore is an alternative to SharedPref where data like user session details or UI updates can be persisted.
     - Room makes it easy to query the data, such as a retrieving chats from a specific date and time to delete etc.

2. getChats() in Repository returns LiveData<List<Chat>>, which can be directly observed as state in our composable.
   - **Why:**
     - When SQL statement returns LiveData, it is already performed in background thread and returned to us.
     - No need of additional LiveData mapping in ViewModel.
     - No need of invoking suspend function for getChats() - Room takes care of Background threading.
     - addChat() is a suspend functon though, it is database heavy operation, and cannot be done in UI.

4. Hilt for Dependency Injection
   - **Why:**
     - Will single instance of ChatDatabase to entire app automatically.
     - No need of maintaining a single instance object in ChatDatabase class.
     - Makes it easier to test Room database queries, using a TestModule.

5. Jetpack Compose for UI
   - Developed the required UI quickly with less prone to RecyclerView errors
   - LazyColumn made it easy to add previous time stamp logic and Timestamp view in between the ChatRow
   - This would have been cumbersome in traditional views, with multiple viewType logic in RecyclerView.
   - State Hoisting made it easy trigger updates among UI elements -
     - Disabling Send button when TextField is empty
     - The toggle isSelf to trigger Chat user type
     - Creating custom text field was easy.

6. Creating shadows between UI elements is easier in Compose. Faster than traditional views.

**Architecure:**

MVVM
- ViewModel can be tested in isolation. 
- Here, since there is no Network Layer, there is no MutableLiveData to test, in response to API call response and persisted data response.
- But if there was, we could test the suspended coroutine functions in those specific TestDispatchers.

- Repository and UseCase can be tested in isolation.
- Espresso tests can be run on even the simple composables.
    
