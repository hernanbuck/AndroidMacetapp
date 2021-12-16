package com.example.macetapp40.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
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
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import okio.ByteString.Companion.toByteString
import org.koin.experimental.property.inject
import kotlin.math.PI


private const val ARG_PARAM1 = "plantName"
private const val ARG_PARAM3 = "userId"

class SettingsFragment : Fragment() {
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null
    private val shareDataViewModelViewModel : ShareDataViewModel by sharedViewModel()
    private var plantName: String? = null
    private var userId: String? = null
    private var edit = 0
    private var code: String = ""
    private var oldDraw: String =""
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
            if (edit == 1) {
                if(editTextPlantCode.length().equals(0) || editTextName.length().equals(0) || imgFolder.drawable == null)
                {
                    Toast.makeText(context, "Modify the necessary data and press update", Toast.LENGTH_SHORT).show()
                }else if (getSizeImg(imgFolder.drawable.toBitmap()) >= 1500000) {
                    Toast.makeText(context, "The selected image is to long", Toast.LENGTH_SHORT).show()
                }else
                {
                    val plantCode = editTextPlantCode.text.toString()
                    val plantName = editTextName.text.toString()
                    val plantTypeId = spinner2.selectedItemId.toInt()
                    val user = FirebaseAuth.getInstance().currentUser?.uid
                    val plantImgBit = imgFolder.drawable.toBitmap()
                    val plantImg = encodeImage(plantImgBit)
                    val myPost = Post(plantCode , code , plantName , plantTypeId , user , plantImg)
                    shareDataViewModelViewModel.setModifyPlant(myPost)
                    Toast.makeText(
                            context ,
                            "Plant successfully updated." ,
                            Toast.LENGTH_SHORT
                                  ).show()
                }

            } else {

                if(editTextPlantCode.length().equals(0) || editTextName.length().equals(0) || imgFolder.drawable.toString().equals(oldDraw))
                {
                    Toast.makeText(context, "Fill the form and press add", Toast.LENGTH_SHORT).show()
                }else if (getSizeImg(imgFolder.drawable.toBitmap()) >= 1500000) {
                    Toast.makeText(context, "The selected image is to long", Toast.LENGTH_SHORT).show()
                }else
                {
                    val plantCode = editTextPlantCode.text.toString()
                    val plantName = editTextName.text.toString()
                    val plantTypeId = spinner2.selectedItemId.toInt()
                    val user = FirebaseAuth.getInstance().currentUser?.uid
                    val plantImgBit = imgFolder.drawable.toBitmap()
                    val plantImg = encodeImage(plantImgBit)
                    val myPost = Post(plantCode , code , plantName , plantTypeId , user , plantImg)
                    shareDataViewModelViewModel.setNewPlant(myPost)
                    Toast.makeText(
                            context ,
                            "Plant successfully create." ,
                            Toast.LENGTH_SHORT
                                  ).show()
                }
            }
        }

        observerResponse()
        userId?.let { shareDataViewModelViewModel.getPlantByUserId(it) }

    }
    private fun getSizeImg(bm: Bitmap): Long {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte: ByteArray = baos.toByteArray()
        val lengthbmp = imageInByte.size.toLong()
        return lengthbmp
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
                    if (state.plant.code.isNotEmpty()) {
                        code = state.plant.code
                        spinner2.setSelection(state.plant.typeId)
                        editTextName.setText(state.plant.name)
                        editTextPlantCode.setText(state.plant.code)
                        val myPlantImage = state.plant.image
                        val myUri = Uri.parse(myPlantImage)
                        val cleanImage: String = state.plant.image.replace("data:image/png;base64," , "").replace("data:image/jpeg;base64," , "")
                        val img: Bitmap? = decodeBase64(cleanImage)
                        imgFolder.setImageBitmap(img)
                        edit = 1
                    }else
                    {
                        imgFolder.setImageResource(R.drawable.addimg)
                        spinner2.setSelection(1)
                        oldDraw = imgFolder.drawable.toString()
                        editTextName.setText("")
                        editTextPlantCode.setText("")
                        edit = 0
                    }



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
        gallery.setType("image/*")
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted..
        val mimeTypes = arrayOf("image/jpeg" , "image/png")
        gallery.putExtra(Intent.EXTRA_MIME_TYPES , mimeTypes)
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