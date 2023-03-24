package com.example.parking.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.findFragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.parking.R
import com.example.parking.api.data.TimeZone
import com.example.parking.api.data.UPDATE_001_Rq
import com.example.parking.main.DetailFragment
import com.example.parking.main.DetailFragmentArgs
import com.example.parking.main.DetailViewModel
import com.example.parking.utils.Loading

@Composable
fun DetailScreen() {
    BasicsCodeLabTheme {
        val viewModel: DetailViewModel = hiltViewModel()
        SetState(viewModel)
        val languageList = listOf(
            TimeZone.TW, TimeZone.CN, TimeZone.EN, TimeZone.JP, TimeZone.KO, TimeZone.ES, TimeZone.ID, TimeZone.TH, TimeZone.VI
        )
        BasicsSurfaceView(languageList, viewModel)
    }
}

@Composable
private fun BasicsSurfaceView(timeZone: List<TimeZone>, viewModel: DetailViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        val navController = LocalView.current.findNavController()
        val args by LocalView.current.findFragment<DetailFragment>().navArgs<DetailFragmentArgs>()
        BaseAppBar(
            arrowBackOnClick = { navController.navigate(R.id.entry_fragment) }
        ) {
            Items(timeZone, onTimeZoneChange(args, viewModel))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun Items(list: List<TimeZone> = List(50) { TimeZone.TW }, onClick: (TimeZone) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
    ) {
        items(items = list) { timeZone -> BaseCardView(timeZone, onClick) }
    }
}

@Composable
fun BaseCardView(timeZone: TimeZone, onClick: (TimeZone) -> Unit) {
    val scrollToPosition = remember { mutableStateOf(0F) }
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
                .clickable { onClick.invoke(timeZone) },
            elevation = 2.dp,
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary,
            // border = BorderStroke(width = 1.dp, color = Color.Green),
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = timeZone.name,
                style = typography.h4,
                textAlign = TextAlign.Start,
                color = Color.Black,
            )
        }
    }
}

fun getPhone(): String {
    return "未設定電話"
}

fun onTimeZoneChange(args: DetailFragmentArgs, viewModel: DetailViewModel): (TimeZone) -> Unit {
    return { timeZone ->
        args.login?.apply {
            val update = UPDATE_001_Rq(sessionToken, objectId, phone, timeZone.name)
            viewModel.updateUserData(update)
        }
    }
}

@Composable
private fun SetState(viewModel: DetailViewModel) {
    viewModel.apply {
        updateUser.observeAsState().value?.let {
            Loading.hide()
            OnSuccess { updateUser.postValue(null) }
        }
        onFailure.observeAsState().value?.let {
            Loading.hide()
            OnError { onFailure.postValue(null) }
        }
    }
}

@Composable
private fun OnSuccess(onClick: () -> Unit) {
    val context = LocalContext.current
    ShowNormalAlert(
        title = context.getString(R.string.common_text_hint),
        msg = context.getString(R.string.common_text_success),
        rightText = context.getString(R.string.common_text_i_know_it),
        rightClick = onClick
    )
}