-dontwarn com.maadiran.myvision.data.**
-keep class com.maadiran.myvision.data.** { *; }

-keep class com.maadiran.myvision.data.devices.tv.** {
    private *; 
    protected *;
    public <methods>;
}

# Protect sensitive classes
-keepnames class com.maadiran.myvision.data.devices.tv.remote.RemoteManager
-keepnames class com.maadiran.myvision.data.devices.tv.pairing.PairingManager

# Additional security
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# JNA Rules
-keep class com.sun.jna.** { *; }
-keep class net.java.dev.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** {
    <fields>;
    <methods>;
}
-dontwarn java.awt.**
-dontwarn org.apache.commons.io.**