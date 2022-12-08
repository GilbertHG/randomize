package sg.com.fairtech.randomize.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.current_item_list.view.*
import sg.com.fairtech.randomize.R
import sg.com.fairtech.randomize.model.Randomize

class RandomizeListAdapter(private val listener: (Randomize, Int) -> Unit, private val deleteListener: (Randomize, Int) -> Unit): RecyclerView.Adapter<RandomizeListAdapter.RandomizeListViewHolder>() {
    private var randomizeList = listOf<Randomize>()

    fun setRandomizeList(randomizeList: List<Randomize>) {
        this.randomizeList = randomizeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomizeListAdapter.RandomizeListViewHolder {
        return RandomizeListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.current_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: RandomizeListAdapter.RandomizeListViewHolder, position: Int) {
        val randomizerHolder = holder as RandomizeListViewHolder
        randomizerHolder.bindItem(randomizeList[position], listener, deleteListener)
    }

    override fun getItemCount(): Int = randomizeList.size

    class RandomizeListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(randomize: Randomize, listener: (Randomize, Int) -> Unit, deleteListener: (Randomize, Int) -> Unit) {
            itemView.tv_item_value.text = randomize.list_name
            itemView.tv_item_value.setOnClickListener {
                listener(randomize, layoutPosition)
            }

            itemView.btn_remove.setOnClickListener {
                deleteListener(randomize, layoutPosition)
                it.setClickable(false)
            }
        }
    }
}