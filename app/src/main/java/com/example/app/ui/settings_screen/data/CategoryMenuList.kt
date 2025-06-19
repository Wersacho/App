package com.example.app.ui.settings_screen.data

import com.example.app.R
import com.example.app.data.DialogType

object CategoryMenuList {
    val menuItemsList = listOf(
        MenuItem(
            "Настройки аккаунта"
        ),
        MenuItem(
            "Личные данные",
            isCategory = false
        ),
        MenuItem(
            "Адрес",
            isCategory = false,
            dialogType = DialogType.ADDRESS,
            fieldsLabelsArrayId = R.array.adress_array
        ),
        MenuItem(
            "Пароль",
            isCategory = false,
            dialogType = DialogType.PASSWORD,
            fieldsLabelsArrayId = R.array.password_array

        ),
        MenuItem(
            "Удалить аккаунт",
            isCategory = false,
            dialogType = DialogType.DELETE_ACCOUNT,
            fieldsLabelsArrayId = R.array.delete_account_array
        )
    )
}