package com.psssum.docs.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.psssum.docs.R
import com.psssum.docs.controllers.RenameDocController
import com.psssum.docs.models.VKDoc
import com.psssum.docs.models.VKUser
import com.psssum.docs.requests.VKGetDocs
import com.psssum.docs.requests.VKUsersGet
import com.psssum.docs.ui.activity.MainActivity
import com.psssum.docs.ui.adapters.DocsAdapter
import com.psssum.docs.ui.customViews.MoreMenuFactory
import com.psssum.docs.utils.KeyboardHelper
import com.psssum.docs.utils.TimerSearchBreaker
import com.psssum.docs.utils.interfaces.*
import com.psssum.market.utils.L
import com.skydoves.powermenu.kotlin.powerMenu
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.docs_fragment.view.*

class DocsFragment : Fragment(), OnScrollListener, OnDocClicked {
    private var offset = 0
    private var docs = ArrayList<VKDoc>()
    private var adapter : DocsAdapter? = null
    private lateinit var docsRV : RecyclerView
    private var tsb : TimerSearchBreaker? = null
    private lateinit var swipeRefresh : SwipeRefreshLayout
    companion object {

        val TAG: String = DocsFragment::class.java.simpleName
        fun newInstance() = DocsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.docs_fragment, container, false)
        docsRV = view.docsRV
        swipeRefresh = view.swipeRefresh
        setUpView()
        return view
    }

    private fun setUpView(){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL;
        docsRV.layoutManager = linearLayoutManager

        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener {
            offset = 0
            docs = ArrayList<VKDoc>()
            adapter = null
            loadDocuments()
        }
        swipeRefresh.isRefreshing = true
        loadDocuments()
    }
    private fun loadDocuments(){
        VK.execute(VKGetDocs(offset, context!!), object :
            VKApiCallback<ArrayList<VKDoc>> {
            override fun success(result: ArrayList<VKDoc>) {
                docs.addAll(result)
                if (adapter == null) {
                    if (context != null) {
                        setAdapter()
                    }
                } else {
                    adapter!!.updateData(docs, offset)
                }
                offset += 50
                swipeRefresh.isRefreshing = false
            }

            override fun fail(error: VKApiExecutionException) {
                swipeRefresh.isRefreshing = false
                L.d("VKApiExecutionException = " + error.errorMsg)
            }
        })
        loadUser()
    }
    private fun loadUser(){
        L.d("loadUser")
        VK.execute(VKUsersGet(context!!), object :
            VKApiCallback<VKUser> {
            override fun success(result: VKUser) {
                L.d("VKUser name = " + result.first_name)
            }

            override fun fail(error: VKApiExecutionException) {
                swipeRefresh.isRefreshing = false
                L.d("VKApiExecutionException = " + error.errorMsg)
            }
        })
    }

    private fun setAdapter(){
        if (context != null) {
            adapter = DocsAdapter(context!!, docs, this, this)
            docsRV.adapter = adapter
            tsb = TimerSearchBreaker(context, TimerSearchBreaker.ISearchTask { loadDocuments() })
        }
    }
    override fun onScroll() {
        if (tsb != null) {
            tsb!!.run("")
        }
    }

    private fun createShareDialog(position: Int, vkDoc: VKDoc){
        if (context == null || activity == null){
            return
        }
        val bottomSheetDialog = BottomSheetDialog(context!!, R.style.DialogStyle)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null);
        val bottomController = RenameDocController()
        val dialog = bottomController.createDialog(bottomSheetDialog, bottomSheetView, context!!, vkDoc, object : OnDocRenamed{
            override fun onRenamed(result: Int) {
                if (result == 1 && adapter != null){
                    adapter!!.notifyDataSetChanged()
                }
            }

        })
        dialog.show()
        KeyboardHelper.setKeyboardVisibilityListener(object : OnKeyboardVisibilityListener {
            override fun onVisibilityChanged(visible: Boolean) {
                if (!visible){
                    bottomController.keyboardInvisible()
                } else {
                    bottomController.keyboardVisible()
                }
            }
        }, activity)

    }


    override fun onClick(vkDoc: VKDoc) {
        if (activity != null){
            (activity as MainActivity).setDetailFragment(vkDoc)
        }

    }

    override fun onRenameClicked(vkDoc: VKDoc, position: Int) {
        createShareDialog(position, vkDoc)
    }
}