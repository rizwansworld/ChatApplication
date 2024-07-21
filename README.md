App Screenshots:

<img width="399" alt="Screenshot 2024-07-21 at 14 51 23" src="https://github.com/user-attachments/assets/de2a5aad-2d0a-497e-b634-2069649fa258">

<img width="394" alt="Screenshot 2024-07-21 at 14 51 50" src="https://github.com/user-attachments/assets/68228072-aada-4dcc-8050-fdc976acd1b1">

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
   
7. Clicking on the list when Soft Keyboard is open, clears focus on the textfield and closes the Keybaord. Similar to popular Chat apps like Whatsapp.

**Architecure:**

MVVM
- ViewModel can be tested in isolation. 
- Here, since there is no Network Layer, there is no MutableLiveData to test, in response to API call response and persisted data response.
- But if there was, we could test the suspended coroutine functions in those specific TestDispatchers.

- Repository and UseCase can be tested in isolation.
- Espresso tests can be run on even the simple composables.

**App Limitations:**
1. Shadow above the TextField is not perfect as in UI. Due to time-constraints, couldn't play around with it.
  - If more time: Get the shadow right by ordering the composable inside Box.
    
2. The background of Back Button and Send button is a Gradient of Magenta and Yellow in given design, like the instagram logo. Only the color Magenta is added now.
   - If more time: Create gradient and add it as background for both. Its faster to create in Compose, compared to traditional view, where we had to create a separate gradient file.

3. WindowInsets Keyboard Animation can be added to enhance user experience when the keyboard opens.
   - Now, the list jumps suddenly to the top and then the keyboard animates to its place.
   - If more time: With the WindowInsets animation library, the list would scroll and animate along with the keyboard.

4. Reply capabilities like Swipe Reply to a ChatRow can be added. Users will like it, as we are all used to it on Whatsapp.
   
5. The Read receipt tick icon is not added to Self chats due to time-constraint.

6. Unit Testing is not done properly.
   - If more time: Dependency of each layer (For example, Repository), can be mocked using Mockito or MockK, and we can try to test whether each funtion is returning what its assumed to like error case, no internet case, no data from api, etc.
    
