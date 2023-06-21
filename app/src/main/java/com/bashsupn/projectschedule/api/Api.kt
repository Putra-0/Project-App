package com.bashsupn.projectschedule.api

import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.LoginResponse
import com.bashsupn.projectschedule.models.ProjectResponse
import com.bashsupn.projectschedule.models.ProjectsResponse
import com.bashsupn.projectschedule.models.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("users")
    fun getUsers(
    ): Call<ArrayList<UsersResponse>>

    @FormUrlEncoded
    @POST("users")
    fun addUser(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<FormResponse>

    @FormUrlEncoded
    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<FormResponse>

    @DELETE("users/{id}")
    fun deleteUser(
        @Path("id") id: Int
    ): Call<FormResponse>

    @GET("jobs")
    fun getProjects(
    ): Call<ProjectsResponse>

    @FormUrlEncoded
    @POST("jobs")
    fun addProject(
        @Field("user_id") user_id: Int,
        @Field("name") name: String,
        @Field("client_name") client_name: String,
        @Field("address") address: String,
        @Field("start_date") start_date: String?,
    ): Call<FormResponse>

    @FormUrlEncoded
    @PUT("jobs/{id}")
    fun updateProject(
        @Path("id") id: Int,
        @Field("user_id") user_id: Int,
        @Field("name") name: String,
        @Field("client_name") client_name: String,
        @Field("address") address: String,
        @Field("status") status: String,
        @Field("start_date") start_date: String?,
    ): Call<FormResponse>

    @GET("jobs/{id}")
    fun getProject(
        @Path("id") id: Int
    ): Call<ProjectResponse>

    @DELETE("jobs/{id}")
    fun deleteProject(
        @Path("id") id: Int
    ): Call<FormResponse>

    @FormUrlEncoded
    @POST("task")
    fun addTask(
        @Field("job_id") job_id: Int,
        @Field("name") name: String,
        @Field("duration") duration: Int?,
        @Field("dependencies") dependencies: String?,
    ): Call<FormResponse>
}