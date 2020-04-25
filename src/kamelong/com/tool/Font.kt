package kamelong.com.tool

/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

/**
 * フォントスタイル情報を管理する
 */
class Font : Cloneable {
    /**
     * フォント高さ
     */
    var height = -1
    /**
     * フォント名
     */
    var name: String? = null
    /**
     * 太字なら１
     */
    var bold = false
    /**
     * 斜体なら１
     */
    var itaric = false
    val ouDiaString: String
        get() {
            val result = StringBuilder()
            result.append("PointTextHeight=").append(height)
            if (name != null) {
                result.append(";Facename=").append(name)
            } else {
                result.append(";Facename=").append("ＭＳ ゴシック")
            }
            if (bold) {
                result.append(";Bold=1")
            }
            if (itaric) {
                result.append("Itaric=1")
            }
            return result.toString()
        }

    constructor() {

    }

    constructor(value: String) {
        val valueList = value.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (s in valueList) {
            val t = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val v = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            when (t) {
                "PointTextHeight" -> height = Integer.parseInt(v)
                "Facename" -> name = v
                "Bold" -> bold = v == "1"
                "Itaric" -> itaric = v == "1"
            }
        }
    }

    private constructor(name: String, height: Int, bold: Boolean, itaric: Boolean) {
        this.name = name
        this.height = height
        this.bold = bold
        this.itaric = itaric
    }

    public override fun clone(): Font {
        try {
            return super.clone() as Font
        } catch (e: CloneNotSupportedException) {
            SDlog.log(e)
            return Font()
        }

    }

    companion object {
        val OUDIA_DEFAULT = Font("ＭＳ ゴシック", 9, false, false)

        private val HEIGHT = "height"
        private val NAME = "facename"
        private val BOLD = "bold"
        private val ITARIC = "itaric"
    }


}
