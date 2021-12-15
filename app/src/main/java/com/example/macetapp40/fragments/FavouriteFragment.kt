package com.example.macetapp40.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.macetapp40.R
import com.example.macetapp40.ShareDataViewModel
import com.example.macetapp40.ViewModelState
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.imgPlant
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.ByteArrayInputStream
import java.io.InputStream


private const val ARG_PARAM1 = "plantName"
private const val ARG_PARAM2 = "plantStatus"
private const val ARG_PARAM3 = "plantSensor"
private const val ARG_PARAM4 = "userId"

class FavouriteFragment : Fragment() {

    private val shareDataViewModelViewModel : ShareDataViewModel by sharedViewModel()
    private var plantName: String? = null
    private var plantStatus: String? = null
    private var plantSensor: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            plantName = it.getString(ARG_PARAM1)
            plantStatus = it.getString(ARG_PARAM2)
            plantSensor = it.getString(ARG_PARAM3)
            userId =  it.getString(ARG_PARAM4)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById(R.id.tv_plantNameFav) as TextView).text = plantName
        (view.findViewById(R.id.tv_statusFav) as TextView).text = plantStatus
        (view.findViewById(R.id.tv_humidityFav) as TextView).text = plantSensor
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
                    tv_plantNameFav.text = state.plant.name
                    val cleanImage: String = state.plant.image.replace("data:image/png;base64," , "").replace("data:image/jpeg;base64," , "")
                    val img: Bitmap? = decodeBase64(cleanImage)
                    imgPlant.setImageBitmap(img)

                    if (state.plant.date == null || state.plant.date == "") {
                        tv_statusDate.text = "   --   "
                    } else {
                        val stringDate = state.plant.date
                        val splitDate = stringDate.split("T")
                        tv_statusDate.text = " " + splitDate[0] + " "
                    }

                    if (state.plant.humidity == null || state.plant.humidity == 0) {
                        tv_humidityFav.text = "--"
                        Toast.makeText(context, "No assigned plant yet! Please register your plant code", Toast.LENGTH_SHORT).show()
                    } else {
                        tv_humidityFav.text = state.plant.humidity.toString()
                    }
                    if (state.plant.watering == "si") {
                        tv_statusFav.text = "Yes"
                    }
                    if (state.plant.watering == null) {
                        tv_statusFav.text = "--"
                    } else {
                        tv_statusFav.text = "No"
                    }
                }
                else -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

}
