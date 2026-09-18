package mg.itu.cycledevie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val tag = "CYCLE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate — l'écran se construit (instance ${hashCode()})")
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSecond).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        findViewById<Button>(R.id.btnPartager).setOnClickListener {
            partagerCollecte()
        }
    }

    private fun partagerCollecte() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Collecte du jour : 4,5 kg de vanille")
        }
        startActivity(Intent.createChooser(intent, "Partager la collecte"))
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart — l'écran devient visible")
    }

    override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume — premier plan, interactif")
    }

    override fun onPause() {
        Log.i(tag, "onPause — perd le premier plan")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "onStop — plus visible")
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(tag, "onRestart — l'écran stoppé va redevenir visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "onDestroy — instance détruite (instance ${hashCode()})")
        super.onDestroy()
    }
}