package com.maadiran.myvision.core.security

import android.util.Log
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.Base64
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import java.io.StringWriter
import java.math.BigInteger
import java.security.*
import java.security.cert.X509Certificate
import java.util.*

object CertificateGenerator {

    fun generateFull(
        name: String,
        country: String,
        state: String,
        locality: String,
        organisation: String,
        OU: String
    ): Map<String, String> {
        try {
            // Generate key pair
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            val keyPair = keyPairGenerator.generateKeyPair()

            // Generate certificate
            val cert = generateCertificate(keyPair, name, country, state, locality, organisation, OU)

            // Format private key PEM
            val privateKeyPem = StringBuilder()
                .append("-----BEGIN PRIVATE KEY-----\n")
                .append(Base64.getEncoder().encodeToString(keyPair.private.encoded)
                    .replace("(.{64})".toRegex(), "$1\n"))
                .append("\n-----END PRIVATE KEY-----")
                .toString()

            // Format certificate PEM
            val certificatePem = StringBuilder()
                .append("-----BEGIN CERTIFICATE-----\n")
                .append(Base64.getEncoder().encodeToString(cert.encoded)
                    .replace("(.{64})".toRegex(), "$1\n"))
                .append("\n-----END CERTIFICATE-----")
                .toString()

            Log.d("CertificateGenerator", "Generated key length: ${privateKeyPem.length}")
            Log.d("CertificateGenerator", "Generated cert length: ${certificatePem.length}")

            return mapOf(
                "key" to privateKeyPem,
                "cert" to certificatePem
            )
        } catch (e: Exception) {
            Log.e("CertificateGenerator", "Error generating certificate", e)
            throw e
        }
    }

    private fun generateCertificate(
        keyPair: KeyPair,
        name: String,
        country: String,
        state: String,
        locality: String,
        organisation: String,
        OU: String
    ): X509Certificate {
        val startDate = Date()
        val endDate = Date(System.currentTimeMillis() + 100L * 365 * 24 * 60 * 60 * 1000) // 100 years

        val dnName = X500Name("CN=$name, C=$country, ST=$state, L=$locality, O=$organisation, OU=$OU")

        val certSerialNumber = BigInteger.valueOf(System.currentTimeMillis())

        val subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded)

        val certBuilder = X509v3CertificateBuilder(
            dnName,
            certSerialNumber,
            startDate,
            endDate,
            dnName,
            subjectPublicKeyInfo
        )

        val contentSigner: ContentSigner = JcaContentSignerBuilder("SHA256withRSA")
            .build(keyPair.private)

        val certificateHolder = certBuilder.build(contentSigner)
        return JcaX509CertificateConverter().getCertificate(certificateHolder)
    }
}
