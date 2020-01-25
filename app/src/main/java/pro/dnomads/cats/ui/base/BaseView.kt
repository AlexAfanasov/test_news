package pro.dnomads.cats.ui.base

/**
 * Base View interface class of MVP
 */
interface BaseView<T> {
    var presenter: T
}