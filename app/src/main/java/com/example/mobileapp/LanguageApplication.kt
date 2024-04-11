package com.example.mobileapp

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

class LanguageApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        supabaseClient = createSupabaseClient(
            supabaseUrl = "https://buadpnhqhvnpmlvoakff.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ1YWRwbmhxaHZucG1sdm9ha2ZmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTI4Mzk1ODIsImV4cCI6MjAyODQxNTU4Mn0.m0BHzGJZ98QIr-1yKRDzQtEgJu1ISnjMiHGZj1GbYIw"
        ) {
            defaultSerializer = KotlinXSerializer(Json)

            install(Auth)
            install(Postgrest)
        }
    }

    companion object {
        lateinit var supabaseClient: SupabaseClient
    }
}