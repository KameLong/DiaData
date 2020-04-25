package kamelong.com.tool


import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.HashMap


/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

/**
 * Csvファイルを読み込んで必要な情報を出力させるクラス
 */
class LoadCsv(inputStream: InputStream) {
    internal var data = ArrayList<Array<String>>()
    internal var headerMap: MutableMap<String, Int> = HashMap()

    init {
        try {
            val br = BufferedReader(InputStreamReader(inputStream))
            val header = br.readLine().split(",".toRegex()).toTypedArray()
            if (header[0].toByteArray()[0] == 0xEF.toByte() && header[0].toByteArray()[1] == 0xBB.toByte() && header[0].toByteArray()[2] == 0xBF.toByte()) {
                val s = StringBuilder()
                for (i in 3 until header[0].toByteArray().size) {
                    s.append(header[0].toByteArray()[i].toChar())
                }
                header[0] = s.toString()
            }
            for (i in header.indices) {
                headerMap[header[i]] = i
            }
            var str: String? = br.readLine()
            while (str != null) {
                data.add(str.split(",".toRegex()).toTypedArray())
                str = br.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getData(key: String, index: Int): String {
        try {
            if (index < data.size && key in headerMap) {
                return data[index][headerMap[key]!!]
            }
        } catch (e: Exception) {
        }

        return ""
    }

    fun dataNum(): Int {
        return data.size
    }
}
