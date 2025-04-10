package edu.temple.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val timerHandler = Handler(Looper.getMainLooper()) { message ->
        // Update the UI with the countdown value
        val countdownValue = message.what
        findViewById<TextView>(R.id.textView).text = countdownValue.toString()
        true
    }

    var timerBinder: TimerService.TimerBinder? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? TimerService.TimerBinder
            if (binder != null) {
                timerBinder = binder
                isBound = true
                timerBinder?.setHandler(timerHandler)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerBinder = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, TimerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)


        findViewById<Button>(R.id.startButton).setOnClickListener {
            timerBinder?.run{
                timerBinder?.start(10) ?: run {

                }
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_start -> {
                timerBinder?.run{
                    timerBinder?.start(10) ?: run {

                    }
                }
            }
            R.id.action_stop -> {
                timerBinder?.stop()
            }
            else -> {
                return false
            }
        }
        return true
    }
}