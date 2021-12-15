package com.example.macetapp40

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    private var GOOGLE_SIGN_IN = 100
    private var plantName : String = ""
    private var plantSensor : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //SetUp
        setUp()
        session()

    }

    override fun onStart() {
        super.onStart()
        logInLayout.visibility = View.VISIBLE
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val plantName = prefs.getString("plantName", null)
        val plantSensor = prefs.getString("plantSensor", null)
        val userId = prefs.getString("userId", null)


        if(email != null && plantName != null && plantSensor != null && userId != null) {
            logInLayout.visibility = View.INVISIBLE
            showHome(email, plantName, plantSensor, userId)
        }
    }

    private fun setUp() {

        logInBtn.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful) {
                            val userGoogleId = account.id
                            val uiid = FirebaseAuth.getInstance().currentUser?.uid
                            if (uiid != null) {
                                showHome(account.displayName ?: "",plantName, plantSensor, uiid)
                            }

                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error, intente de nuevo")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, plantName: String, plantSensor: String, userId: String) {
        val i = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("plantName", plantName)
            putExtra("plantSensor", plantSensor)
            putExtra("userId", userId)
        }
        startActivity(i)
    }
}