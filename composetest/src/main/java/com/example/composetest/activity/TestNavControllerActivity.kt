package com.example.composetest.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composetest.R
import com.example.composetest.activity.ui.theme.MyApplicationTheme

class TestNavControllerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
//                ComposeNavigation()
//                CreateText()
//                CreateImageView()
//                CreateView()
//                ColorText("test")
//                CreateButton()

//                Column() {
//                    RadioDefault()
//                    RadioText()
//                    RadioButtonGroup()
//                    Row() {
//                        CheckboxDefault()
//                        SwitchDefault()
//                    }
//                }

                RecyclerView()
            }
        }
    }
}

@Composable
fun CreateText() {
    Text("你好，")
}

@Composable
fun CreateImageView() {
    Image(
        painter = painterResource(id = R.drawable.ic_face_on),
//        painter = painterResource(id = R.mipmap.ic_appstart),
        contentDescription = null
    )
}

@Composable
fun CreateInputText() {
    val input = remember {
        mutableStateOf("")
    }
    TextField(value = input.value, onValueChange = { input.value = it })
}

private val items =
    arrayOf(Color.Red, Color.Gray, Color.Magenta, Color.Blue, Color.Green, Color.Cyan)

@Composable
fun ColorText(name: String) {
    val color = remember {
        items.random()
    }
    val clicked = remember {
        mutableStateOf(0)
    }
    Log.d("ljm", "UI Compose")
    Column() {
        Text(
            text = "I'm colored: ${clicked.value}",
            modifier = Modifier
                .padding(16.dp)
                .background(color)
                .clickable {
                    Log.d("ljm", "clicked")
                    clicked.value = clicked.value + 1
                })
    }
}

@Composable
fun CreateButton() {
    Column() {
        Row() {
            //普通按钮
            Button(onClick = {}) {
                Text(text = "Button")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "OutlineButton")
            }
            Spacer(modifier = Modifier.width(8.dp))
            //纯文字按钮
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "TextButton")
            }
        }
        Row() {
            //带Icon的按钮
            //圆形水波纹和Checkbox，Switch水波纹效果一致
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.width(120.dp)) {
                Row() {
                    Icon(painter = painterResource(id = R.drawable.ic_figure), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "IconButton")
                }
            }

            //图标切换button，类似于switch，但是更接近于checkBox
            //圆形水波纹和Checkbox，Switch水波纹效果一致
            IconToggleButton(checked = false, onCheckedChange = {}, modifier = Modifier.width(120.dp)) {
                Text(text = "IconToggleButton")
            }
            //悬浮按钮
            ExtendedFloatingActionButton(text = { Text(text = "FAB")}, onClick = { /*TODO*/ })
        }
    }

}

@Composable
fun RadioDefault() {
    val isSelected = remember {
        mutableStateOf(false)
    }
    RadioButton(selected = isSelected.value, onClick = {
        isSelected.value = !isSelected.value
        Log.d("ljm", "默认Checkbox，痛RadioButton不带text文本控件")
    })
}

@Composable
fun RadioText() {
    val isSelected = remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.clickable {
        isSelected.value = !isSelected.value
    }) {
        RadioButton(
            selected = isSelected.value, onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
            unselectedColor = MaterialTheme.colors.error,
            disabledColor = MaterialTheme.colors.secondary
            )
        )
        Text(text = "选项②")
    }
}

@Composable
fun RadioButtonGroup() {
    val options = listOf("选项③","选项④","选项⑤","选项⑥")
    val selectedButton = remember {
        mutableStateOf(options.first())
    }
    
    Row() {
        options.forEach{
            val isSelected = it == selectedButton.value
            Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                selectedButton.value = it
            }) {
                RadioButton(selected = isSelected, onClick = null)
                Text(text = it, textAlign = TextAlign.Justify)
            }
        }
    }
}

@Composable
fun CheckboxDefault() {
    val isSelected = remember {
        mutableStateOf(false)
    }
    Checkbox(checked = isSelected.value, onCheckedChange = {
        isSelected.value = it
    })
}

@Composable
fun SwitchDefault() {
    val isSelected = remember {
        mutableStateOf(false)
    }
    Switch(checked = isSelected.value, onCheckedChange = {
        isSelected.value = it
        Log.d("ljm", "默认Checkbox,同RadioButton不带text文本控件")
    })
}

//@Composable
//fun RadioText() {
//    val isSelected = remember { mutableStateOf(false) }
//    // 如果需要 text 需要和 Text 组合使用,click处理在row上
//    Row(modifier = Modifier.clickable {
//        isSelected.value = !isSelected.value
//    }) {
//        RadioButton(
//            selected = isSelected.value, onClick = null,
//            colors = RadioButtonDefaults.colors(
//                selectedColor = MaterialTheme.colors.primary,
//                unselectedColor = MaterialTheme.colors.error,
//                disabledColor = MaterialTheme.colors.secondary
//            )
//        )
//        Text("选项②")
//    }
//}
//
//@Composable
//fun RadioButtonGroup() {
//    val options = listOf("选项③", "选项④", "选项⑤", "选项⑥")
//    val selectedButton = remember { mutableStateOf(options.first()) }
//    //RadioButton 不带 text 文本控件,
//    Row() {
//        options.forEach {
//            val isSelected = it == selectedButton.value
//            Row(verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.clickable {
//                    selectedButton.value = it
//                }) {
//                RadioButton(
//                    selected = isSelected, onClick = null,
//                )
//                Text(it, textAlign = TextAlign.Justify)
//            }
//        }
//    }
//}

@Composable
fun CreateView() {
//    Text("你好，")
//    Image(
//        painter = painterResource(id = R.drawable.ic_face_on),
////        painter = painterResource(id = R.mipmap.ic_appstart),
//        contentDescription = null
//    )

    Column(modifier = Modifier.background(Color.Green)) {
        Column() {
            Text(text = "文本1")
            Image(painter = painterResource(id = R.drawable.ic_face_on), contentDescription = null)
        }

        Row() {
            Text(text = "文本2")
            Image(painter = painterResource(id = R.drawable.ic_face_on), contentDescription = null)
        }
    }


//    Box(modifier = Modifier
//        .background(Color.Cyan)
//        .size(180.dp)) {
//        Box(modifier = Modifier
//            .align(Alignment.TopCenter)
//            .size(60.dp, 60.dp)
//            .background(Color.Red)) {
//        }
//        Box(modifier = Modifier.align(Alignment.BottomEnd)
//            .size(60.dp, 60.dp)
//            .background(Color.Blue))
//    }
}

@Preview
@Composable
fun RecyclerView(names: List<String> = List(1000) {"$it"}) {
    LazyColumn() {
        items(items = names) { item ->
            MessageCard(item)
        }
    }
}

@Composable
fun MessageCard(msg: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val surfaceColor: Color by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            isExpanded = !isExpanded
        }
        .padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_face_on),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        // We toggle the isExpanded variable when we click on this Column
        Column() {
            Text("当前索引:")
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = "索引为--------> $msg ,这是一个可展开和关闭的 Text 控件:" +
                            "微凉的晨露 沾湿黑礼服\n" +
                            "石板路有雾 父在低诉\n" +
                            "无奈的觉悟 只能更残酷\n" +
                            "一切都为了通往圣堂的路",
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun ComposeNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "first_screen") {
        composable("first_screen") {
            FirstScreen(navController = navController)
        }
        composable("second_screen") {
            SecondScreen(navController = navController)
        }
        composable("third_screen") {
            ThirdScreen(navController = navController)
        }
    }
}

@Composable
fun FirstScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "First Screen\n" + "Click me to go to Second Screen",
            color = Color.Green,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(24.dp)
                .clickable(onClick = {
//                navController.navigate("second_screen")
                })
        )
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Second Screen\n" + "Click me to go to Second Screen",
            color = Color.Yellow,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(24.dp)
                .clickable(onClick = {
//                navController.navigate("third_screen")
                })
        )
    }
}

@Composable
fun ThirdScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Third Screen\n" + "Click me to go to Second Screen",
            color = Color.Red,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(24.dp)
                .clickable(onClick = {
//                navController.navigate("first_screen")
                })
        )
    }
}