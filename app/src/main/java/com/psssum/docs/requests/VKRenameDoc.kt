package com.psssum.docs.requests

import com.psssum.docs.models.VKDoc
import com.psssum.docs.utils.DocType
import com.psssum.docs.utils.getIconByType
import com.psssum.market.utils.L
import com.psssum.market.utils.buildString
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder

class VKRenameDoc(private val owner_id : String, private val doc_id : String, private val title : String = "", private val tags : String): ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val callBuilder = VKMethodCall.Builder()
            .method("docs.edit")
            .args("owner_id", owner_id)
            .args("doc_id", doc_id)
            .args("title", title)
            .args("tags", tags)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), VKChangeDocParser())
    }


    private class VKChangeDocParser : VKApiResponseParser<Int> {
        override fun parse(response: String): Int{
            try {
                return if (JSONObject(response).has("response")) {
                    JSONObject(response).getInt("response")
                } else {
                    0
                }
            } catch (ex: JSONException) {
                L.d("ServerUploadInfoParser ex = " + ex.localizedMessage)
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

}