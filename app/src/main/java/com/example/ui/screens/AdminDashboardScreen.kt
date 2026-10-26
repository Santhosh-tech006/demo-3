package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TurnedIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.SiteSettingsEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TournamentEntity
import com.example.data.local.TrialBookingEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.BgSecondary
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CardBgElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LightBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun AdminDashboardScreen(
    students: List<StudentEntity>,
    courses: List<CourseEntity>,
    bookings: List<TrialBookingEntity>,
    enquiries: List<EnquiryEntity>,
    tournaments: List<TournamentEntity>,
    settings: SiteSettingsEntity?,
    onUpdateBookingStatus: (TrialBookingEntity, String) -> Unit,
    onDeleteBooking: (TrialBookingEntity) -> Unit,
    onUpdateEnquiryStatus: (EnquiryEntity, String, String?) -> Unit,
    onDeleteEnquiry: (EnquiryEntity) -> Unit,
    onSaveCourse: (CourseEntity) -> Unit,
    onDeleteCourse: (CourseEntity) -> Unit,
    onSaveTournament: (TournamentEntity) -> Unit,
    onSaveSettings: (SiteSettingsEntity) -> Unit
) {
    var selectedModuleIndex by remember { mutableIntStateOf(0) }
    val moduleTitles = listOf("Overview", "Trial Bookings", "Enquiries", "Courses", "Students", "Site Settings")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgPrimary)
            .padding(16.dp)
            .testTag("admin_dashboard_scroll")
    ) {
        // Admin Header
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin",
                        tint = ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACADEMY COMMAND CENTER",
                        color = ElectricBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Grandmaster Admin Dashboard",
                    color = TextMain,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage enrollments, approve trial bookings, update syllabi, and respond to enquiries.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Navigation Tabs for Modules
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                items(moduleTitles.indices.toList()) { idx ->
                    val isSel = selectedModuleIndex == idx
                    Surface(
                        color = if (isSel) PrimaryBlue else CardBg,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSel) ElectricBlue else BorderColor
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedModuleIndex = idx }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = moduleTitles[idx],
                            color = if (isSel) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Switch Module Content
        when (selectedModuleIndex) {
            0 -> {
                // Overview KPIs
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            KpiCard("Registered Students", "${students.size}", ElectricBlue, Modifier.weight(1f))
                            KpiCard("Pending Trials", "${bookings.count { it.status == "Pending" }}", AccentGold, Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            KpiCard("New Enquiries", "${enquiries.count { it.status == "New" }}", SuccessGreen, Modifier.weight(1f))
                            KpiCard("Active Courses", "${courses.count { it.isActive }}", LightBlue, Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Recent Trial Bookings Pipeline", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(bookings.take(3)) { booking ->
                    BookingAdminRow(
                        booking = booking,
                        onApprove = { onUpdateBookingStatus(booking, "Approved") },
                        onCancel = { onUpdateBookingStatus(booking, "Cancelled") },
                        onDelete = { onDeleteBooking(booking) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            1 -> {
                // Trial Bookings Management
                item {
                    Text("Trial Bookings (${bookings.size})", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(bookings) { booking ->
                    BookingAdminRow(
                        booking = booking,
                        onApprove = { onUpdateBookingStatus(booking, "Approved") },
                        onCancel = { onUpdateBookingStatus(booking, "Cancelled") },
                        onDelete = { onDeleteBooking(booking) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            2 -> {
                // Enquiry Management
                item {
                    Text("Inquiries Pipeline (${enquiries.size})", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(enquiries) { enquiry ->
                    EnquiryAdminRow(
                        enquiry = enquiry,
                        onStatusChange = { newSt -> onUpdateEnquiryStatus(enquiry, newSt, null) },
                        onDelete = { onDeleteEnquiry(enquiry) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            3 -> {
                // Course Management
                item {
                    CourseAdminModule(
                        courses = courses,
                        onSaveCourse = onSaveCourse,
                        onDeleteCourse = onDeleteCourse
                    )
                }
            }

            4 -> {
                // Student Management
                item {
                    Text("Enrolled Students Directory (${students.size})", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(students) { student ->
                    Surface(
                        color = CardBg,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(student.fullName, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${student.chessRating} FIDE", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Email: ${student.email} • Tel: ${student.phone}", color = TextSecondary, fontSize = 12.sp)
                            Text("Level: ${student.experienceLevel} • Mode: ${student.preferredMode} • Courses: ${student.enrolledCoursesCount}", color = LightBlue, fontSize = 11.sp)
                        }
                    }
                }
            }

            5 -> {
                // Site Settings Management
                item {
                    SiteSettingsAdminModule(
                        settings = settings ?: SiteSettingsEntity(),
                        onSaveSettings = onSaveSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun KpiCard(label: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = label, color = TextMuted, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = accent, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun BookingAdminRow(
    booking: TrialBookingEntity,
    onApprove: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(booking.studentName, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Surface(
                    color = when (booking.status) {
                        "Approved" -> Color(0x2222C55E)
                        "Pending" -> Color(0x22FBBF24)
                        else -> Color(0x22EF4444)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = booking.status.uppercase(),
                        color = when (booking.status) {
                            "Approved" -> SuccessGreen
                            "Pending" -> AccentGold
                            else -> ErrorRed
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(booking.program, color = LightBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("Date: ${booking.preferredDate} • Slot: ${booking.timeSlot} (${booking.mode})", color = TextSecondary, fontSize = 11.sp)
            Text("Contact: ${booking.email} | ${booking.phone}", color = TextMuted, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (booking.status != "Approved") {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Approve", fontSize = 11.sp, color = Color.White)
                    }
                }
                if (booking.status != "Cancelled") {
                    OutlinedButton(
                        onClick = onCancel,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Cancel", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun EnquiryAdminRow(
    enquiry: EnquiryEntity,
    onStatusChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    val statuses = listOf("New", "Contacted", "Converted", "Closed")

    Surface(
        color = CardBg,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(enquiry.name, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(enquiry.status, color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Text("Subject: ${enquiry.subject}", color = LightBlue, fontSize = 12.sp)
            Text(enquiry.message, color = TextSecondary, fontSize = 11.sp)
            Text("Email: ${enquiry.email} • Tel: ${enquiry.phone}", color = TextMuted, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Status Changer
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Set Status:", color = TextMuted, fontSize = 10.sp)
                statuses.forEach { st ->
                    Surface(
                        color = if (enquiry.status == st) PrimaryBlue else CardBgElevated,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onStatusChange(st) }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(st, color = if (enquiry.status == st) Color.White else TextSecondary, fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
private fun CourseAdminModule(
    courses: List<CourseEntity>,
    onSaveCourse: (CourseEntity) -> Unit,
    onDeleteCourse: (CourseEntity) -> Unit
) {
    var showCreateForm by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Beginner Foundations") }
    var desc by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Beginner") }
    var fee by remember { mutableStateOf("299") }
    var coach by remember { mutableStateOf("GM Alexander Vance") }
    var schedule by remember { mutableStateOf("Mon & Wed 6:00 PM EST") }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Academy Courses Management", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
                onClick = { showCreateForm = !showCreateForm },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text(if (showCreateForm) "Close" else "+ Add Course", fontSize = 11.sp)
            }
        }

        if (showCreateForm) {
            Surface(
                color = CardBgElevated,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Add New Course", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Course Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = fee,
                            onValueChange = { fee = it },
                            label = { Text("Fee ($)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = coach,
                            onValueChange = { coach = it },
                            label = { Text("Assigned Coach") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSaveCourse(
                                    CourseEntity(
                                        title = title,
                                        category = category,
                                        description = desc,
                                        level = level,
                                        duration = "10 Weeks",
                                        mode = "Hybrid",
                                        fee = fee.toDoubleOrNull() ?: 299.0,
                                        coachName = coach,
                                        coachTitle = "Senior Coach",
                                        capacity = 12,
                                        availableSeats = 6,
                                        schedule = schedule
                                    )
                                )
                                showCreateForm = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Course in Database")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        courses.forEach { course ->
            Surface(
                color = CardBg,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(course.title, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${course.category} • ${course.level} • $${course.fee.toInt()}", color = LightBlue, fontSize = 11.sp)
                        Text("Coach: ${course.coachName} • ${course.availableSeats} seats left", color = TextSecondary, fontSize = 10.sp)
                    }
                    IconButton(onClick = { onDeleteCourse(course) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SiteSettingsAdminModule(
    settings: SiteSettingsEntity,
    onSaveSettings: (SiteSettingsEntity) -> Unit
) {
    var academyName by remember { mutableStateOf(settings.academyName) }
    var tagline by remember { mutableStateOf(settings.tagline) }
    var email by remember { mutableStateOf(settings.contactEmail) }
    var phone by remember { mutableStateOf(settings.contactPhone) }
    var address by remember { mutableStateOf(settings.address) }
    var announcement by remember { mutableStateOf(settings.announcement) }

    Surface(
        color = CardBg,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Academy Branding & Info Settings", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = academyName, onValueChange = { academyName = it }, label = { Text("Academy Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = tagline, onValueChange = { tagline = it }, label = { Text("Academy Tagline") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Contact Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Telephone") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Campus Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = announcement, onValueChange = { announcement = it }, label = { Text("Banner Announcement") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = {
                    onSaveSettings(
                        settings.copy(
                            academyName = academyName,
                            tagline = tagline,
                            contactEmail = email,
                            contactPhone = phone,
                            address = address,
                            announcement = announcement
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Site Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}
