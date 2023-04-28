package com.example.kb

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View

class HciInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    private var keyboardView: KeyboardView? = null
    private var keyboard: Keyboard? = null

    private var caps = false

    override fun onPress(p0: Int) {
        Log.i("pressed key", p0.toChar().toString())
        Log.i("timestampMillis", System.currentTimeMillis().toString())
        when (p0) {
                Keyboard.KEYCODE_DELETE, Keyboard.KEYCODE_SHIFT, Keyboard.KEYCODE_DONE, 32 -> {
                keyboardView!!.setPreviewEnabled(false)
            }
            else -> {
                keyboardView!!.setPreviewEnabled(true)
            }
        }
    }

    override fun onRelease(p0: Int) {
        when (p0) {
            Keyboard.KEYCODE_DELETE, Keyboard.KEYCODE_SHIFT, Keyboard.KEYCODE_DONE, 32 -> {
                keyboardView!!.setPreviewEnabled(false)
            }
            else -> {
                keyboardView!!.setPreviewEnabled(true)
            }
        }
    }

    override fun onKey(p0: Int, p1: IntArray?) {
        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            when (p0) {
                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
                Keyboard.KEYCODE_SHIFT -> {
                    caps = !caps
                    keyboard!!.isShifted = caps
                    keyboardView!!.invalidateAllKeys()
                }
                Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )
                else -> {
                    var code = p0.toChar()
                    if (Character.isLetter(code) && caps) {
                        code = code.uppercaseChar()
                    }
                    inputConnection.commitText(code.toString(), 1)
                    caps = false
                    keyboard!!.isShifted = caps
                    keyboardView!!.invalidateAllKeys()
                }
            }
        }
    }

    override fun onText(p0: CharSequence?) {
//        TODO("Not yet implemented")
    }

    override fun swipeLeft() {
//        TODO("Not yet implemented")
    }

    override fun swipeRight() {
//        TODO("Not yet implemented")
    }

    override fun swipeDown() {
//        TODO("Not yet implemented")
    }

    override fun swipeUp() {
//        TODO("Not yet implemented")
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keys_layout)
        keyboardView!!.keyboard = keyboard
        keyboardView!!.setOnKeyboardActionListener(this)
        keyboardView!!.setPreviewEnabled(false)
        return keyboardView!!
    }
}