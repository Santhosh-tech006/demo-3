package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val title: String,
    val icon: ImageVector,
    val isPrimaryNav: Boolean = true
) {
    HOME("Home", Icons.Default.Home, true),
    ABOUT("About", Icons.Default.Info, true),
    PROGRAMS("Programs", Icons.Default.School, true),
    TRIAL_BOOKING("Free Trial", Icons.Default.BookmarkBorder, true),
    TOURNAMENTS("Tournaments", Icons.Default.EmojiEvents, true),
    CONTACT("Contact", Icons.Default.Mail, true),
    STUDENT_DASHBOARD("Student Portal", Icons.Default.Person, false),
    ADMIN_DASHBOARD("Admin Desk", Icons.Default.AdminPanelSettings, false)
}
