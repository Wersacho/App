package com.example.app.ui.add_game_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.main_screen.utils.Categories
import com.example.app.ui.theme.LightRed

@Composable
fun RoundedCornerDropDownMenu(
    defCategory: Int,
    onOptionSelected: (Int) -> Unit
) {

    val expanded = remember {
        mutableStateOf(false)
    }
    val categoriesList = stringArrayResource(id = R.array.category_array)
    val selectedOption = remember {
        mutableStateOf(categoriesList[Categories.ACTION])
    }

    //обновляем когда приходит реальное значение
    selectedOption.value = categoriesList[defCategory]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = LightRed,
                shape = RoundedCornerShape(25.dp)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .clickable {
                expanded.value = true
            }
            .padding(16.dp)
    ) {
        Text(text = selectedOption.value)
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            categoriesList.forEachIndexed { index, title ->
                DropdownMenuItem(
                    text = {
                        Text(text = title)
                    },
                    onClick = {
                        selectedOption.value = title
                        expanded.value = false
                        // передаем в addbookscreen
                        onOptionSelected(index)
                    }
                )
            }
        }
    }
}