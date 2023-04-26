package com.hana.somethingtocompose.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.hana.somethingtocompose.R
import com.hana.somethingtocompose.model.CommentMentionInfo


@Composable
fun MentionedListView() {


    val selectedFriends = remember {
        mutableStateListOf<CommentMentionInfo>()
    }

    val selectableFriends = mutableListOf<CommentMentionInfo>()

    repeat(20) {
        selectableFriends.add(
            CommentMentionInfo(
                userId = "$it",
                profileImage = "",
                nickName = "가나다 $it"
            )
        )
    }

    val localDensity = LocalDensity.current

    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }

    var fontSize by remember {
        mutableStateOf(20.dp)
    }

    var iconSize by remember {
        mutableStateOf(28.dp)
    }

    var currentSelectedSize by remember {
        mutableStateOf(0)
    }


    Column {
        FlowRow(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    val updatedHeight =
                        with(localDensity) { coordinates.size.height.toDp() }

                    if (currentSelectedSize != selectedFriends.size) {

                        var newTextSize = fontSize
                        var newIconSize = iconSize

                        if (selectedFriends.size < 3) {
                            newTextSize = 22.dp
                            newIconSize = 28.dp
                        } else if (updatedHeight - columnHeightDp > 10.dp) {
                            newTextSize =
                                fontSize - (0.05 * (updatedHeight - columnHeightDp))
                            newIconSize =
                                iconSize - (0.05 * (updatedHeight - columnHeightDp))
                        } else if (columnHeightDp - updatedHeight > 10.dp) {
                            newTextSize =
                                fontSize + (0.05 * (columnHeightDp - updatedHeight))
                            newIconSize =
                                iconSize + (0.05 * (columnHeightDp - updatedHeight))
                        }

                        fontSize =
                            if (newTextSize > 22.dp) 22.dp else if (newTextSize < 14.dp) 14.dp else newTextSize

                        iconSize =
                            if (newIconSize > 28.dp) 28.dp else if (newIconSize < 22.dp) 22.dp else newIconSize

                        columnHeightDp = updatedHeight
                        currentSelectedSize = selectedFriends.size
                    }
                }
                .fillMaxWidth(),
            mainAxisSpacing = 6.dp,
            crossAxisSpacing = 8.dp,
            mainAxisSize = SizeMode.Expand
        ) {
            selectedFriends.forEach { item ->
                CommentSelectedItemView(
                    commenter = item,
                    textSize = fontSize.value.sp,
                    delete = { id ->
                        selectedFriends.removeAll { it.userId == id }
                    })
            }
        }

        Column {
            val validList = selectableFriends.filterNot { selectedFriends.contains(it) }
            validList.forEach {
                CommentSelectableItemView(commenter = it, selected = { commenter ->
                    selectedFriends.add(commenter)
                })
            }
        }
    }
}

@Composable
private fun CommentSelectedItemView(
    commenter: CommentMentionInfo,
    textSize: TextUnit,
    delete: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color.Yellow)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = commenter.nickName, fontSize = textSize)
        IconButton(
            onClick = {
                delete(commenter.userId)
            },
            modifier = Modifier
                .size(20.dp)
                .padding(0.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_close_24),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CommentSelectableItemView(
    commenter: CommentMentionInfo,
    selected: (CommentMentionInfo) -> Unit
) {
    Text(
        text = commenter.nickName, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                selected(commenter)
            }
    )
}


