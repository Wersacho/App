package com.example.app.custom

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.R
import com.example.app.ui.main_screen.MainScreenViewModel

@Composable
@Preview(showBackground = true)
fun FilterDialog(
    showDialog: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    title: String = "Фильтр",
    confirmButtonText: String = "OK",

    viewModel: MainScreenViewModel = hiltViewModel()
) {
    //val priceRange = 0..50000

    /*val minPriceValue = remember {
        mutableIntStateOf(priceRange.first)
    }*/

    /*val maxPriceValue = remember {
        mutableIntStateOf(priceRange.last)
    }*/

    if (showDialog){
        AlertDialog(

            onDismissRequest = {
                onDismiss()
            },


            confirmButton = {

                Button( onClick = {
                    onConfirm()
                    viewModel.setFilter()
                }

                ) {
                    Text(
                        text = confirmButtonText
                    )
                }

                Button( onClick = {
                    onDismiss()
                }

                ) {
                    Text(
                        text = "Отменить"
                    )
                }
            },

            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },
            text = {

                val orderBySelection = stringArrayResource(R.array.order_by)[0]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RadioButtonsSet(
                        isFilterByTitle = viewModel.isFilterByTitle.value
                    ) { option ->
                        viewModel.isFilterByTitle.value = option == orderBySelection
                    }



                    //отображаем диапозон
                    if (!viewModel.isFilterByTitle.value) {

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Диапозон",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            Modifier.height(10.dp)
                        )

                        PricePicker2(
                            priceValue = viewModel.minPriceValue.floatValue,
                            title = "Min:",
                            onValueChange = { value ->
                                viewModel.minPriceValue.floatValue = value
                            }
                        )

                        PricePicker2(
                            priceValue = viewModel.maxPriceValue.floatValue,
                            title = "Max:",
                            onValueChange = { value ->
                                viewModel.maxPriceValue.floatValue = value
                            }
                        )

                        /*Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Text(text = "От")
                            PricePicker(
                                minPriceValue.intValue,
                                priceRange
                            ) { value ->
                                minPriceValue.intValue = value
                            }

                            Text(text = "До")
                            PricePicker(
                                maxPriceValue.intValue,
                                priceRange
                            ) { value ->
                                maxPriceValue.intValue = value
                            }
                        }*/

                    }
                }
            }
        )
    }
}