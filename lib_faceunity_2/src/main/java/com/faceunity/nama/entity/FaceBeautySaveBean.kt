package com.faceunity.nama.entity

import com.faceunity.core.model.facebeauty.FaceBeautyBlurTypeEnum
import com.faceunity.core.model.facebeauty.FaceBeautyFilterEnum
import com.faceunity.core.model.facebeauty.FaceBeautyShapeEnum

/**
 * author: Lau
 * date: 2022/7/26
 * description:
 */
class FaceBeautySaveBean {
    //region 滤镜
    /* 滤镜名称 */

    var filterName = FaceBeautyFilterEnum.ORIGIN

    /* 滤镜程度 */
    var filterIntensity = 0.0   //范围0~1 0表示不显示滤镜

    //endregion
    //region 美肤
    /* 朦胧磨皮开关 */
    var enableHeavyBlur = false  //0表示关闭，1表示开启

    /* 肤色检测开关 */
    var enableSkinDetect = false  //0表示关闭，1表示开启

    /* 融合程度 */
    var nonSkinBlurIntensity = 0.0  // 肤色检测之后非肤色区域的融合程度，取值范围0.0-1.0，默认0.0

    /* 磨皮类型 */
    var blurType = FaceBeautyBlurTypeEnum.FineSkin // 清晰磨皮  朦胧磨皮   精细磨皮  均匀磨皮。 此参数优先级比 heavy_blur 低，在使用时要将 heavy_blur 设为 0

    /* 基于人脸的磨皮mask */
    var enableBlurUseMask = false //false表示关闭 true表示开启，使用正常磨皮。只在 blur_type 为 FineSkin 时生效

    /* 磨皮程度 */
    var blurIntensity = 0.0 //范围[0-6]


    /* 美白程度 */
    var colorIntensity = 0.0 //范围 [0-2]


    /* 红润程度 */
    var redIntensity = 0.0 //范围 [0-2]


    /* 锐化程度 */
    var sharpenIntensity = 0.0 //范围 [0-1]


    /* 亮眼程度 */
    var eyeBrightIntensity = 0.0 //范围 [0-1]


    /* 美牙程度 */
    var toothIntensity = 0.0 //范围 [0-1]

    /* 去黑眼圈强度 */
    var removePouchIntensity = 0.0  //范围 [0-1]


    /* 去法令纹强度 */
    var removeLawPatternIntensity = 0.0 //范围 [0-1]

//endregion

    //region 美型
    /* 变形选择 */
    var faceShape = FaceBeautyShapeEnum.FineDeformation //0 女神，1 网红，2 自然，3 默认，4 精细变形


    /* 变形程度 */
    var faceShapeIntensity = 1.0 ////范围 [0-1]


    /* 瘦脸程度 */
    var cheekThinningIntensity = 0.0 //范围 [0-1]


    /* V脸程度 */
    var cheekVIntensity = 0.0 //范围 [0-1]


    /* 长脸程度 */
    var cheekLongIntensity = 0.0 //范围 [0-1]


    /* 圆脸程度 */
    var cheekCircleIntensity = 0.0 //范围 [0-1]


    /* 窄脸程度 */
    var cheekNarrowIntensity = 0.0 //范围 [0-1]


    /* 窄脸程度 */
    var cheekNarrowIntensityV2 = 0.0 //范围 [0-1]


    /* 短脸程度 */
    var cheekShortIntensity = 0.0 //范围 [0-1]


    /* 小脸程度 */
    var cheekSmallIntensity = 0.0 //范围 [0-1]


    /* 小脸程度 */
    var cheekSmallIntensityV2 = 0.0 //范围 [0-1]


    /* 瘦颧骨 */
    var cheekBonesIntensity = 0.0 //范围 [0-1]


    /* 瘦下颌骨 */
    var lowerJawIntensity = 0.0 //范围 [0-1]


    /* 大眼程度 */
    var eyeEnlargingIntensity = 0.0 //范围 [0-1]


    /* 大眼程度 */
    var eyeEnlargingIntensityV2 = 0.0 //范围 [0-1]


    /* 下巴调整程度 */
    var chinIntensity = 0.5 //范围 [0-1]，0-0.5是变小，0.5-1是变大


    /* 额头调整程度 */
    var forHeadIntensity = 0.5 //范围[0-1]，0-0.5 是变小，0.5-1 是变大


    /* 额头调整程度 */
    var forHeadIntensityV2 = 0.5 //范围[0-1]，0-0.5 是变小，0.5-1 是变大


    /* 瘦鼻程度 */
    var noseIntensity = 0.0 //范围 [0-1]


    /* 瘦鼻程度 */
    var noseIntensityV2 = 0.0 //范围 [0-1]


    /* 嘴巴调整程度 */
    var mouthIntensity = 0.5 //[0-1]，0-0.5是变小，0.5-1是变大


    /* 嘴巴调整程度 */
    var mouthIntensityV2 = 0.5 //[0-1]，0-0.5是变小，0.5-1是变大


    /* 开眼角强度 */
    var canthusIntensity = 0.0 //范围 [0-1]，0.0 到 1.0 变强


    /* 眼睛间距 */
    var eyeSpaceIntensity = 0.5 //范围 [0-1]，[0-1]，0.5-0.0 变长，0.5-1.0 变短


    /* 眼睛角度 */
    var eyeRotateIntensity = 0.5 //范围 [0-1]，0.5-0.0 逆时针旋转，0.5-1.0 顺时针旋转


    /* 鼻子长度 */
    var longNoseIntensity = 0.5 //范围 [0-1]，0.5-0.0 变短，0.5-1.0 变长


    /* 调节人中 */
    var philtrumIntensity = 0.5 //范围[0-1]，0.5-1.0 变长，0.5-0.0 变短


    /* 微笑嘴角强度 */
    var smileIntensity = 0.0 //范围 [0-1]，0.0 到 1.0 变强


    /* 圆眼程度 */
    var eyeCircleIntensity = 0.0 //范围 [0-1]，0.0 到 1.0 变强


    /* 美型的渐变由change_frames参数控制*/
    var changeFramesIntensity = 0.0 //范围 [0-1]，0.0 到 1.0 变强

}