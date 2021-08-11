package com.dante.android.insulintracker.fragments.List

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dante.android.insulintracker.R
import com.dante.android.insulintracker.model.Insulin
import com.dante.android.insulintracker.viewmodel.InsulinViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {

    private lateinit var mInsulinViewModel: InsulinViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //recycler
        val adapter = ListAdapter()
        val recyclerView = view.list_recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //viewmodel
        mInsulinViewModel = ViewModelProvider(this).get(InsulinViewModel::class.java)
        mInsulinViewModel.readAllData.observe(viewLifecycleOwner, Observer { insulin ->
            adapter.setData(insulin)
        })

        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.floatingActionButton.setOnClickListener {
            insertDataToDatabase()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val name = "Levemir FlexPen"
        val fillness = 250

        val insulin = Insulin(0, name, Integer.parseInt(fillness.toString()))
        mInsulinViewModel.addInsulin(insulin)
        Toast.makeText(requireContext(), getString(R.string.successfully_added), Toast.LENGTH_SHORT)
            .show()
    }

    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да") { _, _ ->
            mInsulinViewModel.deleteAll()
            Toast.makeText(requireContext(), "Все ручки с инсулином удалены!", Toast.LENGTH_SHORT)
                .show()
        }

        builder.setNegativeButton("Нет") { _, _ -> }
        builder.setTitle("Удалить всё?")
        builder.setMessage("Вы уверены что хотите удалить все ручки?")
        builder.create().show()
    }
}