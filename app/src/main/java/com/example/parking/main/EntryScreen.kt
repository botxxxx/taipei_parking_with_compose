package com.example.parking.main

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import com.example.parking.R
import com.example.parking.api.data.Park
import com.example.parking.api.data.Parking
import com.example.parking.ui.BasicsCodeLabTheme
import com.example.parking.utils.DialogUtils
import com.example.parking.utils.Loading

@Composable
fun EntryScreen() {
    BasicsCodeLabTheme {
        val viewModel: EntryViewModel = hiltViewModel()
        SetState(viewModel)
    }
}

@Composable
fun SetMenu(parks: List<Parking>) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        color = Color.White
    ) {
        val scrollState = rememberScrollState(1)
        Menu(parks = parks, scrollState = scrollState)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun Menu(parks: List<Parking> = List(50) { Park.mockParking }, scrollState: ScrollState = rememberScrollState(1)) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(items = parks) { parks ->
            BaseCardView(parks, scrollState)
        }
    }
}

@Composable
fun BaseCardView(parking: Parking, scrollState: ScrollState) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val scrollToPosition = remember { mutableStateOf(0F) }

    val onClickCallback: () -> Unit = {
        expanded.value = !expanded.value
    }
    BlueButtonRow(parking = parking, onClickCallback, expanded = expanded, scrollToPosition = scrollToPosition)
}

@Composable
fun BlueButtonRow(parking: Parking, callback: () -> Unit, expanded: MutableState<Boolean>, scrollToPosition: MutableState<Float>) {
    Surface(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .onGloballyPositioned { coordinates ->
                scrollToPosition.value = coordinates.positionInParent().y
            }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Black),
        color = Color.White
    ) {
        val animationContentSize: (Modifier) -> Modifier = {
            it.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        ConstraintLayout(modifier = animationContentSize.invoke(Modifier.padding(10.dp))) {
            val (title, address, image, content) = createRefs()
            val desc = parking.desc ?: Park.mockDesc
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
                color = Color.Black
            )
            Text(
                modifier = Modifier
                    .constrainAs(address) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints
                    },
                text = desc.address ?: "address",
            )
            IconButton(
                modifier = Modifier.constrainAs(image) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                onClick = callback
            ) {
                val expandLess = Icons.Filled.ExpandLess
                val expandMore = Icons.Filled.ExpandMore
                Icon(
                    imageVector = if (expanded.value) expandLess else expandMore,
                    contentDescription = "",
                    modifier = Modifier.padding(10.dp)
                )
            }
            if (expanded.value) { //expanded.value
                Column(
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(address.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }) {
                    Text(
                        text = desc.summary ?: "summary",
                    )
                    Text(
                        text = desc.tel ?: "tel",
                    )
                }
            }
        }
    }
}

@Composable
private fun SetState(viewModel: EntryViewModel) {
    viewModel.apply {
        parkingDescLiveData.observeAsState().value?.let {
            OnSuccess(viewModel)
        }
        parkingAvailableLiveData.observeAsState().value?.let {
            OnSuccess(viewModel)
        }
        parkingDetail.observeAsState().value?.let {
            SetMenu(it)
            Loading.hide()
        }
        onFailureLiveData.observeAsState().value?.let {
            clearResponse()
        }
        updateLiveData.observeAsState().value?.let {
            OnSuccessDialog()
            Loading.hide()
        }
        getJson(LocalView.current)
    }
}

@Composable
private fun OnSuccessDialog() {
    val context = LocalContext.current
    DialogUtils.showNormalAlert(
        context = context,
        title = context.getString(R.string.common_text_hint),
        msg = context.getString(R.string.common_text_success),
        rightButtonText = context.getString(R.string.common_text_i_know_it),
    )
}

@Composable
private fun OnSuccess(viewModel: EntryViewModel) {
    val parkingList: MutableList<Parking> = mutableListOf()
    val descList = viewModel.parkingDescLiveData.value?.data?.park
    val availableList = viewModel.parkingAvailableLiveData.value?.data?.park
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
    }
}

@Composable
private fun OnError() {
    val context = LocalContext.current
    val fragment = LocalView.current.findNavController()
    DialogUtils.showNormalAlert(
        context = context,
        title = context.getString(R.string.common_text_error_msg),
        msg = context.getString(R.string.common_text_unknown_fail),
        rightButtonText = context.getString(R.string.common_text_i_know_it),
        rightButtonListener = {
            fragment.popBackStack()
        }
    )
}
