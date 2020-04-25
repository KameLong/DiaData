package kamelong.com.tool

/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Logを出力させるためのクラス
 */

object SDlog {
    private val able = false
//    private var activity: Activity? = null
//    private var handler: Handler? = null
    val nowDate: String
        get() {
            val df = SimpleDateFormat("yyyyMMddHHmmss")
            val date = Date(System.currentTimeMillis())
            return df.format(date)
        }


//    fun setActivity(a: Activity) {
//        activity = a
//        handler = Handler()
//    }

    fun toast(string: String) {
//        if (activity != null) {
//            handler!!.post(Runnable { Toast.makeText(activity, string, Toast.LENGTH_SHORT).show() })
//        }
    }

    fun log(e: Exception) {
//        try {
//            val pref = PreferenceManager.getDefaultSharedPreferences(activity)
//            if (pref.getString("userID", "").length() === 0) {
//                pref.edit().putString("userID", UUID.randomUUID().toString()).apply()
//            }
//            if (pref.getBoolean("send_log", false)) {
//                if (pref.getString("userID", "").length() === 0) {
//                    pref.edit().putString("userID", UUID.randomUUID().toString()).apply()
//                }
//                val packageInfo = activity!!.getPackageManager().getPackageInfo(activity!!.getPackageName(), 0)
//                val logName =
//                    activity!!.getCacheDir() + "/" + nowDate + "_" + packageInfo.versionName + "_" + pref.getString(
//                        "userID",
//                        ""
//                    ) + ".log"
//
//
//                Thread(Runnable {
//                    try {
//                        val pw = PrintWriter(logName)
//                        pw.println(packageInfo.versionName)
//                        e.printStackTrace(pw)
//                        pw.close()
//                        Send(logName)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }).start()
//            }
//
//        } catch (e2: Exception) {
//            e.printStackTrace()
//        }
//
        e.printStackTrace()
    }

    fun log(value: Any) {
        println(value)
    }

    fun log(value1: Any, value2: Any) {
        println("$value1,$value2")
    }


    fun Send(filename: String): Int {

        return 0
    }
}