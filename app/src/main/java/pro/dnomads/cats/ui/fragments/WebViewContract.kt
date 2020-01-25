package pro.dnomads.cats.ui.fragments

import pro.dnomads.cats.ui.base.BasePresenter
import pro.dnomads.cats.ui.base.BaseView

interface WebViewContract {

    interface Presenter : BasePresenter<View> {
        fun start(view: View, url: String)
    }


    interface View : BaseView<Presenter> {
        fun initWebView(startURL: String)

        fun initWebViewClient()
    }
}