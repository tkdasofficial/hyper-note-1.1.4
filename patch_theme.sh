#!/bin/bash
sed -i 's/import androidx.core.view.WindowCompat/import androidx.core.view.WindowCompat\nimport com.hyper.note.android.ui.findActivity/g' app/src/main/java/com/hyper/note/android/ui/theme/Theme.kt
sed -i 's/val window = (view.context as Activity).window/val window = view.context.findActivity()?.window/g' app/src/main/java/com/hyper/note/android/ui/theme/Theme.kt
sed -i 's/window.statusBarColor/window?.statusBarColor/g' app/src/main/java/com/hyper/note/android/ui/theme/Theme.kt
sed -i 's/WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = colorScheme == LightColorScheme/window?.let { WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = colorScheme == LightColorScheme }/g' app/src/main/java/com/hyper/note/android/ui/theme/Theme.kt
