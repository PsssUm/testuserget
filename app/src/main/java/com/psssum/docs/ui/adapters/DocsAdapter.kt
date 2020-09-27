package com.psssum.docs.ui.adapters

import android.R.id.button1
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psssum.docs.R
import com.psssum.docs.models.VKDoc
import com.psssum.docs.requests.VKRemoveDoc
import com.psssum.docs.ui.customViews.getMenu
import com.psssum.docs.utils.formatDate
import com.psssum.docs.utils.formatNumber
import com.psssum.docs.utils.interfaces.OnDocClicked
import com.psssum.docs.utils.interfaces.OnScrollListener
import com.psssum.market.utils.L
import com.psssum.market.utils.buildString
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException


class DocsAdapter(private val ctx: Context,var docs: ArrayList<VKDoc>,private val onScrollListener: OnScrollListener, private val onDocClicked: OnDocClicked) : RecyclerView.Adapter<DocsAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconIV: ImageView = view.findViewById<View>(R.id.iconIV) as ImageView
        val titleTV: TextView = view.findViewById<View>(R.id.titleTV) as TextView
        val descriptionTV: TextView = view.findViewById<View>(R.id.descriptionTV) as TextView
        val tagsTV: TextView = view.findViewById<View>(R.id.tagsTV) as TextView
        val tagsLL: LinearLayout = view.findViewById<View>(R.id.tagsLL) as LinearLayout
        val itemLL: LinearLayout = view.findViewById<View>(R.id.itemLL) as LinearLayout
        val menuRL: RelativeLayout = view.findViewById<View>(R.id.menuRL) as RelativeLayout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.doc_item, parent, false)

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val doc = getItem(position)
        Glide.with(ctx).load(doc.resourceId).fitCenter().into(holder.iconIV)
        //Picasso.get().load(doc.resourceId).fit().centerCrop().into(holder.iconIV)
        holder.titleTV.text = doc.name


        holder.descriptionTV.text = doc.description
        if (doc.tags != ""){
            holder.tagsTV.text = doc.tags
            holder.tagsLL.visibility = View.VISIBLE
        } else {
            holder.tagsLL.visibility = View.GONE
        }
        holder.menuRL.setOnClickListener{
            showPopup(holder, position, getItem(position))
        }

        holder.itemLL.setOnClickListener {
            onDocClicked.onClick(getItem(position))
        }
        if (position >= itemCount - 20) {
            onScrollListener.onScroll()
        }
    }

    private fun showPopup(holder : MyViewHolder, position: Int, doc : VKDoc) {
       // moreMenu.showAsAnchorRightTop(holder.menuRL)
        val menu = getMenu(ctx)

        menu?.showAsAnchorRightTop(holder.menuRL)
        menu?.setOnMenuItemClickListener { position, any ->
            when (position){
                0 -> renameDoc(position, doc)
                1 -> deleteDoc(position, doc)
            }
            menu.dismiss()
        }



    }
    private fun renameDoc(position : Int, doc: VKDoc){
        onDocClicked.onRenameClicked(doc, position)
    }
    private fun deleteDoc(position : Int, doc: VKDoc){
        VK.execute(VKRemoveDoc(doc.owner_id, doc.id), object :
            VKApiCallback<Int> {
            override fun success(result: Int) {
                if (result == 1) {
                    docs.remove(doc)
                    notifyDataSetChanged()
                }
            }

            override fun fail(error: VKApiExecutionException) {

                L.d("VKApiExecutionException = " + error.errorMsg)
            }
        })
    }

    override fun getItemCount(): Int {
        return docs.size
    }

    private fun getItem(position: Int): VKDoc {
        return docs[position]
    }
    fun updateData(docs: ArrayList<VKDoc>, offset : Int){
        this.docs = docs
        notifyItemRangeChanged(offset-1, this.docs.size - 1)
    }


}