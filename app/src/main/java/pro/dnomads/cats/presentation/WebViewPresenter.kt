package pro.dnomads.cats.presentation

import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.ui.fragments.WebViewContract
import javax.inject.Inject


@ActivityScope
class WebViewPresenter @Inject constructor() :
    WebViewContract.Presenter {


    override fun start(view: WebViewContract.View, url: String) {
        view.initWebView(url)
        view.initWebViewClient()
    }

    private lateinit var view: WebViewContract.View

    override fun subscribe(view: WebViewContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
    }
}