package com.mshy.VInterestSpeed.common.ui.view.camera

import android.util.Size
import java.lang.Long.signum

/**
 * Compares two `Size`s based on their areas.
 */
class CompareSizesByArea : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    override fun compare(lhs: Size, rhs: Size) =
            signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}
