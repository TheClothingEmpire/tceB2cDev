package com.clothingempire.clothingempire.ui.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityAddProductsBinding
import com.clothingempire.clothingempire.models.Product
import com.clothingempire.clothingempire.models.Variant
import com.clothingempire.clothingempire.ui.adapters.VariantAdaptor
import com.clothingempire.clothingempire.ui.adapters.ViewPagerAdapter
import com.clothingempire.clothingempire.ui.adapters.ViewPagerAdapterUri
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import top.defaults.colorpicker.ColorPickerPopup;
import yuku.ambilwarna.AmbilWarnaDialog


class AddProductsActivity : BaseActivity(),View.OnClickListener {
    var binding: ActivityAddProductsBinding?=null
    private var mColorPreview: String = "FFFFFF"
    private var imageList=ArrayList<String>()
    //private var fbsImage:String?=null
    //private var saveImage: Uri?=null
    var mArrayUri=ArrayList<Uri>()
    var minimumQuantity=1
    var maximumTime=30
    var quantityPerPacket=1
    var mVariantArray:ArrayList<Variant> = ArrayList()
    var updateProfile:Int=0
    private var originalImage=ArrayList<String>()
    lateinit var documentId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityAddProductsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        //binding?.etProductTitle?.setText("hi")

        //setting on click listeners
        binding?.btnSubmit?.setOnClickListener(this)
        binding?.ivAddUpdateProduct?.setOnClickListener(this)
        binding?.tvAddVariant?.setOnClickListener(this)
        setupActionBar(binding?.toolbarAddProductActivity!! ,"Add Product")
        binding?.tvSelectColor?.setOnClickListener {
            colorPicker()
        }
        if(intent.hasExtra(Constants.PRODUCTS)){
            Toast.makeText(this, "Fuck off", Toast.LENGTH_SHORT).show()
        }
        loadProductDataForUpdate()

    }
    fun loadProductDataForUpdate(){

            if(intent.hasExtra(Constants.PRODUCTS)){
                updateProfile=1

                val product:Product=
                    if (Build.VERSION.SDK_INT >= 33) {
                        intent.getParcelableExtra(Constants.PRODUCTS, Product::class.java)!!
                    } else {
                        intent.getParcelableExtra(Constants.PRODUCTS)!!
                    }

                Log.e("updateProduct",product.title)
                imageList=product.image
                binding?.etProductTitle?.setText(product.title)
                binding!!.etProductPerProduct.setText(product.quantityPerPacket.toString())
                //Toast.makeText(this, product.title, Toast.LENGTH_SHORT).show()
                val mViewPagerAdapter = ViewPagerAdapter(this, imageList)
                binding!!.viewPagerMain.adapter=mViewPagerAdapter
                //Constants().loadImage(this,product.image[0],binding!!.ivProductImage)
                for(variant in product.variants) {
                    mVariantArray.add(variant)
                }
                loadVariantList()

                documentId=product.id


                binding!!.etProductDescription.setText(product.description.toString())
                binding!!.etProductPrice.setText(product.price.toString())
                binding!!.etProductMinimumQuantity.setText(product.minimumQuantity.toString())
                Log.e("updateProduct",product.minimumQuantity.toString())
                binding!!.etProductMaximumTime.setText(product.maximumTime.toString())
                binding!!.etMaterialUsed.setText(product.material)
                binding!!.btnSubmit.setText("Update Product")
                //binding!!.btnSubmit.setText("Update Product")
                Log.e("ProductUpdate",documentId)
            }


    }
// to set onClickListener
    override fun onClick(p0: View?) {
        when(p0?.id){
            //to add image to the product
            R.id.iv_add_update_product-> Constants().choosePhotoFromGallery(this)
            R.id.btn_submit->{
                if(validateProductDetails()){
                    uploadImage()
                }
            }
            R.id.tv_Add_variant->addVariant()
        }
    }
    //to set image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.Gallery) {
            // if we return to the activity from the Gallery Activity after selecting the image
                // this function will load the image into the ImageView
            if (data != null) {
                val position = if (data.clipData != null) {
                    val mClipData = data.clipData
                    val cout = data.clipData!!.itemCount
                    for (i in 0 until cout) {
                        // adding imageuri in array
                        val imageurl = data.clipData!!.getItemAt(i).uri
                        mArrayUri!!.add(imageurl)
                    }
                    Log.e("ImageURIList",mArrayUri.toString())
                    //mArrayUri[0] = data!!.data!!
                    /*Glide.with(this)
                        .load(mArrayUri[0])
                        .fitCenter()
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(binding?.ivProductImage!!)*/
                    val mViewPagerAdapter = ViewPagerAdapterUri(this, mArrayUri)
                    binding!!.viewPagerMain.adapter=mViewPagerAdapter
                    0
                } else {
                    val imageurl = data.data
                    mArrayUri!!.add(imageurl!!)

                  /*  Glide.with(this)
                        .load(mArrayUri[0])
                        .fitCenter()
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(binding?.ivProductImage!!)*/
                    val mViewPagerAdapter = ViewPagerAdapterUri(this, mArrayUri)
                    binding!!.viewPagerMain.adapter=mViewPagerAdapter
                    0
                }
            } else {
                // show this if no image is selected
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
            }

                    }


            }



    //this function is used to verify weather all the data has been entered by the user
    private fun validateProductDetails(): Boolean {

        return when {

            TextUtils.isEmpty(binding?.etProductDescription?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description), true)
                false
            }

            TextUtils.isEmpty(binding?.etProductPrice?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }
            /*TextUtils.isEmpty(binding?.etProductQuantity?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity), true)
                false
            }*/
            TextUtils.isEmpty(binding?.etProductTitle?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }
            mArrayUri.isEmpty()->{
                showErrorSnackBar(resources.getString(R.string.err_msg_upload_product_image), true)
                false
            }
            else -> {

                true
            }
        }
    }
    //Upload Image to CLoud
    private fun uploadImage(){
        customProgressDialogOpener("please wait")
        if(updateProfile==1 && imageList==originalImage){
            Toast.makeText(this, "inside update", Toast.LENGTH_SHORT).show()
            //customProgressDialogOpener("please wait")
            updateProduct()
        }
        else if(!mArrayUri.isEmpty()){
            for(saveImage in mArrayUri){
                val sref: StorageReference =
                    FirebaseStorage.getInstance()
                        .reference.child("productImage"+System.currentTimeMillis()
                                +"."+getFileExtension(saveImage))
                sref.putFile(saveImage)
                    .addOnSuccessListener { taskSnapshot ->
                        Log.e(
                            "Firebase Image URL",
                            taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                        )
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                                uri -> Log.i("imageUri",uri.toString())
                            imageList.add(uri.toString())
                            if(mArrayUri.indexOf(saveImage)==mArrayUri.size-1){
                                if(updateProfile==1){
                                    updateProduct()
                                }else{
                                    updateFirestoreProduct()
                                }
                            }


                        }
                        customProgressDialogCloser()
                    }.addOnFailureListener{
                            exception->
                        Log.i("image",exception.message.toString())
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                        customProgressDialogCloser()
                    }
            }
        }else{
            Toast.makeText(this, "no image found", Toast.LENGTH_SHORT).show()
            updateFirestoreProduct()

        }

    }
    //Function to upload data to online database
    private fun updateFirestoreProduct() {
        try {
            //this function will upload all the data to the firebase db
            val title = binding?.etProductTitle?.text?.trim(' ').toString()
            val description = binding?.etProductDescription?.text?.trim(' ').toString()
            val price: Float = (binding?.etProductPrice?.text?.trim(' ')).toString().toFloat()
           // val quantity = binding?.etProductQuantity?.text?.trim(' ').toString().toInt()
            maximumTime=binding?.etProductMaximumTime?.text!!.toString().toInt()
            minimumQuantity=binding?.etProductMinimumQuantity?.text!!.toString().toInt()
            quantityPerPacket=binding?.etProductPerProduct?.text!!.toString().toInt()

            val material=binding?.etMaterialUsed?.text!!.toString()
            val product = Product(
                title =  title,
                description =  description,
                price = price,
                createdOn=System.currentTimeMillis(),

                image = imageList,
                createdBy = FirebaseAuth.getInstance().uid.toString(),
                maximumTime = maximumTime,
                minimumQuantity = minimumQuantity,
                quantityPerPacket = quantityPerPacket,
                material = material,
                variants = mVariantArray
            )


            FirebaseClass().registerProduct(this, product)

        }catch (e:Exception){
            Log.e("uploadProduct",e.message.toString())
        }


    }
    fun updateProduct(){
        val title = binding?.etProductTitle?.text?.trim(' ').toString()
        val description = binding?.etProductDescription?.text?.trim(' ').toString()
        val price: Float = (binding?.etProductPrice?.text?.trim(' ')).toString().toFloat()
        maximumTime=binding?.etProductMaximumTime?.text!!.toString().toInt()
        minimumQuantity=binding?.etProductMinimumQuantity?.text!!.toString().toInt()
        quantityPerPacket=binding?.etProductPerProduct?.text!!.toString().toInt()
        val material=binding?.etMaterialUsed?.text!!.toString()
        var hash=HashMap<String,Any>()
        hash["description"]=description
        hash["price"]=price
        hash["image"]=imageList
        hash["maximumTime"]=minimumQuantity
        hash["quantityPerPacket"]=quantityPerPacket
        hash["material"]=material
        hash["variants"]=mVariantArray
        hash["title"]=title
        hash["updatedOn"]=System.currentTimeMillis()
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
        Log.e("ProductUpdate",documentId)
        FirebaseClass().updateProduct(this,hash,documentId)
    }
    fun updateSuccess(){
        customProgressDialogCloser()
        showErrorSnackBar("image Uploaded Successfully", false)
        finish()
    }
    //Function to get file extension
    private fun getFileExtension(uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    private fun addVariant(){

         val check=   when {


             TextUtils.isEmpty(binding?.etSize?.text.toString()) -> {
                 showErrorSnackBar(
                     resources.getString(R.string.err_msg_enter_product_description),
                     true
                 )
                 false
             }
             TextUtils.isEmpty(binding?.etQuantity?.text.toString()) -> {
                 showErrorSnackBar(
                     resources.getString(R.string.err_msg_enter_product_description),
                     true
                 )
                 false
             }
             else -> {
                 true

             }
         }
         if (check){
             val variant=Variant(
                 type="color",
                 name = binding?.etName?.text.toString(),
                 value = mColorPreview,
                 size = binding?.etSize?.text.toString(),
                 quantity = binding?.etQuantity?.text.toString()

             )
             mVariantArray.add(variant)
             loadVariantList()
         }


    }
    //Refresh Variant List in  Products
    private fun loadVariantList() {
        try{
            val adapter=VariantAdaptor(this,mVariantArray)
            binding?.rvVariants?.adapter=adapter
            binding?.rvVariants?.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        }catch (e:Exception){
            Log.e("updateProduct",e.message.toString())
        }
    }
    //Function to delete Variant
    fun deleteItem(position: Int) {
        mVariantArray.removeAt(position)
        loadVariantList()
    }

    //Function to open Color Picker Dialog
     fun colorPicker(){
         binding!!.tvSelectColor.setOnClickListener(
             object : OnClickListener {
                 override fun onClick(v: View?) {
                     // to make code look cleaner the color
                     // picker dialog functionality are
                     // handled in openColorPickerDialogue()
                     // function
                     openColorPickerDialogue()
                 }
             }
         )
     }

    // the dialog functionality is handled separately
    // using openColorPickerDialog this is triggered as
    // soon as the user clicks on the Pick Color button And
    // the AmbilWarnaDialog has 2 methods to be overridden
    // those are onCancel and onOk which handle the "Cancel"
    // and "OK" button of color picker dialog
    fun openColorPickerDialogue() {


        val colorPickerDialogue = AmbilWarnaDialog(this, R.color.black,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // leave this function body as
                    // blank, as the dialog
                    // automatically closes when
                    // clicked on cancel button
                }

               override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    // change the mDefaultColor to
                    // change the GFG text color as
                    // it is returned when the OK
                    // button is clicked from the
                    // color picker dialog
                    mColorPreview = Integer.toHexString(color)
                   binding!!.tvSelectColor.setBackgroundColor(color)

                    // now change the picked color
                    // preview box to mDefaultColor

                }
            })
        colorPickerDialogue.show()
    }
}