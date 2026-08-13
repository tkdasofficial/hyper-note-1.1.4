#!/bin/bash
sed -i 's/LaunchedEffect(Unit) {/LaunchedEffect(Unit) {\n        kotlinx.coroutines.delay(500)/g' app/src/main/java/com/hyper/note/android/ui/AppLockScreen.kt
