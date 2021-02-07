package com.jw.iamstronger

import com.jw.iamstronger.Classes.*
import retrofit2.Call
import retrofit2.http.*

interface retrofitTest {

    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("id") id: String,
        @Field("password") password: String
    ): Call<getToken>


    @FormUrlEncoded
    @POST("signup/")
    fun signin(
        @Field("id") id2: String,
        @Field("password") password2: String,
        @Field("nickname") nickname2: String
    ): Call<getToken>


    @GET("routines/all")
    fun getAllRoutines(): Call<ArrayList<Routine_ALL>>


    @POST("routines/all")
    fun postRoutines(
        @Body routine: Routine
    ): Call<Routine_ALL>


    @GET("daily-routines/{day}")
    fun getDailyRoutine(
        @Path("day") day: String
    ): Call<ArrayList<Routine_ALL>>


    @GET("routines/{routine_id}")
    fun getRoutineDetail(
        @Path("routine_id") id: Int
    ): Call<Routine_ALL>


    @FormUrlEncoded
    @PUT("routines/{routine_id}")
    fun putRoutineDetail(
        @Path("routine_id") id: Int,
        @FieldMap options: Map<String, String>
    ): Call<Routine_ALL>


    @DELETE("routines/{routine_id}")
    fun DelRoutineDetail(
        @Path("routine_id") id: Int
    ): Call<Void>


    @GET("routines/{routine_id}/workouts")
    fun getWorkoutList(
        @Path("routine_id") id: Int
    ): Call<ArrayList<Workout>>


    @POST("routines/{routine_id}/workouts")
    fun postWorkoutList(
        @Path("routine_id") id: Int,
        @Body workout: Workout_post
    ): Call<Workout>


    @GET("routines/{routine_id}/workouts/{workout_id}")
    fun getWorkoutDetail(
        @Path("routine_id") id1: Int,
        @Path("workout_id") id2: Int
    ): Call<Workout>


    @FormUrlEncoded
    @PUT("routines/{routine_id}/workouts/{workout_id}")
    fun putWorkoutDetail(
        @Path("routine_id") id1: Int,
        @Path("workout_id") id2: Int,
        @FieldMap options: Map<String, String>
    ): Call<Workout>


    @DELETE("routines/{routine_id}/workouts/{workout_id}")
    fun delWorkoutDetail(
        @Path("routine_id") id1: Int,
        @Path("workout_id") id2: Int
    ): Call<Void>

    @GET("newday")
    fun getDaystart(): Call<Void>


    @GET("completed-routines")
    fun getCompletedRoutines(): Call<ArrayList<Routine_ALL>>
}