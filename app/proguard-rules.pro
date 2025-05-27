# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Remove Filament and Sceneform references
# -keep class com.google.android.filament.** { *; }
# -keep class com.google.ar.sceneform.** { *; }
# -keep class com.google.ar.schemas.** { *; }
# -keep class com.google.ar.schemas.lull.** { *; }
# -keep class com.google.ar.schemas.sceneform.** { *; }
# -keep class com.google.flatbuffers.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep JNI methods
-keepclasseswithmembers class * {
    @android.annotation.Keep <methods>;
}

# Keep all model classes
-keep class * extends com.google.ar.sceneform.rendering.ModelRenderable { *; }

# Keep raw types and signatures
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes Exceptions

# JNA Specific Rules
-keep class com.sun.jna.** { *; }
-keep class net.java.dev.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** {
    <fields>;
    <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

# Comment out aggressive optimizations
#-repackageclasses
#-overloadaggressively

# Keep necessary attributes
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Native library loading
-keepclassmembers class com.sun.jna.* { *; }
-keepclassmembers class * implements com.sun.jna.* { *; }

# JNA Rules
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn javax.swing.text.**
-dontwarn com.sun.jna.platform.**
-dontnote com.sun.jna.platform.**

# Keep only core JNA classes
-keep class com.sun.jna.* { *; }
-keepclassmembers class * implements com.sun.jna.* { *; }
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

# Remove any references to platform-specific code
-keep,allowshrinking class com.sun.jna.** { *; }

# Essential JNA rules - minimal set
-keepclassmembers class * extends com.sun.jna.* {
    <fields>;
    <methods>;
}
-keep class com.sun.jna.** { *; }
-keep class * extends com.sun.jna.** { *; }
-keep class org.vosk.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Prevent obfuscation of JNA-used classes
-keep class * implements com.sun.jna.** { *; }

# Don't warn about JNA implementation details
-dontwarn java.awt.**
-dontwarn com.sun.jna.**
-dontwarn org.vosk.**

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Keep generic type information
-keepattributes Signature
-keepclasseswithmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep your ViewModels
-keep class com.maadiran.myvision.presentation.features.**.viewmodels.** { *; }

# Preserve SSL-related classes
-keep class javax.net.ssl.** { *; }
-keep class sun.security.ssl.** { *; }

# Preserve classes involved in certificate handling
-keep class com.maadiran.myvision.core.security.CertificateGenerator { *; }
-keep class com.maadiran.myvision.core.security.utils.CertificateUtils { *; }

# Keep SSLContext and TrustManager related classes
-keep class javax.net.ssl.SSLContext { *; }
-keep class javax.net.ssl.TrustManagerFactory { *; }
-keep class javax.net.ssl.TrustManager { *; }
-keep class javax.net.ssl.X509TrustManager { *; }

# Preserve KeyStore related classes
-keep class java.security.KeyStore { *; }

# Keep all methods that might be used via reflection
-keepclassmembers class * {
    @javax.annotation.SuppressWarnings *;
    @javax.inject.Inject *;
}

# Preserve all annotations
-keepattributes *Annotation*

# Preserve generic type information for Gson
-keepattributes Signature