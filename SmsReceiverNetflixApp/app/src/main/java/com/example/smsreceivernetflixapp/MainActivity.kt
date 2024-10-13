package com.example.smsreceivernetflixapp
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
class MainActivity : AppCompatActivity(), SmsReceiver.MessageListener {
    private var smsReceiver: SmsReceiver? = null
    private var messageTextView: TextView? = null // Declara a TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate: Configurando o SmsReceiver")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate executed")
        // Inicializa a TextView
        messageTextView = findViewById(R.id.messageTextView)
        // Inicializa o SmsReceiver
        smsReceiver = SmsReceiver()
        smsReceiver!!.setMessageListener(this)
        // Verifica permissões
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                1
            )
        } else {
            Toast.makeText(this, "Permissões já concedidas", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onMessageReceived(message: String) {
        Log.d("MainActivity", "Mensagem recebida: $message")
        if (messageTextView != null) {
            if (message.contains("Netflix", ignoreCase = true)) {
                Log.d("MainActivity", "Atualizando TextView com mensagem: $message")
                runOnUiThread {
                    messageTextView!!.text = message
                }
            }
        } else {
            Log.e("MainActivity", "messageTextView é null!")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            Log.d("MainActivity", "onRequestPermissionsResult: " + grantResults.joinToString(", "))
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissões concedidas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissões negadas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
