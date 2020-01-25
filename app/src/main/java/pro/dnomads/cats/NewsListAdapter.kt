package pro.dnomads.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_footer.view.*
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.databinding.ItemListFooterBinding
import pro.dnomads.cats.databinding.NewsItemLayoutBinding

class NewsListAdapter(
    private val onArticleClicked: (ArticlesItem) -> Unit,
    private val retry: () -> Unit
) :
    PagedListAdapter<ArticlesItem, RecyclerView.ViewHolder>(ArticleDiff) {


    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = State.RUNNING
    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount())
            DATA_VIEW_TYPE
        else FOOTER_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) ArticleViewHolder(
            NewsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) else
            ListFooterViewHolder(
                ItemListFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            getItem(position)?.let {
                (holder as ArticleViewHolder).bind(it)
            }
        } else (holder as ListFooterViewHolder).bind(state)

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.RUNNING || state == State.FAILED)
    }

    inner class ArticleViewHolder(private val binder: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind(article: ArticlesItem) {
            binder.dataItem = article
            binder.root.setOnClickListener { onArticleClicked(article) }
            binder.executePendingBindings()
        }
    }

    inner class ListFooterViewHolder(binder: ItemListFooterBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind(status: State?) {
            itemView.progress_bar.visibility =
                if (status == State.RUNNING) View.VISIBLE else View.INVISIBLE
            itemView.bottom_text.visibility =
                if (status == State.FAILED) View.VISIBLE else View.INVISIBLE
            itemView.bottom_text.setOnClickListener { retry() }
        }
    }

    private object ArticleDiff : DiffUtil.ItemCallback<ArticlesItem>() {

        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem) =
            (oldItem.publishedAt == newItem.publishedAt) && (oldItem.description == newItem.description)

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return true
        }

    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}
