package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class Password @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    if (s.toString().length < 8) {
                        error = "Password At Least 8 Characters"
                    }
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}