#!/bin/bash
sed -i '/implementation(libs.androidx.biometric)/a \ \ implementation("androidx.concurrent:concurrent-futures:1.2.0")' app/build.gradle.kts
