package com.example.macetapp40.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import com.example.macetapp40.Login
import com.example.macetapp40.ShareDataViewModel
import com.example.macetapp40.ViewModelState
import kotlinx.android.synthetic.main.fragment_home.imgPlant
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.example.macetapp40.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.tv_hHumidity

private const val ARG_PARAM1 = "email"
private const val ARG_PARAM2 = "plantName"
private const val ARG_PARAM3 = "plantSensor"
private const val ARG_PARAM4 = "userId"
private const val ARG_PARAM5 = "userImage"

class HomeFragment() : Fragment() {

    private val shareDataViewModelViewModel : ShareDataViewModel by sharedViewModel()
    private var email: String? = null
    private var userName: String? = null
    private var plantName: String? = null
    private var plantSensor: String? = null
    private var userId: String? = null
    private var userImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userImage = FirebaseAuth.getInstance().currentUser?.photoUrl
            email = FirebaseAuth.getInstance().currentUser?.email
            userName = it.getString(ARG_PARAM1)
            plantName = it.getString(ARG_PARAM2)
            plantSensor = it.getString(ARG_PARAM3)
            userId =  it.getString(ARG_PARAM4)
            userImage =  FirebaseAuth.getInstance().currentUser?.photoUrl
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into((view.findViewById(R.id.imgUser) as ImageView))
     //   (view.findViewById(R.id.imgUser) as ImageView).setImageURI(userImage)
        (view.findViewById(R.id.emailTextView) as TextView).text = email
        (view.findViewById(R.id.nameTextView) as TextView).text = userName
        (view.findViewById(R.id.tv_plantName) as TextView).text = plantName
        (view.findViewById(R.id.tv_hHumidity) as TextView).text = plantSensor
        imgPlant.setImageURI(userImage)
        logOutBtn.setOnClickListener {
            val prefs = activity?.getSharedPreferences(getString(com.example.macetapp40.R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
            prefs?.clear()
            prefs?.apply()
            val intent = Intent(activity, Login::class.java)
            requireActivity().startActivity(intent)
            activity?.finishAffinity()
        }
        observerResponse()
        shareDataViewModelViewModel.getUriPhoto()
        userId?.let { shareDataViewModelViewModel.getPlantByUserId(it) }
    }

    private fun decodeBase64(input: String?): Bitmap? {
        val decodedString = Base64.decode(input , Base64.DEFAULT)
        try {
            return BitmapFactory.decodeByteArray(decodedString , 0 , decodedString.size)
        } catch (e: Exception) {
            println(e)
            return null
        }
    }


    private fun observerResponse () {
        shareDataViewModelViewModel.getViewModelState.observe(viewLifecycleOwner) {
                state ->
                when (state) {
                    is ViewModelState.Loading -> {

                    }
                    is ViewModelState.UriPicSuccess -> {
                        if (state.uriPic != null) {
                            imgPlant.setImageURI(state.uriPic)
                        }
                    }
                    is ViewModelState.PlantSuccess -> {
                        nameTextView.text = userName
                        emailTextView.text = email
                        tv_plantName.text = state.plant.name
                        val cleanImage: String = state.plant.image.replace("data:image/png;base64," , "").replace("data:image/jpeg;base64," , "").replace("data:image/jpg;base64," , "")
                        val img: Bitmap? = decodeBase64(cleanImage)
                        imgPlant.setImageBitmap(img)
                        if(!state.plant.code.isNotEmpty())
                        {
                            imgPlant.setImageResource(R.drawable.notimage)
                            Toast.makeText(context, "No assigned plant yet! Please register your plant code", Toast.LENGTH_SHORT).show()
                        }
                        if (state.plant.humidity == null || state.plant.humidity == 0) {
                            tv_hHumidity.text = "--"
                           // Toast.makeText(context, "Go to Plant to register or update it.", Toast.LENGTH_SHORT).show()
                            tv_hLastWatering.text = "--"
                        } else {
                            tv_hHumidity.text = state.plant.humidity.toString()
                        }
                        if (state.plant.date == null || state.plant.date == "") {
                            tv_hLastWatering.text = "   --   "
                        } else {
                            val stringDate = state.plant.date
                            val splitDate = stringDate.split("T")
                            tv_hLastWatering.text = " " + splitDate[0] + " "
                        }
                        if (state.plant.watering == "si") {
                            tv_hWatering.text = "Yes"
                        }
                        if (state.plant.watering == null) {
                            tv_hWatering.text = "--"
                        } else {
                            tv_hWatering.text = "No"
                        }
                    }
                    else -> {
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, Login::class.java)
                        requireActivity().startActivity(intent)
                        activity?.finishAffinity()
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.macetapp40.R.layout.fragment_home, container, false)
    }

}
