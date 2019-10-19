package com.dk.project.post.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class FrescoPercentTextView : AppCompatTextView {

    private var disposable: Disposable? = null
    private val progress = PublishSubject.create<Int>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        disposable = progress.throttleLast(200, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { integer ->
                text = if (integer == 10000) {
                    ""
                } else {
                    (integer / 100).toString() + "%"
                }
            }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (!disposable?.isDisposed!!) {
            disposable?.dispose()
        }
    }

    fun setProgress(process: Int) {
        progress.onNext(process)
    }
}



