package pro.dnomads.cats.di.activity


import dagger.Module
import dagger.android.ContributesAndroidInjector
import pro.dnomads.cats.di.fragment.FragmentsModule
import pro.dnomads.cats.di.scope.ActivityScope
import pro.dnomads.cats.ui.MainActivity

@Module
interface ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            FragmentsModule::class
        ]
    )
    fun contributeMainActivity(): MainActivity

}