package com.jw.iamstronger.Classes

import java.io.Serializable

class Routine_ALL (
    var id: Int? = null,
    var routine_user: String? = null,
    var routine_name: String? = null,
    var start_date: String? = null,
    var end_date: String? = null,
    var workout: ArrayList<Workout>? = ArrayList(),
    var weekday: String? = null,
    var done: Boolean? = false,
    var completed: Boolean? = false
): Serializable