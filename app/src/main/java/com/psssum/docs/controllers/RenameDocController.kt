package com.psssum.docs.controllers

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.psssum.docs.R
import com.psssum.docs.models.VKDoc
import com.psssum.docs.requests.VKRenameDoc
import com.psssum.docs.utils.interfaces.OnDocRenamed
import com.psssum.market.utils.L
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class RenameDocController {
    lateinit var renameET: EditText
    lateinit var sendBTN: Button
    lateinit var context: Context
    lateinit var bottomSheetView: View
    lateinit var closeRL: RelativeLayout
    lateinit var dialog: BottomSheetDialog
    lateinit var onDocRenamed: OnDocRenamed
    lateinit var vkDoc: VKDoc
    fun createDialog(
        dialog: BottomSheetDialog,
        bottomSheetView: View,
        context: Context, vkDoc: VKDoc, onDocRenamed: OnDocRenamed
    ): BottomSheetDialog {
        dialog.setContentView(bottomSheetView)
        (bottomSheetView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        renameET = bottomSheetView.renameET
        sendBTN = bottomSheetView.sendBTN
        this.context = context
        this.bottomSheetView = bottomSheetView
        this.dialog = dialog
        closeRL = bottomSheetView.closeRL
        this.onDocRenamed = onDocRenamed
        this.vkDoc = vkDoc
        setData()

        return dialog
    }

    private fun setListeners() {
        sendBTN.setOnClickListener {
            val text = renameET.text.toString()
            if (text.length >= 2) {
                VK.execute(
                    VKRenameDoc(vkDoc.owner_id, vkDoc.id, renameET.text.toString(), vkDoc.tags),
                    object :
                        VKApiCallback<Int> {
                        override fun success(result: Int) {
                            L.d("VKRenameDoc = $result")
                            if (result == 1){
                                vkDoc.name = renameET.text.toString()
                                onDocRenamed.onRenamed(result)
                            }
                            L.d("VKRenameDoc = ${vkDoc.name}")
                        }
                        override fun fail(error: VKApiExecutionException) {
                        }
                    })
                dialog.hide()
            } else {
                Toast.makeText(context, context.getString(R.string.doc_must_have_2), Toast.LENGTH_SHORT).show()
            }
        }
        closeRL.setOnClickListener {
            dialog.hide()
        }

    }

    private fun setData() {
        setListeners()
    }

    fun keyboardInvisible() {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun keyboardVisible() {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


}