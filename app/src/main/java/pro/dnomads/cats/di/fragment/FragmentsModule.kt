package pro.dnomads.cats.di.fragment

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import moxy.MvpPresenter
import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.di.scope.FragmentScope
import pro.dnomads.cats.presentation.ArticleListPresenter
import pro.dnomads.cats.presentation.WebViewPresenter
import pro.dnomads.cats.ui.fragments.ArticleEventContract
import pro.dnomads.cats.ui.fragments.ArticlesListFragment
import pro.dnomads.cats.ui.fragments.WebViewContract
import pro.dnomads.cats.ui.fragments.WebViewFragment

@Module
interface FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun articlesFragment(): ArticlesListFragment

    @ActivityScope
    @Binds
    fun articlesPresenter(presenter: ArticleListPresenter): MvpPresenter<ArticleEventContract.View>


    @FragmentScope
    @ContributesAndroidInjector
    fun webViewFragment(): WebViewFragment

    @ActivityScope
    @Binds
    fun webViewPresenter(presenter: WebViewPresenter): MvpPresenter<WebViewContract.View>

}