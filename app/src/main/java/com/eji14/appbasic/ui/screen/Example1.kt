package com.eji14.appbasic.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eji14.appbasic.AppConfig
import com.eji14.cattycat.navigation.PageNavigation
import com.eji14.cattycat.ui.ScreenHolder
import com.eji14.cattycat.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Example1(
    modifier: Modifier = Modifier
) {

}

private sealed class Pages(
    identifier: String,
    routeKey: String = identifier,
    holder: PageNavigation.Holder = PageNavigation.Holder.DEFAULT
) : PageNavigation.Page(identifier, routeKey, holder) {
    data object Dashboard : Pages("Dashboard")
    data object PlnList : Pages("PlnList")
    data object GameList : Pages("GameList")
    data object PhoneList : Pages("PhoneList")
}

private data class DashboardData(
    val plnCount: Int,
    val plnUsed: Int,
    val gameCount: Int,
    val gameUsed: Int,
    val phoneCount: Int,
    val phoneUsed: Int
)

private class DashboardHolder : ScreenHolder(
    refreshable = true,
    autoRefresh = true
) {
    private val _data = MutableStateFlow<Resource<DashboardData>>(Resource.Idle)

    override suspend fun onRefresh() {
        delay(300L)
        val data = DashboardData(
            plnCount = 10,
            plnUsed = 5,
            gameCount = 20,
            gameUsed = 10,
            phoneCount = 30,
            phoneUsed = 20
        )
        _data.value = Resource.Success(data)
    }

    fun showNetworkError1(config: AppConfig) {
        popupDialog(
            config = config.networkErrorDialogConfig,
            onPrimaryClicked = {},
            onSecondaryClicked = {}
        )
    }
}