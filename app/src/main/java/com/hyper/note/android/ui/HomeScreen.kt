package com.hyper.note.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyper.note.android.data.Note
import com.hyper.note.android.data.UserPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    notes: List<Note>,
    userPreferences: UserPreferences,
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onEraseAll: () -> Unit
) {
    var currentTab by remember { mutableStateOf("Notes") }
    val filters = listOf("All Notes", "Pinned", "Encrypted", "Voice")
    var selectedFilter by remember { mutableStateOf(filters[0]) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (currentTab == "Notes" || currentTab == "Vault") {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Search notes...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            IconButton(onClick = { 
                                isSearchActive = false 
                                searchQuery = ""
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Search", tint = MaterialTheme.colorScheme.onSurface)
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val userName by userPreferences.name.collectAsState()
                                    val firstChar = userName.firstOrNull()?.uppercase() ?: "U"
                                    Text(
                                        firstChar,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                                Column {
                                    val userName by userPreferences.name.collectAsState()
                                    val firstName = userName.split(" ").firstOrNull() ?: "User"
                                    Text(
                                        text = "Hi, $firstName",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Welcome back!",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                            if (currentTab == "Notes" || currentTab == "Vault") {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(onClick = { isSearchActive = true }) {
                                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                    IconButton(onClick = { currentTab = "Setup" }) {
                                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }

                    if (currentTab == "Notes" || currentTab == "Vault") {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filters) { filter ->
                                FilterChip(
                                    selected = filter == selectedFilter,
                                    onClick = { selectedFilter = filter },
                                    label = { Text(filter) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = filter == selectedFilter,
                                        borderColor = Color.Transparent,
                                        selectedBorderColor = Color.Transparent
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentTab == "Notes" || currentTab == "Vault") {
                FloatingActionButton(
                    onClick = onAddNote,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.padding(bottom = 80.dp) // padding for bottom nav
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        },
        bottomBar = {
            BottomNavBar(currentTab) { currentTab = it }
        }
    ) { paddingValues ->
        when (currentTab) {
            "Notes" -> {
                NotesContent(
                    notes = notes,
                    selectedFilter = selectedFilter,
                    searchQuery = searchQuery,
                    onNoteClick = onNoteClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            "Voice" -> {
                VoiceContent(modifier = Modifier.padding(paddingValues), onAddNote = onAddNote)
            }
            "Vault" -> {
                VaultContent(
                    notes = notes,
                    searchQuery = searchQuery,
                    selectedFilter = selectedFilter,
                    userPreferences = userPreferences,
                    onNoteClick = onNoteClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            "Setup" -> {
                SetupContent(modifier = Modifier.padding(paddingValues), userPreferences = userPreferences, onEraseAll = onEraseAll)
            }
        }
    }
}

@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title.ifEmpty { "Untitled" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (note.isEncrypted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "AES-256",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Encrypted",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(note.timestamp)),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        if (note.isVoiceNote) "VOICE" else "SECURE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(currentTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        modifier = Modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Description, contentDescription = "Notes") },
            label = { Text("Notes", fontSize = 11.sp) },
            selected = currentTab == "Notes",
            onClick = { onTabSelected("Notes") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Mic, contentDescription = "Voice") },
            label = { Text("Voice", fontSize = 11.sp) },
            selected = currentTab == "Voice",
            onClick = { onTabSelected("Voice") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Shield, contentDescription = "Vault") },
            label = { Text("Vault", fontSize = 11.sp) },
            selected = currentTab == "Vault",
            onClick = { onTabSelected("Vault") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun NotesContent(
    notes: List<Note>,
    selectedFilter: String,
    searchQuery: String = "",
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val filteredNotes = when (selectedFilter) {
            "Pinned" -> notes.filter { it.isPinned }
            "Voice" -> notes.filter { it.isVoiceNote }
            "Encrypted" -> notes.filter { it.isEncrypted }
            else -> notes
        }.filter {
            searchQuery.isEmpty() || 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.content.contains(searchQuery, ignoreCase = true)
        }

        if (filteredNotes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                Text("No notes found", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        filteredNotes.forEach { note ->
            NoteCard(note = note, onClick = { onNoteClick(note) })
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun VoiceContent(modifier: Modifier = Modifier, onAddNote: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .clickable { onAddNote() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Record", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Tap to start Voice Typing", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Text("Powered by offline NLP", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun VaultContent(
    notes: List<Note>,
    searchQuery: String,
    selectedFilter: String,
    userPreferences: com.hyper.note.android.data.UserPreferences,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var unlocked by remember { mutableStateOf(false) }
    val encryptionKey by userPreferences.encryptionKey.collectAsState()
    val enableBiometrics by userPreferences.enableBiometrics.collectAsState()
    
    if (!unlocked) {
        AppLockScreen(
            correctPin = encryptionKey,
            enableBiometrics = enableBiometrics,
            onUnlocked = { unlocked = true },
            modifier = modifier
        )
    } else {
        NotesContent(notes = notes.filter { it.isEncrypted }, selectedFilter = "Encrypted", searchQuery = searchQuery, onNoteClick = onNoteClick, modifier = modifier)
    }
}

@Composable
fun SetupContent(modifier: Modifier = Modifier, userPreferences: UserPreferences, onEraseAll: () -> Unit) {
    val name by userPreferences.name.collectAsState()
    val dob by userPreferences.dob.collectAsState()
    val address by userPreferences.address.collectAsState()
    val encryptionKey by userPreferences.encryptionKey.collectAsState()
    val theme by userPreferences.theme.collectAsState()
    val enableBiometrics by userPreferences.enableBiometrics.collectAsState()
    val autoLockTimeout by userPreferences.autoLockTimeout.collectAsState()
    val fontSize by userPreferences.fontSize.collectAsState()
    val analyticsEnabled by userPreferences.analyticsEnabled.collectAsState()
    
    var editName by remember { mutableStateOf(name) }
    var editDob by remember { mutableStateOf(dob) }
    var editAddress by remember { mutableStateOf(address) }

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("Settings & Profile", color = MaterialTheme.colorScheme.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp, top = 24.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Personal Details", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                OutlinedTextField(value = editName, onValueChange = { editName = it; userPreferences.saveName(it) }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = editDob, onValueChange = { editDob = it; userPreferences.saveDob(it) }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = editAddress, onValueChange = { editAddress = it; userPreferences.saveAddress(it) }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Security & Privacy", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("AES-256 GCM is active. Your keys are secured in the Android Keystore.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                var showPinChangeDialog by remember { mutableStateOf(false) }
                var oldPinInput by remember { mutableStateOf("") }
                var newPinInput by remember { mutableStateOf("") }
                var pinChangeError by remember { mutableStateOf("") }

                Button(
                    onClick = { 
                        oldPinInput = ""
                        newPinInput = ""
                        pinChangeError = ""
                        showPinChangeDialog = true 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Text("Change App PIN")
                }

                if (showPinChangeDialog) {
                    AlertDialog(
                        onDismissRequest = { showPinChangeDialog = false },
                        title = { Text("Secure PIN Change") },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Please enter your current PIN to set a new one.", fontSize = 14.sp)
                                OutlinedTextField(
                                    value = oldPinInput,
                                    onValueChange = { oldPinInput = it },
                                    label = { Text("Current PIN") },
                                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = newPinInput,
                                    onValueChange = { newPinInput = it },
                                    label = { Text("New PIN (4-16 digits)") },
                                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                if (pinChangeError.isNotEmpty()) {
                                    Text(pinChangeError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (oldPinInput == encryptionKey) {
                                        if (newPinInput.length in 4..16) {
                                            userPreferences.saveEncryptionKey(newPinInput)
                                            showPinChangeDialog = false
                                        } else {
                                            pinChangeError = "New PIN must be 4-16 digits."
                                        }
                                    } else {
                                        pinChangeError = "Current PIN is incorrect."
                                    }
                                }
                            ) {
                                Text("Update")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showPinChangeDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Enable Biometric Unlock", color = MaterialTheme.colorScheme.onSurface)
                    androidx.compose.material3.Switch(checked = enableBiometrics, onCheckedChange = { userPreferences.saveEnableBiometrics(it) })
                }
                
                Text("Auto-Lock Timeout", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val timeouts = listOf("Immediate", "1 Minute", "5 Minutes", "Never")
                    items(timeouts.size) { index ->
                        val t = timeouts[index]
                        FilterChip(
                            selected = autoLockTimeout == t,
                            onClick = { userPreferences.saveAutoLockTimeout(t) },
                            label = { Text(t) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Appearance & Features", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("App Theme", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val themes = listOf("Dark", "Light", "System")
                    items(themes.size) { index ->
                        val t = themes[index]
                        FilterChip(
                            selected = theme == t,
                            onClick = { userPreferences.saveTheme(t) },
                            label = { Text(t) }
                        )
                    }
                }

                Text("Reading Font Size", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sizes = listOf("Small", "Medium", "Large")
                    items(sizes.size) { index ->
                        val s = sizes[index]
                        FilterChip(
                            selected = fontSize == s,
                            onClick = { userPreferences.saveFontSize(s) },
                            label = { Text(s) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Advanced Data Management", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Share Crash Analytics", color = MaterialTheme.colorScheme.onSurface)
                    androidx.compose.material3.Switch(checked = analyticsEnabled, onCheckedChange = { userPreferences.saveAnalyticsEnabled(it) })
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
                        Text("Export Backup")
                    }
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)) {
                        Text("Import Backup")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onEraseAll, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)) {
            Text("Erase All Local Data")
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
