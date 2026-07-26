# Smylo release R8 / ProGuard rules
# Without these, Retrofit suspend APIs + Gson DTOs fail only in minify/release
# (Play Store builds), surfacing as "Something went wrong. Please try again."

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepattributes Exceptions

# Kotlin metadata (Gson / reflection helpers may need it)
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# ---------------------------------------------------------------------------
# Retrofit (R8 full mode) — required for suspend functions
# https://github.com/square/retrofit/blob/trunk/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro
# ---------------------------------------------------------------------------
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

-keep,allowobfuscation,allowshrinking class retrofit2.CallAdapter
-keep,allowobfuscation,allowshrinking class retrofit2.Converter

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep our API interfaces fully (path annotations + suspend signatures)
-keep interface com.smylo.core.network.api.** { *; }

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# ---------------------------------------------------------------------------
# Gson / API DTOs (reflection-based serialization)
# Keep class names, fields, AND constructors — Gson needs them at runtime.
# ---------------------------------------------------------------------------
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class com.smylo.core.network.dto.** { *; }
-keepclassmembers class com.smylo.core.network.dto.** {
    <init>(...);
    <fields>;
}

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ---------------------------------------------------------------------------
# Room
# ---------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ---------------------------------------------------------------------------
# OkHttp
# ---------------------------------------------------------------------------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ---------------------------------------------------------------------------
# Enums used in serialized payloads
# ---------------------------------------------------------------------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
