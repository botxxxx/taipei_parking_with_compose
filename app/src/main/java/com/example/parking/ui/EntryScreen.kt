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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.fragment.app.findFragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.parking.R
import com.example.parking.api.data.Park
import com.example.parking.api.data.Parking
import com.example.parking.main.EntryFragment
import com.example.parking.main.EntryFragmentArgs
import com.example.parking.main.EntryFragmentDirections
import com.example.parking.main.EntryViewModel
import com.example.parking.utils.Loading

@Composable
fun EntryScreen() {
    BasicsCodeLabTheme {
        val view = LocalView.current
        val viewModel: EntryViewModel = hiltViewModel()
        SetState(viewModel)
        LaunchedEffect(Unit) {
            viewModel.getJson(view)
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun BasicsSurfaceView(parks: List<Parking> = List(50) { Park.mockParking }) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        val localView = LocalView.current
        val navController = localView.findNavController()
        val parentFragment = remember(localView) {
            try {
                localView.findFragment<EntryFragment>()
            } catch (e: IllegalStateException) {
                // findFragment throws if no parent fragment is found
                null
            }
        }
        val parentFragmentArgs = parentFragment?.navArgs<EntryFragmentArgs>()
        BaseAppBar(
            arrowBackOnClick = { goBack(navController) },
            settingsOnClick = {
                parentFragmentArgs?.let {
                    goDetail(navController, it.value)
                }
            },
        ) {
            ParkingList(parks = parks)
        }
    }
}

private fun goBack(navController: NavController) {
    navController.navigate(R.id.main_fragment)
}

private fun goDetail(navController: NavController, args: EntryFragmentArgs) {
    val direction = EntryFragmentDirections.actionToDetail(args.login)
    navController.navigate(direction)
}

@Composable
private fun ParkingList(parks: List<Parking>) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
    ) {
        items(items = parks) { parks -> EntryCardView(parks) }
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
                val desc = parking.desc ?: Park.mockDesc
                val subTitleText = if (expanded.value) parking.getAvailableCarExt() else parking.getAvailableCar()
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
                    text = subTitleText,
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
        parkingDesc.observeAsState().value?.let {
            OnSuccess(viewModel)
        }
        parkingAvailable.observeAsState().value?.let {
            OnSuccess(viewModel)
        }
        parkingDetail.observeAsState().value?.let {
            Loading.hide()
            BasicsSurfaceView(it)
        }
        onFailure.observeAsState().value?.let {
            Loading.hide()
            val navController = LocalView.current.findNavController()
            val context = LocalContext.current
            OnError(msg = context.getString(R.string.common_text_unknown_fail)) { navController.popBackStack() }
        }
    }
}

@Composable
private fun OnSuccess(viewModel: EntryViewModel) {
    val parkingList: MutableList<Parking> = mutableListOf()
    val descList = viewModel.parkingDesc.value?.data?.park
    val availableList = viewModel.parkingAvailable.value?.data?.park
    if (descList != null && availableList != null) {
        for (item in descList) {
            for (check in availableList) {
                if (item.id == check.id) {
                    parkingList.add(Parking(item.id, item.getDesc(), check.getAvl()))
                    break
                }
            }
        }
        viewModel.parkingDetail.postValue(parkingList)
        Loading.hide()
    }
}