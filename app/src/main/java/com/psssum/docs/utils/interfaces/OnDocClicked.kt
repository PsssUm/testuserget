package com.psssum.docs.utils.interfaces

import com.psssum.docs.models.VKDoc

public interface OnDocClicked {
    fun onClick(vkDoc: VKDoc)
    fun onRenameClicked(vkDoc: VKDoc, position: Int)
}