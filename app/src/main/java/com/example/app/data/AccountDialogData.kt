package com.example.app.data

data class AccountDialogData(
    val title: String = "",
    val fieldsLabels: List<String> = emptyList(),
    val showDialog: Boolean = false,
    val dialogType: DialogType = DialogType.PERSONAL_DATA
)

enum class DialogType {
    PERSONAL_DATA,
    ADDRESS,
    PASSWORD,
    DELETE_ACCOUNT
}