package com.clothingempire.clothingempire.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.clothingempire.clothingempire.BuildConfig
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityDashboardBinding
import com.clothingempire.clothingempire.models.User
import com.clothingempire.clothingempire.utils.Constants
import com.clothingempire.clothingempire.utils.FirebaseClass
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mSharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseClass().getLatestVersionFromServer(this)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarDashboard    )
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.app_gradient_color_background))
        mSharedPreferences =
            this.getSharedPreferences(Constants.PROGEMANAG_PREFERENCES, Context.MODE_PRIVATE)

        // Variable is used get the value either token is updated in the database or not.
        val tokenUpdated = mSharedPreferences.getBoolean(Constants.FCM_TOKEN_UPDATED, false)
        Log.e("fcmToken","Dashboard activity inside")
        FirebaseClass().getUserByUserId(this)
        // Here if the token is already updated than we don't need to update it every time.
        /*if (tokenUpdated) {
            // Get the current logged in user details.
            // Show the progress dialog.

            FirebaseClass().getUserByUserId(this)
        } else {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener{ token ->
                    updateFCMToken(token)
                }

        }*/


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_products
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        doubleBackToExit()
    }
    /**
     * A function to update the user's FCM token into the database.
     */
     fun updateFCMToken(token: String) {

        val userHashMap = HashMap<String, Any>()
        userHashMap[Constants.FCM_TOKEN] = token

        // Update the data in the database.
        // Show the progress dialog.

        //customProgressDialogOpener("please wait")
        FirebaseClass().updateFirestoreUser(this, userHashMap)
        FirebaseClass().updateShop(this, userHashMap)
    }

    /**
     * A function to populate the result of BOARDS list in the UI i.e in the recyclerView.
     */


    /**
     * A function to notify the token is updated successfully in the database.
     */
    fun tokenUpdateSuccess() {

        customProgressDialogCloser()
        

        // Here we have added a another value in shared preference that the token is updated in the database successfully.
        // So we don't need to update it every time.
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(Constants.FCM_TOKEN_UPDATED, true)
        editor.apply()

        // Get the current logged in user details.
        // Show the progress dialog.
        customProgressDialogOpener("please wait")
        FirebaseClass().getCurrentUserId()
    }

    fun getUserSuccess(loginUser: User) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener{ token ->
                Log.e("fcmToken",token)
                if(loginUser.fcmToken!=token)
                {
                    updateFCMToken(token)
                }
            }

    }
    fun checkAppUpdate(latestVersion:Int){
        val currentVersion = BuildConfig.VERSION_CODE
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Would you like to update now?")
        builder.setPositiveButton("Yes") { dialog, which ->
            val appPackageName = packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        if(latestVersion>currentVersion){
            dialog.show()
        }

    }
}