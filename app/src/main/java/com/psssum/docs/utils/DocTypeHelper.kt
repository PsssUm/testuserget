package com.psssum.docs.utils

import com.psssum.docs.R

fun getIconByType(type : Int) : Int{
    var resourceId = 0
    when(type){
        DocType.TEXT.type ->{
            resourceId = R.mipmap.ic_placeholder_document_text_72
        }
        DocType.ARCHIVE.type ->{
            resourceId = R.mipmap.ic_placeholder_document_archive_72
        }
        DocType.GIF.type ->{
            resourceId = R.mipmap.ic_placeholder_document_image_72
        }
        DocType.IMAGE.type ->{
            resourceId = R.mipmap.ic_placeholder_document_image_72
        }
        DocType.AUDIO.type ->{
            resourceId = R.mipmap.ic_placeholder_document_music_72
        }
        DocType.VIDEO.type ->{
            resourceId = R.mipmap.ic_placeholder_document_video_72
        }
        DocType.BOOK.type ->{
            resourceId = R.mipmap.ic_placeholder_document_book_72
        }
        DocType.UNDEFINED.type ->{
            resourceId = R.mipmap.ic_placeholder_document_other_72
        }
        DocType.NONE.type ->{
            resourceId = R.mipmap.ic_placeholder_document_other_72
        }
    }
    return resourceId
}