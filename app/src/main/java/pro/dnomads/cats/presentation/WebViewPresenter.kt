package pro.dnomads.cats.presentation

import moxy.MvpPresenter
import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.ui.fragments.WebViewContract
import javax.inject.Inject


@ActivityScope
class WebViewPresenter @Inject constructor() :
    MvpPresenter<WebViewContract.View>() {


    fun start(view: WebViewContract.View, url: String) {
        view.initWebView(url)
        view.initWebViewClient()
    }
}