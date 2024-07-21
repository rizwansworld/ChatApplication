package com.rizwan.chatapplication.components

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwan.chatapplication.R
import com.rizwan.chatapplication.data.Chat
import com.rizwan.chatapplication.data.Person
import com.rizwan.chatapplication.ui.theme.Grey40
import com.rizwan.chatapplication.ui.theme.Grey80
import com.rizwan.chatapplication.ui.theme.Magenta40
import com.rizwan.chatapplication.utils.TimeUtils
import com.rizwan.chatapplication.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun ChatScreen() {

    val viewModel: MainViewModel = hiltViewModel()
    val chats: List<Chat> by viewModel.getChats().observeAsState(emptyList())
    val chatsListState = rememberLazyListState()

    val focusManager = LocalFocusManager.current
    var isSelf by remember { mutableStateOf(true) }

    LaunchedEffect(chats) {
        chatsListState.animateScrollToItem(chatsListState.layoutInfo.totalItemsCount)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            UserNameRow(
                person = Person(name = "Sarah", icon = R.drawable.ic_launcher_background),
                modifier = Modifier
                    .shadow(elevation = 20.dp, spotColor = Color.Transparent)
                    .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 20.dp),
                isSelf = isSelf,
                onCheckedSelfSwitch = { isSelf = it }
            )

            LazyColumn(
                state = chatsListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 15.dp, top = 0.dp, end = 15.dp, bottom = 95.dp
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                itemsIndexed(chats) { index, chat ->
                    if (index == 0 || TimeUtils.isMoreThanAnHour(chats[index-1].time, chat.time)) {
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Grey80,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(TimeUtils.getFormattedDay(chat.time))
                            }
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    color = Grey80,
                                    fontWeight = FontWeight.W300,
                                )
                            ) {
                                append(TimeUtils.getFormattedTime(chat.time))
                            }
                        }
                        Text(
                            text = annotatedString,
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    ChatRow(chat = chat)
                }
            }
        }

        CustomTextField(
            modifier = Modifier
                .align(BottomCenter),
            isSelf = isSelf
        )
    }

}

@Composable
fun ChatRow(
    chat: Chat
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 5.dp,
                    start = if (chat.isSelf) 90.dp else 0.dp,
                    end = if (chat.isSelf) 0.dp else 90.dp
                )
                .align(if (!chat.isSelf) Alignment.CenterStart else Alignment.CenterEnd)
                .background(
                    if (chat.isSelf) Magenta40 else Grey40,
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (chat.isSelf) 18.dp else 0.dp,
                        bottomEnd = if (chat.isSelf) 0.dp else 18.dp
                    )
                ),
            contentAlignment = Center
        ) {
            Text(
                text = chat.message, style = TextStyle(
                    color = if (chat.isSelf) Color.White else Color.Black,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp),
                textAlign = TextAlign.End
            )
        }
    }

}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    isSelf: Boolean
) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = hiltViewModel()

    var message by remember { mutableStateOf("") }

    Box(modifier = modifier.padding(20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message, onValueChange = { message = it },
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Magenta40,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier
                    .weight(1f),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(20.dp))
            FilledIconButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.addChat(
                            Chat(
                                message = message,
                                time = Calendar.getInstance().timeInMillis,
                                isSelf = isSelf
                            )
                        )
                        message = ""
                    }
                },
                modifier = Modifier.size(55.dp),
                enabled = message.isNotBlank(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Magenta40,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    modifier = Modifier.size(30.dp),
                    contentDescription = stringResource(R.string.send_message)
                )
            }
        }
    }

}

@Composable
fun UserNameRow(
    modifier: Modifier = Modifier,
    person: Person,
    isSelf: Boolean,
    onCheckedSelfSwitch: (Boolean) -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "",
                modifier = Modifier.size(72.dp),
                tint = Magenta40
            )
            Icon(
                painter = painterResource(id = person.icon),
                contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = person.name, style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Other", style = TextStyle(
                    color = Magenta40,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Switch(checked = isSelf, colors  = SwitchDefaults.colors(
                checkedTrackColor = Color.Black,
            ), onCheckedChange = {
                onCheckedSelfSwitch(it)
            })
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Self", style = TextStyle(
                    color = Magenta40,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )
        }
    }

}