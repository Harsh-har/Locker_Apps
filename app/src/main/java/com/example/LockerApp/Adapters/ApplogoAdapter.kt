package com.example.LockerApp.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.LockerApp.Classes.AppLogoClass
import com.example.LockerApp.R

class ApplogoAdapter(
    private var appsList: List<AppLogoClass>,
    private val context: Context,
    private val selectionListener: OnSelectionChangedListener // Listener to communicate checkbox changes
) : RecyclerView.Adapter<ApplogoAdapter.AppLogoViewHolder>() {

    interface OnSelectionChangedListener {
        fun onSelectionChanged(selectedCount: Int)
    }

    // Set to track selected package names
    private val selectedApps = mutableSetOf<String>()

    // SharedPreferences for storing selected apps
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppLockPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    init {
        // Load previously selected apps from SharedPreferences
        loadSelectedApps()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppLogoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.applogostore, parent, false)
        return AppLogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppLogoViewHolder, position: Int) {
        val app = appsList[position]
        holder.appName.text = app.appname  // Display app name
        holder.appIcon.setImageDrawable(app.appIcon)

        // Remove listener before setting checkbox state to avoid unintended triggers
        holder.checkBox.setOnCheckedChangeListener(null)

        // Set checkbox state based on whether the app is selected
        holder.checkBox.isChecked = selectedApps.contains(app.packageName)

        // Handle checkbox click
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            app.isSelected = isChecked
            if (isChecked) {
                selectedApps.add(app.packageName)  // Add to selectedApps if checked
            } else {
                selectedApps.remove(app.packageName)  // Remove from selectedApps if unchecked
            }

            // Save selected apps to SharedPreferences
            saveSelectedApps()

            // Notify selection change
            selectionListener.onSelectionChanged(selectedApps.size)
        }
    }

    override fun getItemCount(): Int {
        return appsList.size
    }

    inner class AppLogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val appName: TextView = itemView.findViewById(R.id.appname)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    // Method to select/deselect all apps
    fun selectAll(checked: Boolean) {
        selectedApps.clear() // Clear previously selected apps
        for (app in appsList) {
            app.isSelected = checked
            if (checked) {
                selectedApps.add(app.packageName)  // Add all package names if selectAll is checked
            }
        }

        // Save selected apps to SharedPreferences
        saveSelectedApps()

        notifyDataSetChanged() // Notify that the dataset has changed
        selectionListener.onSelectionChanged(selectedApps.size) // Inform listener about the change
    }

    // Return selected package names
    fun getSelectedApps(): Set<String> {
        return selectedApps
    }

    // Save the selected apps in SharedPreferences
    private fun saveSelectedApps() {
        editor.putStringSet("lockedApps", selectedApps)
        editor.apply()  // Commit the changes
    }

    // Load the selected apps from SharedPreferences
    private fun loadSelectedApps() {
        val savedApps = sharedPreferences.getStringSet("lockedApps", emptySet())
        selectedApps.clear()
        selectedApps.addAll(savedApps ?: emptySet())

        // Update the appsList to reflect the selected state
        appsList.forEach { app ->
            app.isSelected = selectedApps.contains(app.packageName)
        }
    }

    // Add this method to get the app list
    fun getAppList(): List<AppLogoClass> {
        return appsList
    }
}
