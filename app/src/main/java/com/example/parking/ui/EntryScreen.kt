package com.example.parking.ui

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.parking.R
import com.example.parking.api.data.Parking
import com.example.parking.api.data.Parking.Companion.mock
import com.example.parking.main.EntryFragment
import com.example.parking.main.EntryFragmentArgs
import com.example.parking.main.EntryFragmentDirections
import com.example.parking.main.EntryViewModel

@Composable
fun EntryScreen() {
    val viewModel: EntryViewModel = hiltViewModel()
    val parkingList: List<Parking> by viewModel.parkingList.collectAsState(initial = listOf())
    val isLoading: Boolean by viewModel.isLoading

    SetState(viewModel)
    BasicsTheme(isLoading) { modifier ->
        BasicsSurfaceView(modifier, parkingList = parkingList)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun BasicsSurfaceView(modifier: Modifier = Modifier, parkingList: List<Parking> = List(50) { mock() }) {
    val navController = getNavController()
    val fragmentArgs = getArgs<EntryFragment, EntryFragmentArgs>()
    Surface(
        modifier = modifier.fillMaxSize(), color = Color.White
    ) {
        BaseAppBar(
            arrowBackOnClick = { navController.actionToMain() },
            settingsOnClick = {
                fragmentArgs?.value?.let { login ->
                    navController.actionToDetail(login)
                }
            },
        ) {
            ParkingList(parkingList)
        }
    }
}

private fun NavController.actionToMain() {
    navigate(EntryFragmentDirections.goMain())
}

private fun NavController.actionToDetail(args: EntryFragmentArgs) {
    navigate(EntryFragmentDirections.goDetail(args.login))
}

@Composable
private fun ParkingList(parkingList: List<Parking>) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
    ) {
        items(items = parkingList) { parking -> EntryCardView(parking) }
    }
}

@Composable
private fun EntryCardView(parking: Parking) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val scrollToPosition = remember { mutableStateOf(0F) }
    val onClick: () -> Unit = { expanded.value = !expanded.value }
    val animationContentSize: (Modifier) -> Modifier = {
        it.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
            )
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                scrollToPosition.value = coordinates.positionInParent().y
            },
        color = Color.White,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(5.dp)
                .clickable { onClick.invoke() },
            elevation = 2.dp,
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary,
            // border = BorderStroke(width = 1.dp, color = Color.Green),
        ) {
            ConstraintLayout(modifier = animationContentSize.invoke(Modifier.padding(10.dp))) {
                val (title, subTitle, image, content) = createRefs()
                val desc = parking.getDesc()
                val subTitleString = if (expanded.value) parking.getAvailableCarExt() else parking.getAvailableCar()
                val subTitleStyle = if (expanded.value) typography.h5 else LocalTextStyle.current
                val expandIcon = if (expanded.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
                Text(
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(image.start)
                        width = Dimension.fillToConstraints
                    },
                    text = desc.name ?: "name",
                    style = typography.h4,
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                )
                Text(
                    modifier = Modifier.constrainAs(subTitle) {
                        top.linkTo(title.bottom)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                        width = Dimension.fillToConstraints
                    },
                    text = subTitleString,
                    style = subTitleStyle,
                    color = Color.Black,
                )
                IconButton(
                    modifier = Modifier.constrainAs(image) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                    onClick = { onClick.invoke() },
                ) {
                    Icon(
                        imageVector = expandIcon,
                        contentDescription = "",
                        tint = Color.Black,
                    )
                }
                if (expanded.value) {
                    Column(modifier = Modifier.constrainAs(content) {
                        top.linkTo(subTitle.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }) {
                        Text(
                            text = desc.getTelPhone(),
                            style = typography.h5,
                            color = Color.Black,
                        )
                        Text(
                            text = desc.summary ?: "summary",
                            style = typography.h6,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SetState(viewModel: EntryViewModel) {
    viewModel.apply {
        onFailure.observeAsState().value?.let {
            val navController = LocalView.current.findNavController()
            val context = LocalContext.current
            OnError(msg = context.getString(R.string.common_text_unknown_fail)) { navController.popBackStack() }
        }
    }
}