package pro.dnomads.cats.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.articles_fragment.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import pro.dnomads.cats.NewsListAdapter
import pro.dnomads.cats.R
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.presentation.ArticleListPresenter
import timber.log.Timber
import javax.inject.Inject


@ActivityScope
class ArticlesListFragment @Inject constructor() : DaggerFragment(), ArticleEventContract.View {

    @Inject
    @InjectPresenter
    lateinit var presenter: ArticleListPresenter

    @ProvidePresenterTag(presenterClass = ArticleListPresenter::class)
    fun provideDialogPresenterTag(): String = "Hello"

    @Inject
    lateinit var fragment: WebViewFragment

    private lateinit var newsListAdapter: NewsListAdapter
    private val KEY_RECYCLER_STATE = "recycler_state"
    private var mBundleRecyclerViewState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
        newsListAdapter = NewsListAdapter(
            this::onArticleClicked,
            this::retry
        )
        news_list.adapter = newsListAdapter
        presenter.getArticles().observe(viewLifecycleOwner, Observer {
            Timber.e("Submit list size ${it.size}")
            newsListAdapter.submitList(it)
        })
        news_list.isNestedScrollingEnabled = true
        with(news_list) {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    manager.orientation
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mBundleRecyclerViewState?.let {
            val listState = it.getParcelable<LinearLayoutManager.SavedState>(KEY_RECYCLER_STATE)
            news_list.layoutManager?.onRestoreInstanceState(listState)
        }
    }

    override fun onPause() {
        super.onPause()
        mBundleRecyclerViewState = Bundle()
        val listState = news_list.layoutManager?.onSaveInstanceState()
        mBundleRecyclerViewState?.putParcelable(KEY_RECYCLER_STATE, listState)
    }


    private fun onArticleClicked(article: ArticlesItem) {
        val action = ArticlesListFragmentDirections.actionListFragmentToWebViewFragment(article.url)
        view?.findNavController()?.navigate(action)
    }

    override fun retry() {
        presenter.requestRetry()
    }

    override fun initState() {
        presenter.getState().observe(viewLifecycleOwner, Observer { state ->
            newsListAdapter.setState(state ?: State.SUCCESS)
        })
    }
}