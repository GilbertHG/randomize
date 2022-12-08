package sg.com.fairtech.randomize.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.rv_current_items
import kotlinx.android.synthetic.main.layout_dialog.view.*
import kotlinx.android.synthetic.main.layout_save_randomize.*
import kotlinx.android.synthetic.main.layout_save_randomize.view.*
import sg.com.fairtech.randomize.R
import sg.com.fairtech.randomize.adapter.RandomizeAdapter
import sg.com.fairtech.randomize.adapter.RandomizeListAdapter
import sg.com.fairtech.randomize.model.Randomize
import sg.com.fairtech.randomize.util.Commons
import sg.com.fairtech.randomize.viewmodel.RandomizeViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var randomizeViewModel: RandomizeViewModel
    private lateinit var randomizeAdapter: RandomizeAdapter
    private lateinit var randomizeListAdapter: RandomizeListAdapter

    private lateinit var editItem: EditText
    private lateinit var btnAddItem: Button
    private lateinit var spinner: Spinner
    private lateinit var btnRandomize: Button
    private lateinit var textViewResult: TextView

    private val listRandomizeItem = arrayListOf<String>()
    private val countListRandomize = arrayListOf<Int>()

    private val CHANNEL_ID = "channel_id_randomize"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddItem = findViewById(R.id.btn_add_item)
        editItem = findViewById(R.id.input_add_item)
        spinner = findViewById(R.id.spinner_count_items)
        btnRandomize = findViewById(R.id.btn_randomize)
        textViewResult = findViewById(R.id.text_view_result_randomize)

        val layoutManager = LinearLayoutManager(this)
        rv_current_items.layoutManager = layoutManager

        randomizeAdapter = RandomizeAdapter(listRandomizeItem) { i ->
            listRandomizeItem.removeAt(i)
            randomizeAdapter.notifyItemRemoved(i)
            randomizeAdapter.notifyItemRangeChanged(i, listRandomizeItem.size)
            setSpinner()
        }

        rv_current_items.adapter = randomizeAdapter

        randomizeViewModel = ViewModelProvider(this).get(RandomizeViewModel::class.java)

        btnAddItem.setOnClickListener{
            addItem()
            refreshActivity()
            editItem.text.clear()
        }

        btn_randomize.setOnClickListener{
            if (spinner.selectedItemPosition < 0 || listRandomizeItem.size <= 1) {
                textViewResult.text = ""
                Toast.makeText(this, "Please Add More Item Value", Toast.LENGTH_SHORT).show()
            } else {
                runRandomize()
            }
        }

        setSpinner()

        createNotificationChannel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_item_list -> {
                listRandomizeItem.clear()
                textViewResult.text = ""
                refreshActivity()
                true
            }
            R.id.action_load_list -> {
                showLoadDialog()
                true
            }
            R.id.action_save_list -> {
                showSaveDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addItem() {
        val randomizerItem = editItem.text.toString().trim()
        if (randomizerItem.isBlank()) {
            Toast.makeText(this, "Item Field is Required!", Toast.LENGTH_SHORT).show()
        } else {
            listRandomizeItem.add(randomizerItem)
        }
    }

    private fun refreshActivity() {
        randomizeAdapter.notifyDataSetChanged()
        setSpinner()
    }

    private fun setSpinner() {
        if (listRandomizeItem.isEmpty() || listRandomizeItem.size == 1) {
            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
            countListRandomize.clear()
            for (i in 1 until listRandomizeItem.size) {
                countListRandomize.add(i)
            }
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countListRandomize.reversed())
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //Do Nothing
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Do Nothing
                }

            }
        }
    }

    private fun runRandomize() {
        val result: String = Commons.pickNRandom(listRandomizeItem, spinner.selectedItem.toString().toInt())?.joinToString(separator = ", ") ?: ""
        textViewResult.text = result
        sendNotification(result)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(contentText: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Result")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun showLoadDialog() {
        val loadDialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null)
        val loadDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Select data to load")
            .setView(loadDialogView)

        val randomizeListAdapter = RandomizeListAdapter({ randomize, i ->
            listRandomizeItem.clear()
            listRandomizeItem.addAll(randomize.list_item.removeSurrounding("[", "]").split(",").map { it.toString() }.toTypedArray())
            refreshActivity()
        },  { randomize, i ->
            randomizeViewModel.deleteRandomize(randomize)

        })

        loadDialogView.rv_dialog_list_title.layoutManager = LinearLayoutManager(this)
        loadDialogView.rv_dialog_list_title.adapter = randomizeListAdapter
        randomizeViewModel.getRandomizeList()?.observe(this, {
            randomizeListAdapter.setRandomizeList(it)
            randomizeListAdapter.notifyDataSetChanged()
        })
        loadDialogBuilder.show()
    }

    private fun showSaveDialog() {
        val saveDialogView = LayoutInflater.from(this).inflate(R.layout.layout_save_randomize, null)
        val saveDialogBuilder = AlertDialog.Builder(this)
                                    .setTitle("Enter List Name")
                                    .setView(saveDialogView)
                                    .setPositiveButton("Save") { _, _ ->
                                        if (saveDialogView.input_save_name.text.isNullOrEmpty()) {
                                            Toast.makeText(this, "Randomize Title Required", Toast.LENGTH_SHORT).show()
                                        } else if (listRandomizeItem.isEmpty() || listRandomizeItem.size <= 1) {
                                            Toast.makeText(this, "Please add randomize item at least 2 item", Toast.LENGTH_SHORT).show()
                                        } else {
                                            saveRandomize(saveDialogView.input_save_name.text.toString())
                                        }
                                    }
        saveDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        saveDialogBuilder.show()

    }

    private fun saveRandomize(fileName: String) {
        val randomize = Randomize(
            list_name = fileName,
            list_item = listRandomizeItem.toString()
        )
        randomizeViewModel.insertRandomize(randomize)
    }

//    private fun getRandomizeList() {
//        randomizeViewModel.getRandomizeList()?.observe(this, {
//            randomizeListAdapter.setRandomizeList(it)
//            randomizeListAdapter.notifyDataSetChanged()
//        })
//    }

}