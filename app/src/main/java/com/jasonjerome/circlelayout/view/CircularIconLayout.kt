package com.jasonjerome.circlelayout.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.jasonjerome.circlelayout.R
import kotlin.math.roundToInt

class CircularIconLayout : ConstraintLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    companion object {
        private const val MIN_SECTIONS = 0
        private const val CIRCLE_RADIUS = 120
    }

    private var circleRadius = CIRCLE_RADIUS
    private lateinit var centerView: TextView

    val sectionList = mutableListOf<View>()

    var sectionCount = MIN_SECTIONS
        set(value) {
            field = if (value < MIN_SECTIONS) MIN_SECTIONS else value
            setupIconRestraints()
        }

    @LayoutRes
    var sectionLayout: Int = R.layout.round_image
        set(value) {
            if (value != field) {
                setupInitialView()
            }
            field = value
            setupIconRestraints()
        }

    private fun init(attrs: AttributeSet?) {
        var requestedSectionCount = MIN_SECTIONS

        setupInitialView()

        attrs?.let {
            val styleValues = context.obtainStyledAttributes(it, R.styleable.CircularIconLayout, 0, 0)
            requestedSectionCount = styleValues.getInt(R.styleable.CircularIconLayout_sections, MIN_SECTIONS)
            circleRadius = styleValues.getInt(R.styleable.CircularIconLayout_radius, CIRCLE_RADIUS)
            styleValues.recycle()
        }

        sectionCount = requestedSectionCount
    }

    private fun setupInitialView() {
        sectionList.clear()
        this.removeAllViews()

        // "center view" - all views will be constrained to this one.
        centerView = TextView(context).apply {
            id = R.id.CircularIconCenter
            layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            visibility = View.INVISIBLE
            text = context.resources.getText(R.string.about)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        this.addView(centerView)
    }

    /**
     * Convert an Int (pixel) value to density pixels (higher res screens have more pixels)
     */
    private fun Int.toDensityPixels(): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).roundToInt()
    }

    /**
     * Updates views based on the sectionCount.
     * If the sectionCount changes, we need to either add/remove views appropriately
     * - `sectionLayout` is the layout file used for each section's view to be displayed
     */
    private fun updateSections() {
        if (sectionCount > sectionList.size) {
            val newSections = sectionCount - sectionList.size
            repeat(newSections) {
                val section = View.inflate(context, sectionLayout, null).apply {
                    id = View.generateViewId()
                }
                sectionList.add(section)
                this.addView(section)
            }
        } else if (sectionCount < sectionList.size) {
            val removeSections = sectionList.size - sectionCount
            repeat(removeSections) {
                this.removeView(sectionList.last())
                sectionList.remove(sectionList.last())
            }
        } //else: same amount of sections, do nothing
    }

    /**
     * 1. Constrain `centerView` to the middle of the (parent) view
     * 2. For each (requested) section, constrain it to `centerView`
     */
    private fun setupIconRestraints() {
        // add/remove sections based on sectionCount
        updateSections()

        // constrain sections to the centerView
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        // constrain centerView to the middle of our (parent) view
        constraintSet.connect(centerView.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP)
        constraintSet.connect(centerView.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM)
        constraintSet.connect(centerView.id, ConstraintSet.START, this.id, ConstraintSet.START)
        constraintSet.connect(centerView.id, ConstraintSet.END, this.id, ConstraintSet.END)

        val distanceFromCenter = circleRadius.toDensityPixels()
        val segmentDegrees: Float = 360.0f / sectionCount.toFloat()
        var angle: Float = segmentDegrees / 2.0f

        // constrain each section to the centerView
        sectionList.forEach { section ->
            constraintSet.constrainCircle(section.id, centerView.id, distanceFromCenter, angle)
            angle += segmentDegrees
        }

        constraintSet.applyTo(this)
    }

}