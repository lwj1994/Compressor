package me.shouheng.sample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shouheng.compress.Compress
import me.shouheng.compress.strategy.Strategies
import me.shouheng.compress.strategy.config.ScaleMode
import me.shouheng.mvvm.base.CommonActivity
import me.shouheng.mvvm.base.anno.ActivityConfiguration
import me.shouheng.mvvm.comn.EmptyViewModel
import me.shouheng.sample.databinding.ActivitySampleBinding
import me.shouheng.utils.ui.ImageUtils
import me.shouheng.utils.ui.ToastUtils
import me.shouheng.utils.ui.ViewUtils
import java.io.ByteArrayOutputStream

/**
 * Sample strategy activity
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version 2019/5/17 22:04
 */
@ActivityConfiguration(layoutResId = R.layout.activity_sample)
class SampleActivity : CommonActivity<ActivitySampleBinding, EmptyViewModel>() {

    override fun doCreateView(savedInstanceState: Bundle?) {
        val d = Observable.create<Bitmap> {
            val bitmap = Compress.with(this, BitmapFactory.decodeResource(resources, R.drawable.img_lena))
                .strategy(Strategies.compressor())
                .setMaxHeight(100f)
                .setMaxHeight(120f)
                .setScaleMode(ScaleMode.SCALE_LARGER)
                .asBitmap()
                .get()
            val result = ImageUtils.addCircleBorder(bitmap, ViewUtils.dp2px(1f), Color.RED)
            it.onNext(result)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            binding.iv2.setImageBitmap(it)
        }, {
            toast("error : $it")
        })
        val d2 = Observable.create<Bitmap> {
            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_lena)
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            val byteArray = out.toByteArray()
            bitmap = Compress.with(this@SampleActivity, byteArray)
                .strategy(Strategies.compressor())
                .setMaxHeight(50f)
                .setMaxHeight(60f)
                .setScaleMode(ScaleMode.SCALE_LARGER)
                .asBitmap()
                .get()
            val result = ImageUtils.addCornerBorder(bitmap, ViewUtils.dp2px(1f),
                Color.GREEN, ViewUtils.dp2px(20f).toFloat())
            it.onNext(result)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            binding.iv3.setImageBitmap(it)
        }, {
            toast("error : $it")
        })
    }
}
