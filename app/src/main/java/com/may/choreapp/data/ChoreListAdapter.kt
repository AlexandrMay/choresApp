package com.may.choreapp.data

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.may.choreapp.R
import com.may.choreapp.model.Chore
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListAdapter(val chores: ArrayList<Chore>, val context: Context) :
    RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, context, chores)
    }

    override fun getItemCount(): Int {
        return chores.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindViews(chores[position])
    }

     inner class ViewHolder(itemView: View, context: Context, chores: ArrayList<Chore>): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mContext = context
         var mList = chores
        var choreName: TextView = itemView.findViewById(R.id.listChoreName)
        var assignedBy: TextView = itemView.findViewById(R.id.listAssignedBy)
        var assignedDate: TextView = itemView.findViewById(R.id.listDate)
        var assignedTo: TextView = itemView.findViewById(R.id.listAssignedTo)
        var deleteButton: Button = itemView.findViewById(R.id.listDeleteButton)
        var editButton: Button = itemView.findViewById(R.id.listEditButton)

        fun bindViews(chore: Chore) {
            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedDate.text = chore.showHumanDate(System.currentTimeMillis())
            assignedTo.text = chore.assignedTo
            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var mPosition: Int = adapterPosition
            var chore = mList[mPosition]

            when(v!!.id) {
                deleteButton.id -> {
                    deleteChore(chore.id!!.toInt())
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                editButton.id -> {
                    updateChore(chore)
                }

            }
        }



        fun deleteChore(id: Int){
            var db:ChoresDbHandler = ChoresDbHandler(mContext)

            db.deleteChore(id)
        }



         fun updateChore(chore: Chore){
             lateinit var dialogBuilder: AlertDialog.Builder
             lateinit var dialog: AlertDialog
             var dbHandler: ChoresDbHandler = ChoresDbHandler(context)
             val view = LayoutInflater.from(context).inflate(R.layout.popup, null)
             val choreName = view.popEnterChore
             val assignedBy = view.popAssignBy
             val assignedTo = view.popAssignTo
             val saveButton = view.popSaveBtn

             dialogBuilder = AlertDialog.Builder(context).setView(view)
             dialog = dialogBuilder.create()

             dialog.show()

             saveButton.setOnClickListener {
                 if (!TextUtils.isEmpty(choreName.text.toString().trim()) &&
                     !TextUtils.isEmpty(assignedBy.text.toString().trim()) &&
                     !TextUtils.isEmpty(assignedTo.text.toString().trim())) {

                     //val chore = Chore()
                     chore.choreName = choreName.text.toString().trim()
                     chore.assignedBy = assignedBy.text.toString().trim()
                     chore.assignedTo = assignedTo.text.toString().trim()
                     dbHandler.updateChore(chore)
                     notifyItemChanged(adapterPosition, chore)

                     dialog.dismiss()

                 } else {
                     Toast.makeText(context, "Enter all values", Toast.LENGTH_SHORT).show()
                 }
             }
         }

    }
}