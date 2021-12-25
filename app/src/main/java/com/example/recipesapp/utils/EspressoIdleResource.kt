package com.example.recipesapp.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdleResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdleResource = CountingIdlingResource(RESOURCE)

    fun increment(){
        countingIdleResource.increment()
    }

    fun decrement(){
        if(!countingIdleResource.isIdleNow){
            countingIdleResource.decrement()
        }
    }

}