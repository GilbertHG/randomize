package sg.com.fairtech.randomize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.current_item_list.view.*
import sg.com.fairtech.randomize.R

class RandomizeAdapter(private val listRandomizeItem: ArrayList<String>, private val listener: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RandomizeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.current_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listRandomizeItem[position]
        val randomizeHolder = holder as RandomizeViewHolder
        randomizeHolder.bindItem(item, position ,listener)
    }

    override fun getItemCount(): Int {
        return listRandomizeItem.size
    }

    class RandomizeViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: String, position: Int, listener: (Int) -> Unit) {
            itemView.tv_item_value.text = item

            itemView.btn_remove.setOnClickListener{
                listener(position)
                it.setClickable(false)
            }
        }
    }

}