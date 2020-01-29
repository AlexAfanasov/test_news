package pro.dnomads.cats.ui.fragments

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.webview_fragment.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import pro.dnomads.cats.R
import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.presentation.WebViewPresenter
import javax.inject.Inject


@ActivityScope
class WebViewFragment @Inject constructor() : DaggerFragment(), WebViewContract.View {

    private var url: String? = ""

    @Inject
    @InjectPresenter
    lateinit var presenter: WebViewPresenter

    @ProvidePresenterTag(presenterClass = WebViewPresenter::class)
    fun provideDialogPresenterTag(): String = "Hello"

    @ProvidePresenter()
    fun provideDialogPresenter() = WebViewPresenter()
    lateinit var mProgressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        url = arguments?.let { WebViewFragmentArgs.fromBundle(it).url }
        mProgressDialog = ProgressDialog(this.context)
        return inflater.inflate(R.layout.webview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url?.let { presenter.start(this, it) }
    }

    override fun initWebView(startURL: String) {
        val settings = main_webview.settings

        settings?.run {
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = true

            textZoom = 125

            blockNetworkImage = false
            loadsImagesAutomatically = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = true  // api 26
            }

            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false

            domStorageEnabled = true
            setSupportMultipleWindows(true)
            loadWithOverviewMode = true
            allowContentAccess = true
            setGeolocationEnabled(true)

            allowUniversalAccessFromFileURLs = true

            allowFileAccess = true
        }


        main_webview.run {
            fitsSystemWindows = true

            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            loadUrl(startURL)
        }
    }

    override fun initWebViewClient() {

        main_webview.webViewClient = object : WebViewClient() {


//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                view?.loadUrl(request?.url.toString())
//                return super.shouldOverrideUrlLoading(view, request)
//            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mProgressDialog.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mProgressDialog.hide()
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String) = true
//
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ) = true
        }
    }

}