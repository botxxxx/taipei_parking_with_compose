package com.example.parking.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.api.data.TimeZone
import com.example.parking.api.data.UPDATE_001_Rq
import com.example.parking.main.DetailFragment
import com.example.parking.main.DetailFragmentArgs
import com.example.parking.main.DetailViewModel
import com.example.parking.utils.Loading

@Composable
fun DetailScreen() {
    BasicsTheme {
        val languageList = listOf(TimeZone.TW, TimeZone.CN, TimeZone.EN, TimeZone.JP, TimeZone.KO, TimeZone.ES, TimeZone.ID, TimeZone.TH, TimeZone.VI)
        val viewModel: DetailViewModel = hiltViewModel()

        SetState(viewModel)
        BasicsSurfaceView(languageList, viewModel)
    }
}

@Composable
private fun BasicsSurfaceView(timeZoneList: List<TimeZone>, viewModel: DetailViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        val navController = getNavController()
        val fragmentArgs = getArgs<DetailFragment, DetailFragmentArgs>()
        BaseAppBar(
            appBar = "基本資料",
            arrowBackOnClick = { navController.navigate(R.id.entry_fragment) }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(9.dp),
            ) {
                Text(text = getPhone(fragmentArgs?.value?.login), color = Color.Black, style = typography.h6)
                Text(text = "設定時區", color = Color.Black, style = typography.subtitle1)
                Items(modifier = Modifier.padding(top = 5.dp), list = timeZoneList, onClick = onTimeZoneChange(fragmentArgs?.value?.login, viewModel))
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Items(modifier: Modifier = Modifier, list: List<TimeZone> = List(50) { TimeZone.TW }, onClick: (TimeZone) -> Unit = {}) {
    LazyColumn(modifier = modifier.fillMaxHeight()) {
        items(items = list) { timeZone ->
            BaseCardView(timeZone, onClick)
        }
    }
}

@Composable
private fun BaseCardView(timeZone: TimeZone, onClick: (TimeZone) -> Unit) {
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

private fun getPhone(login: LOGIN_001_Rs?): String {
    return "電話: ${login?.phone ?: "未設定電話"}"
}

private fun onTimeZoneChange(login: LOGIN_001_Rs?, viewModel: DetailViewModel): (TimeZone) -> Unit {
    return { timeZone ->
        login?.apply {
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
        val context = LocalContext.current
        onFailure.observeAsState().value?.let {
            Loading.hide()
            OnError(msg = context.getString(R.string.common_text_error_content)) { onFailure.postValue(null) }
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