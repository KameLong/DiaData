package kamelong.com.tool


import java.io.BufferedReader
import java.io.IOException
import java.io.Reader

/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 * This source code is released under GNU GPL ver3.
 */

/**
 * ShitJISに存在するだめ文字問題(0x5c問題)を回避するためのクラス
 */

class ShiftJISBufferedReader(reader: Reader) : BufferedReader(reader) {
    @Throws(IOException::class)
    override fun readLine(): String? {
        var str: String? = super.readLine()
        if (str == null || !str.contains("\\")) return str
        val dameMoji = arrayOf(
            "\\",
            "―",
            "ソ",
            "Ы",
            "Ⅸ",
            "噂",
            "浬",
            "欺",
            "圭",
            "構.",
            "蚕",
            "十",
            "申",
            "曾",
            "箪",
            "貼",
            "能",
            "表",
            "暴",
            "予",
            "禄",
            "兔",
            "喀",
            "媾",
            "彌",
            "拿",
            "杤",
            "歃",
            "濬",
            "畚",
            "秉",
            "綵",
            "臀",
            "藹",
            "觸",
            "軆",
            "鐔",
            "饅",
            "鷭",
            "偆",
            "砡",
            "纊",
            "犾"
        )
        for (moji in dameMoji) {
            str = str!!.replace(moji + "\\", moji)
        }
        return str

    }
}
