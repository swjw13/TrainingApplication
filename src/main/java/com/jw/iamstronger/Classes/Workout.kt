package com.jw.iamstronger.Classes

import java.io.Serializable

class Workout(
    var id: Int? = null,
    var workout_name: String? = null,
    var weight: Int? = 0,
    var repetition: Int? = 0,
    var sets: Int? = 0
): Serializable