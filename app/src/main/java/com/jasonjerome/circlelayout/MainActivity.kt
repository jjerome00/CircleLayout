package com.jasonjerome.circlelayout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ibm.icu.text.RuleBasedNumberFormat
import com.jasonjerome.circlelayout.util.RandomColors
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.round_image.view.*
import kotlinx.android.synthetic.main.text_item.view.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private val randomColors = RandomColors()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circleLayout.sectionLayout = R.layout.round_image

        lessButton.setOnClickListener {
            setSections(circleLayout.sectionCount - 1)
        }

        moreButton.setOnClickListener {
            setSections(circleLayout.sectionCount + 1)
        }

        changeLayout.setOnClickListener {
            switchLayout()
        }

        updateViewContents()
    }

    private fun switchLayout() {
        circleLayout.sectionLayout = if (circleLayout.sectionLayout == R.layout.text_item) {
            R.layout.round_image
        } else {
            R.layout.text_item
        }
        updateViewContents()
    }

    private fun setSections(newSectionCount: Int) {
        circleLayout.sectionCount = newSectionCount
        updateViewContents()
    }

    /**
     * Update the chosen view with colors/images/text.
     * It's not really relevant to the demo.
     */
    private fun updateViewContents() {
        circleLayout.sectionList.forEachIndexed { index, view ->
            // I use the tag as a cheap way of keeping track of which views that have not been set yet.
            if (view.tag == null) {
                val color = randomColors.color
                when (circleLayout.sectionLayout) {
                    R.layout.text_item -> {
                        view.textId.text = (index+1).asWord
                        view.textId.setTextColor(color)
                    }
                    R.layout.round_image -> {
                        view.imageId.setImageDrawable(this.resources.getDrawable(R.drawable.ic_face, this.theme))
                        view.imageId.setColorFilter(color)
                    }
                }
                view.tag = true
            }
        }
    }

    /**
     * Fun way to show a number as a word
     */
    private val Int.asWord: String
        get() = this.let {
            var result = this.toString()
            try {
                val locale = Locale(Locale.getDefault().toLanguageTag(), Locale.getDefault().country)
                val ruleBasedNumberFormat = RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT)
                result = ruleBasedNumberFormat.format(this)
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "error: $e")
            }
            result
        }

}
