package com.clothingempire.clothingempire.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.clothingempire.clothingempire.models.*
import com.clothingempire.clothingempire.ui.activities.*
import com.clothingempire.clothingempire.ui.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.myshoppal.models.Order
import java.lang.Exception

class FirebaseClass {

    var fireStore = FirebaseFirestore.getInstance()
    fun registerUser(userActivity: RegisterActivity, userInfo: User) {
        fireStore.collection(Constants.USERS).document(getCurrentUserId())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                userActivity.userRegistrationsuccess()
            }.addOnFailureListener {
                Toast.makeText(userActivity, "load failed", Toast.LENGTH_SHORT).show()
            }

    }

    fun getCurrentUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (user != null) {
            userId = user.uid
        }
        return userId
    }

    fun getUserByUserId(userActivity: BaseActivity) {
        fireStore.collection(Constants.USERS).document(getCurrentUserId())
            .get().addOnSuccessListener { document ->
                val loginUser = document.toObject(User::class.java)!!
                when (userActivity) {
                    is LogInActivity -> {

                        userActivity.signInSuccess(loginUser)
                    }
                    is SettingsActivity -> {
                        userActivity.userDetailSuccess(loginUser)
                    }
                    is SplashActivity -> {
                        userActivity.userTypeChecker(loginUser)
                    }
                    is CheckOutActivity->{
                        userActivity.getUserSuccess(loginUser)
                    }
                    is DashboardActivity->{
                        userActivity.getUserSuccess(loginUser)
                    }


                }
                Log.e("fcmToken",loginUser.fcmToken)
                (userActivity).customProgressDialogCloser()

            }.addOnFailureListener {
                Log.e("fcmToken",it.message.toString())
                when (userActivity) {
                    is LogInActivity -> {
                        userActivity.customProgressDialogCloser()
                    }
                    is SettingsActivity -> {
                        userActivity.customProgressDialogCloser()

                    }
                }
                Toast.makeText(userActivity, "load failed", Toast.LENGTH_SHORT).show()

            }

    }

    fun registerShop(userActivity: BaseActivity, shop: Shop) {
        fireStore.collection(Constants.SHOP).document()
            .set(shop, SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(userActivity, "shop registered", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(userActivity, "shop registration failed", Toast.LENGTH_SHORT).show()
            }

    }

    fun registerProduct(userActivity: AddProductsActivity, product: Product) {
        fireStore.collection(Constants.PRODUCTS).document()
            .set(product, SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(userActivity, "product registered", Toast.LENGTH_SHORT).show()
                userActivity.updateSuccess()
            }.addOnFailureListener {
                Toast.makeText(userActivity, "product registration failed", Toast.LENGTH_SHORT)
                    .show()
            }

    }


    fun updateFirestoreUser(activity: BaseActivity, userHashMap: HashMap<String, Any>) {
        fireStore.collection(Constants.USERS).document(getCurrentUserId())
            .update(userHashMap).addOnSuccessListener {

                activity.customProgressDialogCloser()
                when(activity){
                    is DashboardActivity->activity.tokenUpdateSuccess()
                }
                //Toast.makeText(activity, "update successful", Toast.LENGTH_SHORT).show()


            }.addOnFailureListener { e ->
                Log.e("updateException", e.message!!)
                activity.customProgressDialogCloser()
                Toast.makeText(activity, "update failed", Toast.LENGTH_SHORT).show()
            }
    }
    fun updateShop(activity: BaseActivity, userHashMap: HashMap<String, Any>) {
        fireStore.collection(Constants.SHOP).whereEqualTo(Constants.CREATED_BY,getCurrentUserId())
            .get().addOnSuccessListener {
                it->
                for(i in it){
                    fireStore.collection(Constants.SHOP)
                        .document(i.id).update(userHashMap).addOnSuccessListener {
                            Log.e("updateShop","Success")
                        }.addOnFailureListener {
                            Log.e("updateShop",it.message.toString())
                        }
                }
                activity.customProgressDialogCloser()

                //Toast.makeText(activity, "update successful", Toast.LENGTH_SHORT).show()


            }.addOnFailureListener { e ->
                Log.e("updateException", e.message!!)
                activity.customProgressDialogCloser()
                Toast.makeText(activity, "update failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun validateShop(activity: BaseActivity, shop: Shop): Boolean {
        var registerUser = false
        //Toast.makeText(activity, "validate shop started ${shop.gst}", Toast.LENGTH_SHORT).show()
        fireStore.collection(Constants.SHOP)
            .whereEqualTo(Constants.GST, shop.gst)
            .get().addOnSuccessListener { document ->
                //Toast.makeText(activity, document.documents.toString(), Toast.LENGTH_SHORT).show()
                try {
                    if (document.isEmpty) {
                        //Toast.makeText(activity, "fuck off", Toast.LENGTH_SHORT).show()
                        registerShop(activity, shop)
                        if (activity is ShopSignIn) {
                            activity.assignShopToUser(shop.gst)
                        }

                    } else {
                        //Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show()
                        try {
                            val shop2 = document.documents[0].toObject(Shop::class.java)!!
                            getShopUserByUID(activity, shop2)
                        } catch (e: Exception) {
                            Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }


                    }
                } catch (e: Exception) {
                    Log.e("fuckOff", e.message.toString())
                }
                activity.customProgressDialogCloser()

            }.addOnFailureListener {
                registerUser = false
            }
        return registerUser
    }

    fun getShopUserByUID(activity: BaseActivity, shop: Shop) {
        val uid = shop.createdBy

        fireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.ID, uid)
            .get().addOnSuccessListener { document ->
                val name = document.documents[0].toObject(User::class.java)!!
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.CLOTHING_EMPIRE_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USER_NAME,
                    "${name.firstName} ${name.lastName}"
                )
                Log.e("mainActivity", document.documents.toString())

                Toast.makeText(
                    activity, "${shop.gst} is already registered as" +
                            " ${shop.name} by createdBy " +
                            "${name.firstName} ${name.lastName}",
                    Toast.LENGTH_SHORT
                ).show()


            }
    }

    fun getProductByCreator(fragment: BaseFragment) {
        fireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.CREATED_BY, getCurrentUserId())
            .get().addOnSuccessListener { document ->
                var productList = ArrayList<Product>()
                for (i in document.documents) {
                    var product = i.toObject(Product::class.java)
                    product?.id = i.id
                    productList.add(product!!)
                    Log.e("getProduct", product.title)
                }
                when (fragment) {
                    is ProductsFragment -> fragment.getProductsSuccess(productList)
                }
            }.addOnFailureListener { e ->
                Log.e("getProductFailed", e.message.toString())

            }
    }

    fun getAllProducts(fragment: BaseFragment) {
        fireStore.collection(Constants.PRODUCTS)
            .get().addOnSuccessListener { document ->

                val productList = ArrayList<Product>()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product?.id = i.id
                    productList.add(product!!)
                    Log.e("getProduct", product.id)
                }
                when (fragment) {
                    is DashboardFragment -> fragment.getProductsSuccess(productList)
                }
            }.addOnFailureListener { e ->
                Log.e("getProductFailed", e.message.toString())

            }
    }

    fun deleteProductByID(id: String, fragment: BaseFragment) {
        fireStore.collection(Constants.PRODUCTS)
            .document(id).delete()
            .addOnSuccessListener {
                Log.e("deleteProduct", "product deleted")
                when (fragment) {
                    is ProductsFragment -> fragment.deleteProductSuccess()
                }
            }
    }

    fun getProductByID(activity: ProductDetailsActivity, id: String) {
        fireStore.collection(Constants.PRODUCTS)
            .document(id).get()
            .addOnSuccessListener {
                var product = it.toObject(Product::class.java)!!
                product.id = id
                activity.getProductSuccess(product)
                Log.e("getProductByID", product.title)
            }.addOnFailureListener {
                Log.e("getProductByID", "failed due to ${it.message.toString()}")
            }
    }

    fun addItemToCart(activity: BaseActivity, cartItem: CartItem) {
        fireStore.collection(Constants.CART_ITEMS).document()
            .set(cartItem, SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(activity, "added to cart", Toast.LENGTH_SHORT).show()
                (activity as ProductDetailsActivity).addToCartSuccess()
            }.addOnFailureListener {
                Toast.makeText(activity, "add to cart failed", Toast.LENGTH_SHORT).show()
            }

    }

    fun checkIfProductInCart(activity: BaseActivity, productId: String) {
        //Toast.makeText(activity, "add to cart failed", Toast.LENGTH_SHORT).show()
        fireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    when (activity) {

                        is ProductDetailsActivity -> activity.addToCartSuccess()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "add to cart failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun getCartItems(activity: BaseActivity) {
        fireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId()).get()
            .addOnSuccessListener {
                val cartList = ArrayList<CartItem>()
                val productList = ArrayList<String>()
                for (i in it.documents) {
                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id
                    cartList.add(cartItem)
                    productList.add(cartItem.product_id)
                }
                //getProductByIDLISt(activity,cartList,productList)
                when (activity) {
                    is CartListActivity -> activity.getCartItemsSuccess(cartList)
                    is CheckOutActivity -> activity.getCartListSuccess(cartList)
                }
                Log.e("getCartList", cartList.size.toString())
            }.addOnFailureListener {
                Log.e("getCartList", "failed due to ${it.message.toString()}")
            }
    }

    fun getProductByIDLISt(
        activity: BaseActivity,
        cartList: ArrayList<CartItem>,
        product: ArrayList<String>
    ) {
        fireStore.collection(Constants.PRODUCTS)
            .whereArrayContains(Constants.ID, product)
            .get().addOnSuccessListener {
                Log.e("productCartLength", it.documents.size.toString())

                /*for(i in it.documents) {
                    for (j in cartList) {
                        val p = i.toObject(Product::class.java)!!
                        //j.stock_quantity = p.stockAvailable.toString()
                        //if (p.stockAvailable == 0) {
                          //  j.stock_quantity = "0"
                        }
                    }
                }*/
                when (activity) {
                    is CartListActivity -> activity.getCartItemsSuccess(cartList)
                }
            }.addOnFailureListener {
                Log.e("productCartException", it.message.toString())
            }
    }

    fun updateCartList(activity: BaseActivity, id: String, userHashMap: HashMap<String, Any>) {
        fireStore.collection(Constants.CART_ITEMS)
            .document(id).update(userHashMap).addOnSuccessListener {
                when (activity) {
                    is CartListActivity -> activity.updateSuccess()
                    is CheckOutActivity -> activity.getCartListSuccess()
                }
            }
    }

    fun deleteCartItemByID(activity: BaseActivity, id: String) {
        fireStore.collection(Constants.CART_ITEMS)
            .document(id).delete()
            .addOnSuccessListener {
                (activity as CartListActivity).updateSuccess()
            }
    }

    fun deleteShopByGST(activity: ShopActivity, id: String) {
        fireStore.collection(Constants.SHOP)
            .document(id)
            .delete().addOnSuccessListener {
                activity.deleteShopSuccess()
            }
    }

    fun getShopList(shopActivity: ShopActivity) {
        fireStore.collection(Constants.SHOP)
            .whereEqualTo(Constants.CREATED_BY, getCurrentUserId())
            .get().addOnSuccessListener {
                val shopList = ArrayList<Shop>()
                for (i in it.documents) {
                    val shop = i.toObject(Shop::class.java)!!
                    shop.documentId = i.id
                    shopList.add(shop)
                }
                shopActivity.getShopListSuccess(shopList)
            }
    }

    fun getShopByID(activity: BaseActivity, id: String) {
        fireStore.collection(Constants.SHOP)
            .document(id)
            .get().addOnSuccessListener {
                val shop = it.toObject(Shop::class.java)!!
                shop.documentId = it.id
                when (activity) {
                    is CheckOutActivity -> activity.getShopSuccess(shop)
                    is MyOrderDetailsActivity -> activity.getShopSuccess(shop)
                    is SoldProductsDetailsActivity -> activity.getShopSuccess(shop)
                }

            }
    }

    fun saveOder(checkOutActivity: CheckOutActivity, order: Order) {
        fireStore.collection(Constants.ORDERS).document()
            .set(order, SetOptions.merge()).addOnSuccessListener {
                checkOutActivity.updateAllDetails()
                Log.e("errorOne", "inside save order success")
                //Toast.makeText(checkOutActivity, "product registration success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(checkOutActivity, "product registration failed", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun updateAllDetails(activity: CheckOutActivity, cartList: ArrayList<CartItem>, order: Order) {
        val writeBatch = fireStore.batch()
        Log.e("errorOne", "inside update all details")
        for (i in cartList) {
            var userid = ""
            Toast.makeText(activity, i.product_id, Toast.LENGTH_SHORT).show()
            Log.e("errorOne", i.title + "inside cartlist")
            fireStore.collection(Constants.PRODUCTS)
                .document(i.product_id).get()
                .addOnSuccessListener {
                    var variantList = ArrayList<Variant>()
                    if (it.exists()) {
                        Log.e("errorOne", "Product exists")
                        val productHash = HashMap<String, Any>()
                        val product = it.toObject(Product::class.java)!!
                        userid = product.createdBy
                        val prodVariantArray = ArrayList<Variant>()
                        for (k in 1..product.variants.size) {
                            val j = k - 1
                            var productQuantity = product.variants[j].quantity.toInt()
                            val cartQuantity = i.cartVariant[j].orderQuantity.toInt()
                            productQuantity -= cartQuantity
                            val prodvar = product.variants[j]
                            val variant = Variant(
                                name = prodvar.name,
                                value = prodvar.value,
                                size = prodvar.size,
                                quantity = productQuantity.toString()

                            )
                            prodVariantArray.add(variant)
                            var soldVariant = Variant(
                                name = i.cartVariant[j].name,
                                value = i.cartVariant[j].value,
                                size = i.cartVariant[j].size,
                                quantity = i.cartVariant[j].orderQuantity,
                                type = i.cartVariant[j].type,
                            )
                            Log.e("errorOne", "saving sold variant " + soldVariant.name)
                            variantList.add(soldVariant)
                        }
                        productHash[Constants.VARIANT] = prodVariantArray
                        try {
                            Log.e("errorOne", "inside try block")
                            
                            fireStore.collection(Constants.PRODUCTS)
                                .document(i.product_id).update(productHash)
                                .addOnSuccessListener {
                                    Log.e("errorOne", "saving  product")
                                    Log.e("ProductUpdateCommit", i.product_id)
                                    val soldProduct = SoldProduct(

                                        userid,
                                        i.title,
                                        i.price,
                                        variantList,
                                        i.image,
                                        order.title,
                                        order.order_datetime,
                                        order.sub_total_amount,
                                        order.shipping_charge,
                                        order.total_amount,
                                        order.address
                                    )
                                    Log.e("errorOne", "saving sold product")
                                    fireStore.collection(Constants.USERS).document(userid)
                                        .get().addOnSuccessListener {it->
                                            val token=it.toObject<User>()!!.fcmToken
                                            soldProduct.fcmToken=token
                                            fireStore.collection(Constants.SOLD_PRODUCTS)
                                                .document().set(soldProduct).addOnSuccessListener {
                                                    Log.e("errorOne", "saving sold_product table")
                                                }.addOnFailureListener {
                                                    Log.e("errorOne", "saving sold_product table failed")
                                                }
                                        }

                                }
                                .addOnFailureListener {
                                    Log.e("ProductUpdateCommit", it.message.toString())
                                    Log.e("errorOne", "saving sold product")
                                }
                        }
                        catch (e: Exception) {
                            Log.e("errorOne", e.message.toString())
                        }
                    }

                    val documentReference = fireStore.collection(Constants.CART_ITEMS)
                        .document(i.id)
                    writeBatch.delete(documentReference)
                    writeBatch.commit().addOnSuccessListener {
                        activity.saveOrderSuccess()
                }

            }

        }
    }

        fun getOrderList(fragment: OrdersFragment) {
            fireStore.collection(Constants.ORDERS)
                .whereEqualTo(Constants.USER_ID, getCurrentUserId())
                .get().addOnSuccessListener {
                    val orderList = ArrayList<Order>()
                    for (i in it.documents) {
                        val order = i.toObject(Order::class.java)!!
                        order.id = i.id
                        orderList.add(order)
                    }
                    fragment.getOrderListSuccess(orderList)
                }
        }


        fun getSoldProducts(fragment: SoldProductsFragment) {
            fireStore.collection(Constants.SOLD_PRODUCTS)
                .whereEqualTo(Constants.USER_ID, getCurrentUserId())
                .get().addOnSuccessListener {
                    val orderList = ArrayList<SoldProduct>()
                    for (i in it.documents) {
                        val order = i.toObject(SoldProduct::class.java)!!
                        order.id = i.id
                        orderList.add(order)
                        Log.e("SoldProductList", order.title)
                    }
                    fragment.getSoldProductListSuccess(orderList)

                }.addOnFailureListener {
                    Log.e("SoldProductList", it.message.toString())
                }
        }
    fun updateProduct(activity: AddProductsActivity,productHash:HashMap<String,Any>,documentId: String){
        fireStore.collection(Constants.PRODUCTS)
            .document(documentId)
            .update(productHash)
            .addOnSuccessListener {
                Log.e("ProductUpdate","Success")
                activity.updateSuccess()
            }
    }

    fun getLatestVersionFromServer(activity: DashboardActivity) {
        fireStore.collection("APP_VERSION").orderBy("version",Query.Direction.DESCENDING).get().addOnSuccessListener {

                val version=it.documents[0].toObject<APP_VERSION>()!!
                activity.checkAppUpdate(version.version)

        }
    }


}