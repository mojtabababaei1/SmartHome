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

-keepattributes Signature
-keepattributes EnclosingMethod

# Keep critical classes (example)
-keep class com.maadiran.myvision.data.** { *; }

-dontusemixedcaseclassnames
-dontpreverify
-dontnote kotlin.reflect.**

# Remove line number info for extra obfuscation
-renamesourcefileattribute SourceFile
-keepattributes LineNumberTable

-repackageclasses
-allowaccessmodification
-optimizationpasses 5
-keepattributes *Annotation*

# Additional security rules
-keep class com.maadiran.myvision.data.proto.** { *; }  # Keep proto files
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { *; }

# Aggressive obfuscation
-repackageclasses 'com.maadiran.myvision.data'
-allowaccessmodification
-optimizationpasses 7
-overloadaggressively
#-flattenpackagehierarchy  # Comment out this line
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Remove debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-adaptresourcefilenames    **.properties,**.xml,**.png,**.jpg,**.gif
-adaptresourcefilecontents **.properties,**.xml

# Encryption related
-keep class javax.crypto.** { *; }
-keep class javax.crypto.spec.** { *; }
-keep class java.security.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Additional security measures
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.slf4j.**

# Remove all JNA-related rules
# -keep class com.sun.jna.** { *; }
# -keepclassmembers class * extends com.sun.jna.** { *; }

# StringConcatFactory workaround
-keep class java.lang.invoke.StringConcatFactory {
    public static java.lang.invoke.CallSite makeConcat(java.lang.invoke.MethodHandles$Lookup,
        java.lang.String, java.lang.invoke.MethodType, java.lang.String, java.lang.Object[]);
}

# Keep JNA native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Remove conflicting rules
-keep class java.lang.invoke.StringConcatFactory {*;}
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn java.awt.**
-keep class java.awt.** { *; }

# Keep all proto generated classes
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# Keep all kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Additional keep rules for R8
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep class androidx.** { *; }
-keep class com.google.** { *; }

# Keep Vosk-related classes
-keep class org.vosk.** { *; }
-keep class com.alphacephei.vosk.** { *; }

# SSL/Security specific rules
-keepclassmembers class com.android.org.conscrypt.** { *; }
-keep class com.android.org.conscrypt.** { *; }
-keepclassmembers class javax.net.ssl.** { *; }
-keep class javax.net.ssl.** { *; }
-keepclassmembers class javax.crypto.** { *; }
-keep class javax.crypto.** { *; }
-keepclassmembers class java.security.** { *; }
-keep class java.security.** { *; }
-keepclassmembers class sun.security.** { *; }
-keep class sun.security.** { *; }
-keep class org.bouncycastle.** { *; }

# Keep SSL related attributes
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Exceptions

# Add specific rules for SSL socket factories
-keep class * extends javax.net.ssl.SSLSocketFactory { *; }
-keep class * implements javax.net.ssl.X509TrustManager { *; }

# Additional SSL security rules
-keepclassmembers class javax.net.ssl.** {
    *;
}
-keep class javax.net.ssl.** {
    *;
}
-keepclassmembers class javax.security.cert.** {
    *;
}
-keep class javax.security.cert.** {
    *;
}
-keepclassmembers class java.security.cert.** {
    *;
}
-keep class java.security.cert.** {
    *;
}
-keep class sun.security.** {
    *;
}
-keep class com.android.org.conscrypt.** {
    *;
}
-keepclassmembers class com.android.org.conscrypt.** {
    *;
}

# Preserve all native method names and the names of their classes
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep all SSL related classes
-keep class * extends javax.net.ssl.SSLSocketFactory
-keep class * extends javax.net.ssl.TrustManagerFactory
-keep class * extends javax.net.ssl.X509TrustManager

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep generic signatures and R8 specific rules
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Exceptions
-keepattributes *Annotation*

# Keep any classes using TypeToken
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Keep TypeToken itself
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Keep generic type information
-keepattributes Signature
-keepclasseswithmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Additional R8 specific rules for generics
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Keep your model classes
-keep class com.maadiran.myvision.domain.model.** { *; }
-keep class com.maadiran.myvision.data.model.** { *; }
-keep class com.maadiran.myvision.presentation.model.** { *; }

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