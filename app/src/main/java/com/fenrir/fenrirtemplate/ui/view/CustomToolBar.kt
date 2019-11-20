package com.fenrir.fenrirtemplate.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.Toolbar
import com.fenrir.fenrirtemplate.R

class CustomToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    Toolbar(context, attrs) {

    var mode: ToolbarMode = ToolbarMode.None
        set(value) {
            field = value
            if (value.icon != null) setNavigationIcon(value.icon)
            else navigationIcon = null
        }

    var onNavClick: (() -> Unit)? = null
        set(value) {
            setNavigationOnClickListener { value?.invoke() }
            field = value
        }

    init {
        if (attrs != null)
            init(context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar))

        setBackgroundColor(Color.WHITE)
    }

    private fun init(tArray: TypedArray) =
        (0..tArray.indexCount)
            .map { tArray.getIndex(it) }
            .forEach { setAttr(it, tArray) }

    private fun setAttr(@StyleableRes attrIndex: Int, tArray: TypedArray) {
        Enum
        when (attrIndex) {
            R.styleable.CustomToolbar_ct_navigationType ->
                mode = ToolbarMode.values().find {
                    it.id == tArray.getInt(attrIndex, TOOLBAR_MODE_NONE)
                } ?: ToolbarMode.None
        }
    }
}

private const val TOOLBAR_MODE_NONE = 0
private const val TOOLBAR_MODE_BACK = 1
private const val TOOLBAR_MODE_MENU = 2

enum class ToolbarMode(val id: Int, val icon: Int? = null) {
    None(TOOLBAR_MODE_NONE, null),
    Back(TOOLBAR_MODE_BACK, R.drawable.icn_arrow_left),
    Menu(TOOLBAR_MODE_MENU, R.drawable.icn_menu_green);

}