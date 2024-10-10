package com.bupware.wedraw.android.logic.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FBAuth{

    val auth = FirebaseAuth.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    //val auth: FirebaseAuth = Firebase.auth

}