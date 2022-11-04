package com.example.aure.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class HelperFunctions {
}

/**
 * Turns object (data class) in to Map<String, String>,
 * flattens, and includes/excludes keys/values.
 */
fun <T : Any> toMapAndExclude(
    obj: T,
    include: Map<String, String> = emptyMap(),
    exclude: List<String> = emptyList()
): Map<String, Any?> {
    val mapResult = toMap(obj).toMutableMap()
    val finalMap = hashMapOf<String, Any?>()

    flattenMap(mapResult, finalMap)
    val mutableFinalMap = finalMap.filter { it -> it.key !in exclude }.toMutableMap()
    include.forEach { (key, value) ->
        mutableFinalMap[key] = value
    }
    println("toMapAndExclude -> $mutableFinalMap")
    return mutableFinalMap

}

/**
 * Flattens nested maps.
 * TODO Must be tested for multiple dimentional maps.
 */
fun flattenMap(entry: Map<String, Any?>, result: HashMap<String, Any?>) {
    for (e in entry) {
        if (e.value is Map<*, *>) {
            flattenMap(e.value as Map<String, Any?>, result)
            continue
        }
        result.put(e.key, e.value)
    }
}

/**
 * Turns object (data class) in to Map<String, String>
 */
fun <T : Any> toMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(obj)?.let { value ->
            if (value::class.isData) {
                toMap(value)
            } else {
                value
            }
        }
    }
}