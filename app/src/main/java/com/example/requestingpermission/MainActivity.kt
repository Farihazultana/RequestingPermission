package com.example.requestingpermission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    val FINE_LOCATION_RO = 101
    val CAMERA_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTaps()

    }

   /* override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i("TAG", "onResume: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("TAG", "onRestart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i("TAG", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i("TAG", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy: ")
    }
*/


    private fun buttonTaps(){
        val camera = findViewById<Button>(R.id.bt_camera)
        val loc = findViewById<Button>(R.id.bt_location)

        camera.setOnClickListener{
            checkForPermissions(android.Manifest.permission.CAMERA, "camera", CAMERA_RQ)
        }
        loc.setOnClickListener{
            checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION_RO)
        }
    }


    private fun checkForPermissions(permission: String, name: String, requestCode: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission)-> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("TAG", "onRequestPermissionsResult: ")
        fun innerCheck(name: String){
            Log.i("TAG", "innerCheck: onRequestPermissionsResult")
            if(grantResults.isNotEmpty()){

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("TAG", "innerCheck: onRequestPermissionsResult IF")
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }else{
                    Log.i("TAG", "innerCheck: onRequestPermissionsResult IF Else")
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null)
                        )
                    )
                }


            }else{
                Log.i("TAG", "innerCheck: onRequestPermissionsResult ELSE")
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()



                /*startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )*/

            }
        }

        when(requestCode){
            FINE_LOCATION_RO -> innerCheck("location")
            CAMERA_RQ -> innerCheck("camera")
        }
    }



    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access $name is required to use this app")
            setTitle("Permission Required")
            setPositiveButton("OK"){dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
}