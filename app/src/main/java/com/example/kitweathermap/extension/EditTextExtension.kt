package com.example.kitweathermap.extension

import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import com.example.kitweathermap.fragment.DrawablePosition

fun EditText.isClickRightIcon(event: MotionEvent, action: (String) -> Unit): Boolean {
    val textString = text.toString()
    if (event.action == MotionEvent.ACTION_UP) {
        if (event.rawX >= right - compoundDrawables[DrawablePosition.DRAWABLE_RIGHT.positionNum].bounds.width()) {
            action(textString)
            return true
        }
    }
    return false
}

fun EditText.isKeyEnter(keyCode: Int,event: KeyEvent,action: (String) -> Unit): Boolean {
    val textString = text.toString()
    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
        action(textString)
        return true
    }
    return false
}
