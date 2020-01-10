package com.jasonjerome.circlelayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jasonjerome.circlelayout.util.RandomColors
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.round_image.view.*

class MainActivity : AppCompatActivity() {

    private val randomColors = RandomColors()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circleLayout.sectionLayout = R.layout.round_image

        lessButton.setOnClickListener {
            setCircleSections(circleLayout.sectionCount - 1)
        }

        moreButton.setOnClickListener {
            setCircleSections(circleLayout.sectionCount + 1)
        }

        setCircleColors()
    }

    private fun setCircleColors() {
        circleLayout.sectionList.forEach { view ->
            val tag = view.tag
            if (tag == null) {
                view.image_id.setBackgroundColor(randomColors.color)
                view.tag = true
            }
        }
    }

    private fun setCircleSections(newSectionCount: Int) {
        circleLayout.sectionCount = newSectionCount
        setCircleColors()
    }

}
