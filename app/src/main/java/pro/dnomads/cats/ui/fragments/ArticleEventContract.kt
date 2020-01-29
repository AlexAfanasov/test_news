package pro.dnomads.cats.ui.fragments

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ArticleEventContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {
        fun initState()
        fun retry()
    }
}