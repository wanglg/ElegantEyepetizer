package com.android.leo.base.utils

import com.google.gson.*
import java.lang.reflect.Type

/**
 * gson
 * Created by 1 on 2017/9/28.
 */

class GsonUtils {


    /**
     * 实现了 序列化 接口    对为null的字段进行转换
     */
    class StringConverter : JsonSerializer<String>, JsonDeserializer<String> {
        //字符串为null 转换成"",否则为字符串类型
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): String {
            return json.asJsonPrimitive.asString
        }

        override fun serialize(src: String?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
                if (src == null || src == "null") JsonPrimitive("") else JsonPrimitive(src.toString())
    }

}
