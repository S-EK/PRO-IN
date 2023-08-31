package com.jsseok.proin.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jsseok.proin.R

// '언어 전환' 기능 구현에 사용한 Custom View 클래스
class CustomView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.custom_view, this)

        // 설정해둔 속성 값을 뷰에 적용
        val imageView: ImageView = findViewById(R.id.image)
        val textView: TextView = findViewById(R.id.caption)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomView)

        imageView.setImageDrawable(attributes.getDrawable(R.styleable.CustomView_image))
        textView.text = attributes.getString(R.styleable.CustomView_text)

        attributes.recycle()
    }
}