package com.psssum.docs.requests

import android.content.Context
import com.psssum.docs.models.VKDoc
import com.psssum.docs.utils.DocType
import com.psssum.docs.utils.formatDate
import com.psssum.docs.utils.formatNumber
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

class VKGetDocs(private val offset : Int, private val context : Context): ApiCommand<ArrayList<VKDoc>>() {
    override fun onExecute(manager: VKApiManager): ArrayList<VKDoc> {
        val callBuilder = VKMethodCall.Builder()
            .method("docs.get")
            .args("count", 50)
            .args("offset", offset)
            .args("return_tags", 1)
            .args("lang", 0)
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), VKGetDocsParser(context))
    }


    private class VKGetDocsParser(private val context : Context) : VKApiResponseParser<ArrayList<VKDoc>> {
        override fun parse(response: String): ArrayList<VKDoc>{
            try {
                val joResponseItems = JSONObject(response).getJSONObject("response").getJSONArray("items")
                val docs = ArrayList<VKDoc>()
                for (i in 0 until joResponseItems.length()){
                    val item = joResponseItems.getJSONObject(i)
                    val doc = VKDoc(
                        name = item.getString("title"),
                        url = item.getString("url"),
                        date = item.getLong("date"),
                        id = item.getString("id"),
                        size = item.getLong("size"),
                        type = item.getInt("type"),
                        resourceId = getIconByType(item.getInt("type")),
                        owner_id = item.getString("owner_id")
                    )
                    if (item.has("tags")){
                        val tags = item.getJSONArray("tags")
                        var stringTags = StringBuilder()
                        for (i in 0 until tags.length()){
                            val tag = tags.get(i).toString()
                            if (i == 0){
                                stringTags = buildString(tag)
                            } else {
                                stringTags= buildString(stringTags.toString(), ", ", tag)
                            }
                        }
                        doc.tags = stringTags.toString()
                    }
                    val formattedDate = formatDate(doc.date, context)

                    if (item.has("ext")){
                        doc.ext = item.getString("ext")
                    }

                    val description = if (doc.ext != ""){
                        buildString(
                            doc.ext.toUpperCase()," · ",formatNumber(doc.size)," · ",formattedDate)
                    } else {
                        buildString(formatNumber(doc.size), " · ", formattedDate)
                    }
                    doc.description = description.toString()
                    docs.add(doc)
                }
                return docs
            } catch (ex: JSONException) {
                L.d("ServerUploadInfoParser ex = " + ex.localizedMessage)
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

}