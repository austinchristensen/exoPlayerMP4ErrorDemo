package person.mikepatterson.videodemo.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.asset_card_view.view.*
import kotlinx.android.synthetic.main.asset_list_header_view.view.*
import person.mikepatterson.models.view_state.AssetListItemViewState
import person.mikepatterson.videodemo.R

class AssetListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private val recyclerViewAdapter = RecyclerViewAdapter()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = recyclerViewAdapter
    }

    fun render(list: List<AssetListItemViewState>) {
        recyclerViewAdapter.updateList(list)
    }
}

// Note: In general I'm not a fan of stacking class after class into 1 file except for certain use-cases like sealed classes
// Note: I would move this into a generic RecyclerView pattern that only defines which classes/viewHolders it supports and how to render those
// TODO: turn this into a generic recycler view approach
private class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val cardList = mutableListOf<AssetListItemViewState>()

    fun updateList(list: List<AssetListItemViewState>) {
        // Note: this should really include DiffUtil instead of notifying all changed
        cardList.clear()
        cardList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val v = inflater.inflate(R.layout.asset_card_view, parent, false)
            AssetCardViewHolder(v)
        } else {
            val v = inflater.inflate(R.layout.asset_list_header_view, parent, false)
            AssetHeaderViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        when (holder) {
            is AssetCardViewHolder -> holder.render(cardList[position])
            is AssetHeaderViewHolder -> holder.render(cardList[position])
        }
    }

    override fun getItemCount() = cardList.size

    override fun getItemViewType(position: Int): Int {
        return when (cardList[position]) {
            is AssetListItemViewState.AssetCard -> 0
            is AssetListItemViewState.AssetHeader -> 1
        }
    }
}

private sealed class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private class AssetCardViewHolder(itemView: View) : RecyclerViewHolder(itemView) {

    fun render(data: AssetListItemViewState) {
        if (data is AssetListItemViewState.AssetCard) {
            itemView.assetCardName.text = data.name
            itemView.setOnClickListener { data.onClick() }
        }
    }
}

private class AssetHeaderViewHolder(itemView: View) : RecyclerViewHolder(itemView) {

    fun render(data: AssetListItemViewState) {
        if (data is AssetListItemViewState.AssetHeader) {
            itemView.assetListHeader.text = data.title
        }
    }
}
