package com.psssum.docs.models

import com.psssum.docs.utils.DocType

class VKDoc(var name: String,var ext: String = "",var url: String,var date: Long, var id: String, var tags : String = "", var size : Long, var type : Int, var resourceId : Int, var owner_id : String, var description : String = "")
class VKUser(var id : String, var first_name : String, var last_name : String)