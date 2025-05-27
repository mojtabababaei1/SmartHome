package com.maadiran.myvision.core.security.utils

import android.util.Log
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

object CertificateUtils {
    fun loadPrivateKey(pemKey: String): PrivateKey {
        try {
            // Clean the PEM content properly
            val cleanedKey = pemKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .trim()

            Log.d("CertificateUtils", "Cleaned key length: ${cleanedKey.length}")

            val keyBytes = try {
                Base64.getDecoder().decode(cleanedKey)
            } catch (e: IllegalArgumentException) {
                Log.e("CertificateUtils", "Failed to decode key: ${e.message}")
                Log.d("CertificateUtils", "Key content: $cleanedKey")
                throw IllegalArgumentException("Invalid key format: ${e.message}")
            }

            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
        } catch (e: Exception) {
            Log.e("CertificateUtils", "Error loading private key", e)
            throw e
        }
    }

    fun loadCertificate(pemCert: String): X509Certificate {
        try {
            // Clean the PEM content properly
            val cleanedCert = pemCert
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .trim()

            val certBytes = try {
                Base64.getDecoder().decode(cleanedCert)
            } catch (e: IllegalArgumentException) {
                Log.e("CertificateUtils", "Failed to decode certificate: ${e.message}")
                Log.d("CertificateUtils", "Certificate content: $cleanedCert")
                throw IllegalArgumentException("Invalid certificate format: ${e.message}")
            }

            val cf = CertificateFactory.getInstance("X.509")
            return cf.generateCertificate(ByteArrayInputStream(certBytes)) as X509Certificate
        } catch (e: Exception) {
            Log.e("CertificateUtils", "Error loading certificate", e)
            throw e
        }
    }
}
