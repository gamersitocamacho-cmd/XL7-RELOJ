package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sin

object SoundSynthesizer {
    private const val SAMPLE_RATE = 22050

    suspend fun playBeep(frequencyHz: Double, durationMs: Int) = withContext(Dispatchers.Default) {
        try {
            val numSamples = (durationMs * SAMPLE_RATE / 1000)
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val sample = sin(2.0 * Math.PI * i / (SAMPLE_RATE / frequencyHz))
                buffer[i] = (sample * 25000).toInt().toShort() // Slightly padded maximum volume for comfort
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            delay(durationMs.toLong() + 20)
            audioTrack.stop()
            audioTrack.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun playChirp(startFreq: Double, endFreq: Double, durationMs: Int) = withContext(Dispatchers.Default) {
        try {
            val numSamples = (durationMs * SAMPLE_RATE / 1000)
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val tProgress = i.toDouble() / numSamples
                val currentFreq = startFreq + (endFreq - startFreq) * tProgress
                val phase = 2.0 * Math.PI * currentFreq * (i.toDouble() / SAMPLE_RATE)
                val sample = sin(phase)
                buffer[i] = (sample * 25000).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            delay(durationMs.toLong() + 20)
            audioTrack.stop()
            audioTrack.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun playSciFiPulse() {
        playChirp(350.0, 1000.0, 180) // Rising sweep
        delay(60)
        playChirp(1000.0, 500.0, 120)  // Slight dip sweep
        delay(60)
        playBeep(1600.0, 100)          // Confirm pulse
    }

    suspend fun playTick() {
        playBeep(2200.0, 6) // Extremely fast mechanical key click
    }

    suspend fun playConfirm() {
        playBeep(1100.0, 40)
        delay(30)
        playBeep(1650.0, 80)
    }

    suspend fun playError() {
        playBeep(140.0, 300)
    }
}
