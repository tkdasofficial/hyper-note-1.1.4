#!/bin/bash
sed -i 's/val biometricPrompt = BiometricPrompt(fragmentActivity!!, executor,/if (fragmentActivity != null) {\n                    val biometricPrompt = BiometricPrompt(fragmentActivity, executor,/g' app/src/main/java/com/hyper/note/android/ui/AppLockScreen.kt
sed -i 's/biometricPrompt.authenticate(promptInfo)/biometricPrompt.authenticate(promptInfo)\n                }/g' app/src/main/java/com/hyper/note/android/ui/AppLockScreen.kt
