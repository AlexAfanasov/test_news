package pro.dnomads.cats.ui.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_footer.view.*
import pro.dnomads.cats.R
import pro.dnomads.cats.State

class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility =
            if (status == State.RUNNING) View.VISIBLE else View.INVISIBLE
        itemView.bottom_text.visibility =
            if (status == State.FAILED) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)
            view.bottom_text.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }
}