package com.psssum.docs.requests


import android.content.Context
import com.psssum.docs.models.VKDoc
import com.psssum.docs.models.VKUser
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

class VKUsersGet(private val context : Context): ApiCommand<VKUser>() {
    override fun onExecute(manager: VKApiManager):VKUser {
        val callBuilder = VKMethodCall.Builder()
            .method("users.get")
            .version(manager.config.version)

        return manager.execute(callBuilder.build(), VKGetUsersParser(context))
    }


    private class VKGetUsersParser(private val context : Context) : VKApiResponseParser<VKUser> {
        override fun parse(response: String): VKUser{
            try {
                val joResponse = JSONObject(response).getJSONArray("response").getJSONObject(0)
                val user = VKUser(id = joResponse.getString("id"),
                                    first_name = joResponse.getString("first_name"),
                                    last_name = joResponse.getString("last_name"))

                return user
            } catch (ex: JSONException) {
                L.d("ServerUploadInfoParser ex = " + ex.localizedMessage)
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

}