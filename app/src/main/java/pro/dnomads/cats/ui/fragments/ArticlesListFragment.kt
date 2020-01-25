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
import pro.dnomads.cats.NewsListAdapter
import pro.dnomads.cats.R
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.di.scope.ActivityScope
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class ArticlesListFragment @Inject constructor() : DaggerFragment(), ArticleEventContract.View {

    @Inject
    lateinit var fragment: WebViewFragment

    @Inject
    override lateinit var presenter: ArticleEventContract.Presenter

    private lateinit var newsListAdapter: NewsListAdapter


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
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }


    private fun onArticleClicked(article: ArticlesItem) {
        val action = ArticlesListFragmentDirections.actionListFragmentToWebViewFragment(article.url)
        view?.findNavController()?.navigate(action)
    }

    private fun retry() {
        presenter.requestRetry()
    }

    private fun initState() {
        presenter.getState().observe(viewLifecycleOwner, Observer { state ->
            newsListAdapter.setState(state ?: State.SUCCESS)
        })
    }
}