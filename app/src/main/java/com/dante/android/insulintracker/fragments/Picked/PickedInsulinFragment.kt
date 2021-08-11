package com.dante.android.insulintracker.fragments.Picked

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dante.android.insulintracker.R
import com.dante.android.insulintracker.model.Insulin
import com.dante.android.insulintracker.viewmodel.InsulinViewModel
import kotlinx.android.synthetic.main.custom_recycler_row.view.*
import kotlinx.android.synthetic.main.fragment_picked_insulin.*
import kotlinx.android.synthetic.main.fragment_picked_insulin.view.*
import java.time.LocalDate


const val MORNING_INJECT_PRESSED = "morning inject"
const val EVENING_INJECT_PRESSED = "evening inject"
const val MORNING_SAVED_DATE = "saved morning date"
const val EVENING_SAVED_DATE = "saved evening date"


class pickedInsulinFragment : Fragment() {

    private var pref: SharedPreferences? = null
    private var morning_inject_visibility: Boolean = true
    private var evening_inject_visibility: Boolean = true
    private var morningSavedDate: String = ""
    private var eveningSavedDate: String = ""

    private val args by navArgs<pickedInsulinFragmentArgs>()

    private lateinit var mInsulinModel: InsulinViewModel
    private val morning_ml = 18
    private val evening_ml = 4
    private val needle_ml = 2

    private var inject = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pref = this.activity?.getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        morning_inject_visibility = pref?.getBoolean(MORNING_INJECT_PRESSED, true)!!
        evening_inject_visibility = pref?.getBoolean(EVENING_INJECT_PRESSED, true)!!
        morningSavedDate = pref?.getString(MORNING_SAVED_DATE, "")!!
        eveningSavedDate = pref?.getString(EVENING_SAVED_DATE, "")!!

        val view = inflater.inflate(R.layout.fragment_picked_insulin, container, false)

        mInsulinModel = ViewModelProvider(this).get(InsulinViewModel::class.java)

        inject = args.currentInsulin.fullness

        view.tv_ml_left.text = args.currentInsulin.fullness.toString()

        morningDateCheck()
        eveningDateCheck()

        view.btn_morning_inject.isEnabled = morning_inject_visibility
        view.btn_evening_inject.isEnabled = evening_inject_visibility


        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = LocalDate.now()

        view.btn_morning_inject.setOnClickListener {
            injection(morning_ml, "Ручка инсулина кончилась!", "Утренняя доза введена!")
            morningSavedDate = currentDate.toString()
            morning_inject_visibility = false
            saveVisibility(
                morning_inject_visibility,
                evening_inject_visibility,
                morningSavedDate,
                eveningSavedDate
            )

            view.btn_morning_inject.isEnabled = morning_inject_visibility
        }

        view.btn_evening_inject.setOnClickListener {
            injection(evening_ml, "Ручка инсулина кончилась!", "Вечерняя доза введена!")
            eveningSavedDate = currentDate.toString()
            evening_inject_visibility = false
            saveVisibility(
                morning_inject_visibility,
                evening_inject_visibility,
                morningSavedDate,
                eveningSavedDate
            )

            view.btn_evening_inject.isEnabled = evening_inject_visibility
        }
    }

    fun injection(ml: Int, message_delete: String, message_inject: String) {

        inject -= ml

        if (inject <= 0) {
            mInsulinModel.deleteInsulin(args.currentInsulin)
            Toast.makeText(requireContext(), message_delete, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_pickedInsulinFragment_to_listFragment)
        } else {
            val updateFullness =
                Insulin(args.currentInsulin.id, args.currentInsulin.name, inject)
            tv_ml_left.text = inject.toString()
            mInsulinModel.updateInsulin(updateFullness)
            Toast.makeText(requireContext(), message_inject, Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteInsulin()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteInsulin() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да") { _, _ ->
            mInsulinModel.deleteInsulin(args.currentInsulin)
            Toast.makeText(requireContext(), "Ручка инсулина удалена!", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_pickedInsulinFragment_to_listFragment)
        }

        builder.setNegativeButton("Нет") { _, _ -> }
        builder.setTitle("Удаление ручки?")
        builder.setMessage("Вы действительно хотите удалить ручку инсулина?")
        builder.create().show()
    }

    private fun saveVisibility(
        mInjectVis: Boolean,
        eInjectVis: Boolean,
        mSavedDate: String,
        eSavedDate: String
    ) {
        val editor = pref?.edit()
        editor?.putBoolean(MORNING_INJECT_PRESSED, mInjectVis)
        editor?.putBoolean(EVENING_INJECT_PRESSED, eInjectVis)
        editor?.putString(MORNING_SAVED_DATE, mSavedDate)
        editor?.putString(EVENING_SAVED_DATE, eSavedDate)
        editor?.apply()
    }

    private fun morningDateCheck() {
        val currentDate = LocalDate.now()

        if (morningSavedDate != currentDate.toString()) {
            morning_inject_visibility = true
            saveVisibility(
                morning_inject_visibility,
                evening_inject_visibility,
                morningSavedDate,
                eveningSavedDate
            )
        }
    }

    private fun eveningDateCheck() {
        val currentDate = LocalDate.now()

        if (eveningSavedDate != currentDate.toString()) {
            evening_inject_visibility = true
            saveVisibility(
                morning_inject_visibility,
                evening_inject_visibility,
                morningSavedDate,
                eveningSavedDate
            )
        }
    }
}