package com.example.macetapp40.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.macetapp40.R
import kotlinx.android.synthetic.main.fragment_settings.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import com.example.macetapp40.ShareDataViewModel
import com.example.macetapp40.ViewModelState
import com.example.macetapp40.model.Post
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "plantName"
private const val ARG_PARAM3 = "userId"

class SettingsFragment : Fragment() {
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null
    private val shareDataViewModelViewModel : ShareDataViewModel by sharedViewModel()
    private var plantName: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            plantName = it.getString(ARG_PARAM1)
            userId =  it.getString(ARG_PARAM3)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById(R.id.editTextName) as EditText).setText(plantName)
        val adapter = context?.let { ArrayAdapter.createFromResource(it, R.array.plantType, android.R.layout.simple_spinner_item )}
        (view.findViewById(R.id.spinner2) as Spinner).adapter = adapter

        imgFolder.setOnClickListener {
            openGallery()
        }

        saveBtn.setOnClickListener {
            if (editTextPlantCode.text != null) {
                val plantCode = editTextPlantCode.text.toString()
                val plantName = editTextName.text.toString()
                val plantTypeId = 2
                val user = FirebaseAuth.getInstance().currentUser?.uid
                val plantImgBit = imgFolder.drawable.toBitmap()
                val plantImg = encodeImage(plantImgBit)
                val myPost = Post(plantCode, plantName, plantTypeId, user, plantImg)
                shareDataViewModelViewModel.setModifyPlant(myPost)
            } else {
                val plantCode = editTextPlantCode.text.toString()
                val plantName = editTextName.text.toString()
                val plantTypeId = 3
                val user = FirebaseAuth.getInstance().currentUser?.uid
                val plantImgBit = imgFolder.drawable.toBitmap()
                val plantImg = encodeImage(plantImgBit)
                val myPost = Post(plantCode, plantName, plantTypeId, user, plantImg)
                shareDataViewModelViewModel.setNewPlant(myPost)
            }
        }

        observerResponse()
        userId?.let { shareDataViewModelViewModel.getPlantByUserId(it) }

    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    private fun observerResponse() {
        shareDataViewModelViewModel.getViewModelState.observe(viewLifecycleOwner) {
            state ->
            when (state) {
                is ViewModelState.PlantSuccess -> {
                    editTextName.setText(state.plant.name)
                    editTextPlantCode.setText(state.plant.code)
                    val myPlantImage = state.plant.image
                    val myUri = Uri.parse(myPlantImage)
                    val cleanImage: String = state.plant.image.replace("data:image/png;base64," , "").replace("data:image/jpeg;base64," , "")
                    val img: Bitmap? = decodeBase64(cleanImage)
                    imgFolder.setImageBitmap(img)
                }
            }
        }
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
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data as Uri
            shareDataViewModelViewModel.setUriPhoto(imageUri)
            imgFolder.setImageURI(imageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}