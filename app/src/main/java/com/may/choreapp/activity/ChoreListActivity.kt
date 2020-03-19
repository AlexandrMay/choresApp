package com.may.choreapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.may.choreapp.R
import com.may.choreapp.data.ChoreListAdapter
import com.may.choreapp.data.ChoresDbHandler
import com.may.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListActivity : AppCompatActivity() {

    lateinit var dbHandler: ChoresDbHandler
    lateinit var adapter: ChoreListAdapter
    lateinit var choreList: ArrayList<Chore>
    lateinit var choreListItems: ArrayList<Chore>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var dialogBuilder: AlertDialog.Builder
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        dbHandler = ChoresDbHandler(this)

        choreList = ArrayList<Chore>()
        choreListItems = ArrayList<Chore>()
        layoutManager = LinearLayoutManager(this)
        adapter = ChoreListAdapter(choreListItems, this)

        recyclerViewId.layoutManager = layoutManager
        recyclerViewId.adapter = adapter

        choreList = dbHandler.readChores()
        choreList.reverse()

        for (c in choreList.iterator()) {
            val chore = Chore()
            chore.choreName = c.choreName
            chore.assignedBy = c.assignedBy
            chore.assignedTo = c.assignedTo
            chore.showHumanDate(c.timeAssigned)
            chore.id = c.id
            choreListItems.add(chore)
        }

        adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.add_menu_btn){
            Log.d("Item Clicked", "Item Clicked")
            createPopDial()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createPopDial(){
        val view: View = layoutInflater.inflate(R.layout.popup, null)
        val choreName = view.popEnterChore
        val assignedBy = view.popAssignBy
        val assignedTo = view.popAssignTo
        val saveButton = view.popSaveBtn

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder.create()

        dialog.show()

        saveButton.setOnClickListener {
            if (!TextUtils.isEmpty(choreName.text.toString().trim()) &&
                !TextUtils.isEmpty(assignedBy.text.toString().trim()) &&
                !TextUtils.isEmpty(assignedTo.text.toString().trim())) {

                val chore = Chore()
                chore.choreName = choreName.text.toString().trim()
                chore.assignedBy = assignedBy.text.toString().trim()
                chore.assignedTo = assignedTo.text.toString().trim()
                dbHandler.createChore(chore)

                dialog.dismiss()



                startActivity(Intent(this, ChoreListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Enter all values", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
