package com.rizwan.chatapplication.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwan.chatapplication.R
import com.rizwan.chatapplication.data.Chat
import com.rizwan.chatapplication.data.Person
import com.rizwan.chatapplication.ui.theme.Grey40
import com.rizwan.chatapplication.ui.theme.Magenta40
import com.rizwan.chatapplication.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date

@Composable
fun ChatScreen() {

    val viewModel: MainViewModel = hiltViewModel()
    val chats: List<Chat> by viewModel.getChats().observeAsState(emptyList())

    val focusManager = LocalFocusManager.current

    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            UserNameRow(
                person = Person(name = "Sarah", icon = R.drawable.ic_launcher_background),
                modifier = Modifier.padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White, RoundedCornerShape(
                            topStart = 30.dp, topEnd = 30.dp
                        )
                    )
                    .padding(top = 25.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(
                        start = 15.dp,
                        top = 25.dp,
                        end = 15.dp,
                        bottom = 75.dp
                    )
                ) {
                    items(chats) {
                        ChatRow(chat = it)
                        if (it.id % 5 == 0) {
                            Text(
                                text = "Thursday",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        CustomTextField(
            text = message, onValueChange = { message = it },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(BottomCenter)
        )
    }

}

@Composable
fun ChatRow(
    chat: Chat
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (!chat.isSelf) Alignment.Start else Alignment.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (chat.isSelf) Magenta40 else Grey40,
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (chat.isSelf) 20.dp else 0.dp,
                        bottomEnd = if (chat.isSelf) 0.dp else 20.dp
                    )
                ),
            contentAlignment = Center
        ) {
            Text(
                text = chat.message, style = TextStyle(
                    color = if (chat.isSelf) Color.White else Color.Black,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
                textAlign = TextAlign.End
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text, onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.type_message),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = { CommonIconButton(imageVector = Icons.Default.Add) },
        trailingIcon = { CommonIconButton(imageVector = Icons.Default.Add) },
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape
    )

}

@Composable
fun CommonIconButton(
    imageVector: ImageVector
) {

    Box(
        modifier = Modifier
            .background(Magenta40, CircleShape)
            .size(33.dp), contentAlignment = Center
    ) {
        Icon(imageVector = imageVector, contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.White)
    }

}

@Composable
fun CommonIconButtonDrawable(
    @DrawableRes icon: Int
) {
    Box(
        modifier = Modifier
            .background(Magenta40, CircleShape)
            .size(33.dp), contentAlignment = Center
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(15.dp)
        )
    }

}

@Composable
fun UserNameRow(
    modifier: Modifier = Modifier,
    person: Person
) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = hiltViewModel()

    var isSelf by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                painter = painterResource(id = person.icon),
                contentDescription = "",
                modifier = Modifier.size(42.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = person.name, style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Switch(checked = isSelf, onCheckedChange = {
                isSelf = it
            })
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "",
            modifier = modifier
                .size(24.dp)
                .clickable { coroutineScope.launch {
                    viewModel.addChat(
                        Chat(
                            message = "Shaddy",
                            isSelf = isSelf,
                            time = Calendar.getInstance().timeInMillis
                        )
                    )
                } },
            tint = Color.White
        )
    }

}