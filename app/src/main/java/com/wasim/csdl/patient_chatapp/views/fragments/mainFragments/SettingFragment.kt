package com.wasim.csdl.patient_chatapp.views.fragments.mainFragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.base.BaseFragments
import com.wasim.csdl.patient_chatapp.databinding.FragmentSettingBinding
import com.wasim.csdl.patient_chatapp.models.Users
import com.wasim.csdl.patient_chatapp.utils.singleClick
import com.wasim.csdl.patient_chatapp.viewModels.BaseViewModel


class SettingFragment : BaseFragments<BaseViewModel>() {
    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java
    lateinit var binding: FragmentSettingBinding
    var userRefrence: DatabaseReference? = null
    var firebseUser: FirebaseUser? = null
    private var imageUri: Uri? = null
    private var storageRefrence: StorageReference? = null
    private var coverChecker: String? = ""




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeLiveData() {
    }

    override fun init() {
        firebseUser = FirebaseAuth.getInstance().currentUser
        userRefrence =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebseUser!!.uid)
        storageRefrence = FirebaseStorage.getInstance().reference.child("User Images")

        userRefrence!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if (context != null) {
                        binding.tvUSerName.text = user!!.getUsername()

                        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile)
                            .into(binding.imgProfileSetting)

                        Picasso.get().load(user.getCover()).placeholder(R.drawable.cover_pic)
                            .into(binding.imgCover)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.imgProfileSetting.singleClick {
            readPermissions()
            imagePicker.launch("image/*")
        }

        binding.imgCover.singleClick {
            readPermissions()
            coverChecker = "cover"
            imagePicker.launch("image/*")
        }

    }


    private var permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->

        var is_Granted = true
        for (item in map) {
            if (!item.value) {
                is_Granted = false
            }
        }
        if (is_Granted) {
            showToast("Permission Granted")
        } else {
            showToast("Permission Denied")
        }

    }
    private fun readPermissions() {

        var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        if (!hasPermission(permissions[0])) {
            permissionLauncher.launch(permissions)
        }
    }

    private fun hasPermission(permission: String): Boolean {

        return ContextCompat.checkSelfPermission(
            currentActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        imageUri = uri
        uploadInage()
    }

    private fun uploadInage() {


        if (imageUri != null) {

            val loadingBar =
                ProgressDialog(currentActivity()) // 'this' refers to the current Activity or Context
            loadingBar.setMessage("Image is uploading, Please wait...")
            loadingBar.show()

            val fireRef =
                storageRefrence!!.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask: StorageTask<*>
            uploadTask = fireRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                        loadingBar.dismiss()
                    }
                }
                return@Continuation fireRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if (coverChecker == "cover") {
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"] = url
                        userRefrence!!.updateChildren(mapCoverImg)
                        coverChecker = ""
                    } else { //profile image
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["profile"] = url
                        userRefrence!!.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    loadingBar.dismiss()
                }
            }
        }
    }


}