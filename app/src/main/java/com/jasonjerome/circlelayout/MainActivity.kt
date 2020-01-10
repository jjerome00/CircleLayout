package com.jasonjerome.circlelayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jasonjerome.circlelayout.util.RandomColors
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.round_image.view.*
import kotlinx.android.synthetic.main.text_item.view.*

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

        updateColors()
    }

    private fun switchLayout() {
        circleLayout.sectionLayout = if (circleLayout.sectionLayout == R.layout.text_item) {
            R.layout.round_image
        } else {
            R.layout.text_item
        }
        updateColors()
    }

    private fun updateColors() {
        circleLayout.sectionList.forEachIndexed { index, view ->
            // a cheap way of keeping colors for views that haven't been removed
            if (view.tag == null) {
                val color = randomColors.color
                when (circleLayout.sectionLayout) {
                    R.layout.text_item -> {
                        view.textId.setBackgroundColor(color)
                        val label = "${index + 1}"
                        view.textId.text = label
                    }
                    R.layout.round_image -> {
                        view.imageId.setBackgroundColor(color)
                    }
                }
                view.tag = true
            }
        }
    }

    private fun setSections(newSectionCount: Int) {
        circleLayout.sectionCount = newSectionCount
        updateColors()
    }

}
