package com.example.practicletask
import android.app.AlertDialog;

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicletask.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import org.geeksforgeeks.demo.ListAdapter
import java.util.Locale
import java.util.Locale.getDefault


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ListAdapter
    var addnote=""
    var data= mutableListOf<ListModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        initview()
        initlistener()
        setContentView(binding.root)

    }

    private fun initlistener() {
        binding.edttext.addTextChangedListener(textWatcher)

        binding.btnadd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Notes")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.custom_alert, null)
            builder.setView(customLayout)

            // add a button
            builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                // send data from the AlertDialog to the Activity
                val editText: EditText = customLayout.findViewById(R.id.edttext)
                Toast.makeText(this, editText.text.toString(), Toast.LENGTH_SHORT).show()
                addnote=editText.text.toString()
                if (addnote.isNotEmpty()){
                    val newItem = ListModel((data.size+1).toString(),addnote) // Create a new data object
                    adapter.addItem(newItem, 0) // Add to the top of the list
                    editText.text.clear() // Clear the input field
                    binding.rvc.scrollToPosition(0)
                }
                dialog?.dismiss()
            }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }


        binding.btnaccending.setOnClickListener {
            val sortedList = data.sortedBy { it.notes }
            adapter.updateList(sortedList as MutableList<ListModel>)
        }

        binding.btndecending.setOnClickListener {
            val sortedList = data.sortByDescending { it.notes }
            adapter.updateList(sortedList as MutableList<ListModel> )
        }
    }

    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // this function is called before text is edited
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // this function is called when text is edited
            filter(s.toString())
        }

        override fun afterTextChanged(s: Editable) {
            // this function is called after text is edited
        }
    }


    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist = ArrayList<ListModel>()

        // running a for loop to compare elements.
        for (item in data) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.notes.lowercase(getDefault()).contains(text.lowercase(Locale.getDefault()))) {

                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {

            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist)
        }
    }
    private fun initview() {
        data.add(ListModel("1","UI concepts worth exsisting"))
        data.add(ListModel("2","Book Review : The Design of Everyday Things by Don Norman"))
        data.add(ListModel("3","Animes produced by Ufotable"))
        data.add(ListModel("4","Mangas planned to read"))
        binding.rvc.layoutManager= LinearLayoutManager(this)
        adapter= ListAdapter(this,data)
        binding.rvc.adapter= adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                data.removeAt(viewHolder.adapterPosition)

                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                Toast.makeText(this@MainActivity, "delete item", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(binding.rvc)


    }


}