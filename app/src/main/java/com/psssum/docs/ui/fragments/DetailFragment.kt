package com.psssum.docs.ui.fragments

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

import com.psssum.docs.R
import com.psssum.docs.models.VKDoc
import com.psssum.docs.ui.activity.MainActivity
import com.psssum.docs.utils.DecoderHelper
import com.psssum.docs.utils.DocType
import com.psssum.docs.utils.formatDate
import com.psssum.docs.utils.formatNumber
import com.psssum.docs.utils.interfaces.OnDownloadComplete
import com.psssum.market.utils.L
import com.psssum.market.utils.buildString
import kotlinx.android.synthetic.main.back_toolbar.view.*
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.view.*
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.net.URLDecoder


class DetailFragment : Fragment(), View.OnClickListener{
    private lateinit var photoIV : ImageView
    private lateinit var closeRL : RelativeLayout
    private lateinit var openInBrowserLL : LinearLayout
    private lateinit var tagsLL : LinearLayout
    private lateinit var descriptionTV : TextView
    private lateinit var nameTV : TextView
    private lateinit var tagsTV : TextView
    private lateinit var videoVV : VideoView
    val handler = Handler(Looper.getMainLooper())
    var vkDoc : VKDoc? = null
    companion object {
        val TAG: String = DetailFragment::class.java.simpleName
        fun newInstance() = DetailFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        photoIV = view.photoIV
        closeRL = view.closeRL
        openInBrowserLL = view.openInBrowserLL
        tagsLL = view.tagsLL
        descriptionTV = view.descriptionTV
        nameTV = view.nameTV
        tagsTV = view.tagsTV
        videoVV = view.videoVV
        setUpView()
        setListeners()
        return view
    }
    private fun setUpView(){
        if (vkDoc != null && context != null){
            when (vkDoc!!.type){
                DocType.IMAGE.type -> {
                    setPhoto()
                }
                DocType.GIF.type -> {
                    setPhoto()
                }
                DocType.VIDEO.type -> {
                   // setPhoto()
                }
                DocType.TEXT.type -> {
                    setText()
                }
            }
            nameTV.text = vkDoc!!.name
            if (vkDoc!!.tags != "") {
                tagsTV.text = vkDoc!!.tags
            } else {
                tagsLL.visibility = View.GONE
            }
            setDescription()
        }
    }
    private fun setListeners(){
        closeRL.setOnClickListener(this)
        openInBrowserLL.setOnClickListener(this)
    }

    private fun setPhoto(){
        Glide.with(context!!)
            .asBitmap()
            .load(vkDoc!!.url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val scale = resource.width.toFloat() / resource.height.toFloat()
                    setLayoutParams(photoIV, scale)
                    Glide.with(context!!).load(vkDoc!!.url).centerCrop().into(photoIV)
                }

            });
        //Picasso.get().load(vkDoc!!.resourceId).fit().centerCrop().into(photoIV)
    }
    private fun setVideo(){
//        download(vkDoc!!.url, object : OnDownloadComplete {
//            override fun onDownload(path: String) {
//                L.d("lenght = " + File("file://" + path).length())
//                handler.post(Runnable {
//                    videoVV.visibility = View.VISIBLE
//                    setLayoutParams(videoVV)
//                    val mediaController = MediaController(context)
//                    mediaController.setAnchorView(videoVV)
//                    videoVV.setMediaController(mediaController)
//                    videoVV.setVideoPath(File("file://" + path).absolutePath)
//                    videoVV.start()
//                })
//            }
//        })
        videoVV.visibility = View.VISIBLE
        setLayoutParams(videoVV, 1.0f)
        videoVV.setVideoPath(vkDoc!!.url)
        videoVV.start()

    }
    private fun setText(){

        download(vkDoc!!.url, object : OnDownloadComplete {
            override fun onDownload(path: String) {
                try {
                    val textBuilder = StringBuilder()
                    File(path).forEachLine(Charsets.UTF_8) {
                        println(it)
                        textBuilder.append(it)
                        textBuilder.append("\n")
                    }
                    handler.post(Runnable {
                        textTV.text = textBuilder
                    })
                } catch (e : Exception){

                }
            }
        })
    }
    fun download(link: String, onDownloadComplete: OnDownloadComplete) {

        val thread = Thread(Runnable {
            try {
            val httpCacheDir = File(context!!.cacheDir, "text." + vkDoc!!.ext)
            URL(link).openStream().use { input ->
                FileOutputStream(File(httpCacheDir.absolutePath)).use { output ->
                    input.copyTo(output)
                }
                onDownloadComplete.onDownload(httpCacheDir.absolutePath)
            }
            } catch (e : Exception){

            }



        })
        thread.start()
    }
    private fun setLayoutParams(view : View, scale : Float){
        val photoParams = view.layoutParams as LinearLayout.LayoutParams
        photoParams.height = (Resources.getSystem().displayMetrics.widthPixels / scale).toInt()
        view.layoutParams = photoParams
    }
    private fun setDescription(){


        descriptionTV.text = vkDoc!!.description

    }
    override fun onClick(v: View?) {
        when (v?.id){
            R.id.closeRL -> {
                if (activity != null){
                    (activity as MainActivity).removeDocFragment()
                }
            }
            R.id.openInBrowserLL -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(vkDoc!!.url))
                context!!.startActivity(browserIntent)
            }
        }
    }
}