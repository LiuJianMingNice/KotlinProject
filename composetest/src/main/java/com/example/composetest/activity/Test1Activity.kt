package com.example.composetest.activity

import android.graphics.Color
import android.graphics.Color.GREEN
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import com.example.composetest.R
import com.example.composetest.activity.ui.theme.MyApplicationTheme

class Test1Activity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
//                CreateLazyColumnItems()
//                CreateLazyRowItems()
                CreateLazyVerticalGridItems()
            }
        }
    }
}

@Composable
fun CreateLazyColumnItems() {
    val itemsList = (0..5).toList()
    val itemsIndexedList = listOf("A","B","C")
    LazyColumn() {
        items(itemsList) {
            Text("Item is $it")
        }

        item {
            Text("Single item")
        }

        itemsIndexed(itemsIndexedList) { index, item ->
            Text("Item at index $index is $item")
        }
    }
}

@Composable
fun CreateLazyRowItems() {
    val itemsList = (0..5).toList()
    val itemsIndexList = listOf("A", "B", "C")
    LazyRow() {
        items(itemsList) {
            Text("Item is $it")
        }

        item{
            Text(text = "Single item")
        }

        itemsIndexed(itemsIndexList) { index, item ->
            Text("Item at index $index is $item")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun CreateLazyVerticalGridItems() {
    val itemsList = (0..5).toList()
    val itemsIndexList = listOf("A", "B", "C")
    LazyVerticalGrid(cells = GridCells.Fixed(2), modifier = Modifier.background(Green)) {
        items(itemsList) {
            Text(text = "Item is $it")
        }
        item {
            Text("Single item")
        }
        itemsIndexed(itemsIndexList) { index, item ->
            Text(text = "Item ad index $index is $item")
        }
    }
}

