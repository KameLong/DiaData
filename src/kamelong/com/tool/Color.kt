package kamelong.com.tool

/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

/**
 * java.awt.ColorはAndroidでは使えない
 * そもそもjava.awt.Colorはあまり好きではないのでColorクラスを自作
 */

class Color : Cloneable {

    private var alpha = 255
    private var red = 0
    private var green = 0
    private var blue = 0
    /**
     * HTML形式の色情報を出力する
     * @return
     */
    val htmlColor: String
        get() {
            var result = "#" + String.format("%02X", red)
            result += String.format("%02X", green)
            result += String.format("%02X", blue)
            return result
        }

    /**
     * Android形式の色情報を出力する
     * @return
     */
    val androidColor: Int
        get() {
            var result = alpha shl 24
            result += red shl 16
            result += green shl 8
            result += blue
            return result
        }

    /**
     * OuDiaの色形式で出力する
     * @return
     */
    val oudiaString: String
        get() {
            var result = "00"
            result += String.format("%02X", blue)
            result += String.format("%02X", green)
            result += String.format("%02X", red)
            return result
        }

    /**
     * デフォルトは黒色を作る
     */
    constructor() {
        alpha = 255
        red = 0
        green = 0
        blue = 0
    }

    /**
     * HTMLの色記述形式からColorを作成する
     * #rgb
     * #rrggbb
     * #aarrggbb
     * の形式に対応
     * @param str
     */
    constructor(str2: String) {
        var str = str2
        if (str.startsWith("#")) {
            str = str.substring(1)
        }
        when (str.length) {
            3 -> {
                red = Integer.parseInt(str.substring(0, 1), 16)
                green = Integer.parseInt(str.substring(1, 2), 16)
                blue = Integer.parseInt(str.substring(2, 3), 16)
            }
            6 -> {
                red = Integer.parseInt(str.substring(0, 2), 16)
                green = Integer.parseInt(str.substring(2, 4), 16)
                blue = Integer.parseInt(str.substring(4, 6), 16)
            }
            8 -> {
                alpha = Integer.parseInt(str.substring(0, 2), 16)
                red = Integer.parseInt(str.substring(2, 4), 16)
                green = Integer.parseInt(str.substring(4, 6), 16)
                blue = Integer.parseInt(str.substring(6, 8), 16)
            }
        }
    }

    /**
     * Androidの32bitカラーより作成
     * @param color
     */
    constructor(color: Int) {
        alpha = color shr 24 and 0xff // or color >>> 24
        red = color shr 16 and 0xff
        green = color shr 8 and 0xff
        blue = color and 0xff
    }

    constructor(alpha: Int, red: Int, green: Int, blue: Int) {
        this.alpha = alpha
        this.red = red
        this.green = green
        this.blue = blue
    }

    /**
     * OuDiaの色形式を入力する
     * @param value
     */
    fun setOuDiaColor(value: String) {
        if (value.length == 8) {
            red = Integer.parseInt(value.substring(6, 8), 16)
            green = Integer.parseInt(value.substring(4, 6), 16)
            blue = Integer.parseInt(value.substring(2, 4), 16)
        }
        if (value.length == 6) {
            red = Integer.parseInt(value.substring(4, 6), 16)
            green = Integer.parseInt(value.substring(2, 4), 16)
            blue = Integer.parseInt(value.substring(0, 2), 16)

        }
    }


    fun getAlpha(): Int {
        return alpha
    }

    fun getRed(): Int {
        return red
    }

    fun getGreen(): Int {
        return green
    }

    fun getBlue(): Int {
        return blue
    }

    fun setAlpha(value: Int) {
        var value = value
        if (value > 255) {
            value = 255
        }
        alpha = value
    }

    fun setRed(value: Int) {
        var value = value
        if (value > 255) {
            value = 255
        }
        red = value
    }

    fun setGreen(value: Int) {
        var value = value
        if (value > 255) {
            value = 255
        }
        green = value
    }

    fun setBlue(value: Int) {
        var value = value
        if (value > 255) {
            value = 255
        }
        blue = value
    }

    public override fun clone(): Color {
        try {
            return super.clone() as Color
        } catch (e: CloneNotSupportedException) {
            SDlog.log(e)
            return Color()
        }

    }

    companion object {
        val BLACK = Color("#000000")
        val WHITE = Color("#FFFFFF")
    }
}
