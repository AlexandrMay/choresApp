package com.may.choreapp.activity


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.may.choreapp.R
import com.may.choreapp.data.ChoresDbHandler
import com.may.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var dbHandler: ChoresDbHandler
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        dbHandler = ChoresDbHandler(this)


        saveChore.setOnClickListener {
            progressDialog.setMessage("Saving...")
            progressDialog.show()
            if (!TextUtils.isEmpty(enterChoreId.text.toString()) &&
                !TextUtils.isEmpty(assignedById.text.toString()) &&
                !TextUtils.isEmpty(assignToId.text.toString())){
                val chore = Chore()
                chore.choreName = enterChoreId.text.toString()
                chore.assignedTo = assignToId.text.toString()
                chore.assignedBy = assignedById.text.toString()
                saveToDb(chore)
                progressDialog.cancel()
                startActivity(Intent(this, ChoreListActivity::class.java))

            } else {
                Toast.makeText(this, "Enter all values", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun saveToDb(chore: Chore){
        dbHandler.createChore(chore)
    }


}
