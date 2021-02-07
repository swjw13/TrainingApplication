package com.jw.iamstronger.Classes

import java.io.Serializable

class Workout_post (
    var workout_name: String? = null,
    var weight: Int? = 0,
    var repitition: Int? = 0,
    var sets: Int? = 0
): Serializable