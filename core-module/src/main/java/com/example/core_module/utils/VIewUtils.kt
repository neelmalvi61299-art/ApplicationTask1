package com.example.core_module.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.example.core_module.BuildConfig
import java.util.*

fun printLog(key:String,message:String){
    if (BuildConfig.CORE_DEBUG){
        Log.e(key,message)
    }
}

fun isValidString(value:String?):Boolean{
    return value != null && !value.isEmpty() && !value.equals("null")
}

fun isValidLong(value:Long?):Boolean{
    return value != null && value > 0
}

fun isValidBoolean(value:Boolean?):Boolean{
    return value != null && value
}

fun isValidContact(value:String?):Boolean{
    return value != null && !value.isEmpty() && !value.equals("null") && value.length > 7
}

//fun getRandomColorFromUtil(mContext: Context):Int{
//    val androidColors = mContext.resources.getIntArray(R.array.androidcolors)
//    return androidColors[Random().nextInt(androidColors.size)]
//}


fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun EditText.toText():String{
    return this.text.toString().trim()
}

fun TextView.toText():String{
    return this.text.toString().trim()
}

fun BottomSheetBehavior<LinearLayout>.hideSheet() {
    this.setState(BottomSheetBehavior.STATE_HIDDEN)
}

fun BottomSheetBehavior<LinearLayout>.collapseSheet() {
    this.setState(BottomSheetBehavior.STATE_COLLAPSED)
}

fun BottomSheetBehavior<LinearLayout>.showSheet() {
    this.setState(BottomSheetBehavior.STATE_EXPANDED)
}

fun Context.toast(message : String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Context.dialog(layout: Int, callBack: (View, AlertDialog) -> Unit) {
    val alertDialogLoader = AlertDialog.Builder(this).create()
    val dialogView: View = LayoutInflater.from(this).inflate(layout, null, false)
    alertDialogLoader.setCancelable(false)
    alertDialogLoader.setView(dialogView)
    alertDialogLoader.getWindow()!!.getDecorView().setBackgroundResource(android.R.color.transparent)
    //alertDialogLoader.getWindow()!!.setDimAmount(0.0f)
    alertDialogLoader.show()
    callBack(dialogView, alertDialogLoader)
}


inline fun View.debouncedOnClick(debounceTill: Long = 500, crossinline onClick: (v: View) -> Unit) {
    this.setOnClickListener(object : DebouncedOnClickListener(debounceTill) {
        override fun onDebouncedClick(v: View) {
            onClick(v)
        }
    })
}