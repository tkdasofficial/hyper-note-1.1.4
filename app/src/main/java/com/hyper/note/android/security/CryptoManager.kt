package com.hyper.note.android.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64

class CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getSecretKey(): SecretKey {
        return try {
            val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
            existingKey?.secretKey ?: createSecretKey()
        } catch (e: Exception) {
            try { keyStore.deleteEntry("secret") } catch (e2: Exception) {}
            createSecretKey()
        }
    }

    private fun createSecretKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(bytes: ByteArray): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encrypted = cipher.doFinal(bytes)
            val combined = iv + encrypted
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            ""
        }
    }

    fun decrypt(data: String): ByteArray {
        val combined = Base64.decode(data, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, 12)
        val encrypted = combined.copyOfRange(12, combined.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return cipher.doFinal(encrypted)
    }

    fun encryptString(text: String): String {
        if (text.isEmpty()) return ""
        return encrypt(text.toByteArray(Charsets.UTF_8))
    }

    fun decryptString(data: String): String {
        if (data.isEmpty()) return ""
        return try {
            String(decrypt(data), Charsets.UTF_8)
        } catch (e: Exception) {
            data // Return raw if decryption fails (e.g. unencrypted legacy note)
        }
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
