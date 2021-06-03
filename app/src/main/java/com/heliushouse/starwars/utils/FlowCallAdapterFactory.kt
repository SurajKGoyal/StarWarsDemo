package com.heliushouse.starwars.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        fun create() = FlowCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }
        check(returnType is ParameterizedType) {
            "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>"
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == Response::class.java) {
            check(responseType is ParameterizedType) {
                "Response must be parameterized as Response<Foo> or Response<out Foo>"
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T : Any>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<T> = flow { emit(call.await()) }
    }

    private class ResponseCallAdapter<T : Any>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<Response<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<Response<T>> = flow { emit(call.awaitResponse()) }
    }
}