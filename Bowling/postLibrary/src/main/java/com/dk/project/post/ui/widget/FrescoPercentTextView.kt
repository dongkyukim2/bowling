package com.dk.project.post.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import com.dk.project.post.R
import com.dk.project.post.utils.ScreenUtil
import com.dk.project.post.utils.TextViewUtil.INPUT_TEXT_SIZE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class FrescoPercentTextView : AppCompatTextView {

    private var disposable: Disposable? = null
    private val progress = PublishSubject.create<Int>()

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    init {

        ScreenUtil.dpToPixel(100).let {
            val param = RelativeLayout.LayoutParams(it, it)
            param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = param
        }

        setTextColor(AppCompatResources.getColorStateList(context, R.color.colorAccent))

        setTypeface(null, Typeface.BOLD)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, (INPUT_TEXT_SIZE * 2).toFloat())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setListener()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (!disposable?.isDisposed!!) {
            disposable?.dispose()
        }
    }

    fun setProgress(process: Int) {
        progress.onNext(process)
        if (process >= 9900) {
            text = ""
        }
    }

    private fun setListener() {
        if (disposable == null || disposable!!.isDisposed) {
            disposable = progress.throttleLast(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { process ->
                    text = if (process in 100..9899) (process / 100).toString() + "%" else ""
                }
        }
    }
}