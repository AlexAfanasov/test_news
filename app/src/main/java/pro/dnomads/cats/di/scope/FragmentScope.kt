package pro.dnomads.cats.di.scope

import javax.inject.Scope

@Scope
@Retention
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class FragmentScope