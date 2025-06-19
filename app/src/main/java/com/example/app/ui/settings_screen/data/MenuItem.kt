package com.example.app.ui.settings_screen.data

import com.example.app.R
import com.example.app.data.DialogType


data class MenuItem(
    val title: String,
    val isCategory: Boolean = true,
    val isChecked: Boolean = false,
    val isSwitchVisible: Boolean = false,
    val dialogType: DialogType = DialogType.PERSONAL_DATA,
    val fieldsLabelsArrayId: Int = R.array.personal_data_array
)
