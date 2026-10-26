package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.data.local.EnquiryEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TrialBookingEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CardBgElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LightBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun StudentDashboardScreen(
    students: List<StudentEntity>,
    bookings: List<TrialBookingEntity>,
    enquiries: List<EnquiryEntity>,
    onRegisterStudent: (fullName: String, email: String, phone: String, rating: Int, level: String, mode: String) -> Unit,
    onBookTrialClick: () -> Unit
) {
    val activeStudent = students.firstOrNull() ?: StudentEntity(
        fullName = "Alexander Ward",
        email = "alex.ward@example.com",
        phone = "+1 (555) 777-8899",
        chessRating = 1620,
        experienceLevel = "Intermediate",
        preferredMode = "Hybrid",
        enrolledCoursesCount = 2
    )

    var showRegisterForm by remember { mutableStateOf(false) }

    // Registration Form state
    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regRating by remember { mutableStateOf("1400") }
    var regLevel by remember { mutableStateOf("Intermediate") }
    var regMode by remember { mutableStateOf("Online") }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgPrimary)
            .padding(16.dp)
            .testTag("student_dashboard_scroll")
    ) {
        // Dashboard Greeting
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "STUDENT PORTAL",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Welcome Back, ${activeStudent.fullName.split(" ").first()}",
                    color = TextMain,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track your learning progress, scheduled trial sessions, and submitted enquiries.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Student Profile Header Card
        item {
            Surface(
                color = CardBg,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlue)
                                    .border(1.5.dp, ElectricBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activeStudent.fullName.take(1),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = activeStudent.fullName,
                                    color = TextMain,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = activeStudent.email,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Rating badge
                        Surface(
                            color = CardBgElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = AccentGold, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${activeStudent.chessRating} FIDE",
                                    color = TextMain,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProfileStatItem(label = "Level", value = activeStudent.experienceLevel, modifier = Modifier.weight(1f))
                        ProfileStatItem(label = "Mode", value = activeStudent.preferredMode, modifier = Modifier.weight(1f))
                        ProfileStatItem(label = "Courses", value = "${activeStudent.enrolledCoursesCount} Active", modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Daily Tactics Workout Card
        item {
            Surface(
                color = CardBgElevated,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Daily Grandmaster Tactical Drill", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("White to move and deliver mate in 3 moves. (Puzzle #104)", color = TextSecondary, fontSize = 12.sp)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Solve", fontSize = 11.sp)
                    }
                }
            }
        }

        // Scheduled Free Trial / Sessions
        item {
            Column(modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Trial & Class Bookings",
                        color = TextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "+ Book Session",
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onBookTrialClick() }
                    )
                }
            }
        }

        if (bookings.isEmpty()) {
            item {
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No scheduled trial sessions yet. Tap '+ Book Session' above to reserve your free 60-min assessment.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(bookings) { booking ->
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
                            Text(booking.program, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Surface(
                                color = when (booking.status) {
                                    "Approved" -> Color(0x2222C55E)
                                    "Pending" -> Color(0x22FBBF24)
                                    else -> CardBgElevated
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = booking.status.uppercase(),
                                    color = when (booking.status) {
                                        "Approved" -> SuccessGreen
                                        "Pending" -> AccentGold
                                        else -> LightBlue
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Date: ${booking.preferredDate} • Slot: ${booking.timeSlot} (${booking.mode})",
                            color = LightBlue,
                            fontSize = 11.sp
                        )
                        if (booking.message.isNotBlank()) {
                            Text(text = "Notes: ${booking.message}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Enquiry History
        item {
            Column(modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)) {
                Text(
                    text = "Submitted Enquiries (${enquiries.size})",
                    color = TextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        if (enquiries.isEmpty()) {
            item {
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No previous enquiries found.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            items(enquiries) { enq ->
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
                            Text(enq.subject, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(enq.status, color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(enq.message, color = TextSecondary, fontSize = 11.sp, maxLines = 2)
                        if (enq.adminNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Admissions Response: ${enq.adminNotes}", color = LightBlue, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Option to Register New Student
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showRegisterForm = !showRegisterForm },
                colors = ButtonDefaults.buttonColors(containerColor = if (showRegisterForm) CardBgElevated else PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showRegisterForm) "Close Registration Form" else "+ Register New Student Profile", fontWeight = FontWeight.Bold)
            }

            if (showRegisterForm) {
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("New Student Registration", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regName,
                            onValueChange = { regName = it },
                            label = { Text("Full Name *") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            label = { Text("Email Address *") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = regPhone,
                            onValueChange = { regPhone = it },
                            label = { Text("Phone Number *") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = regRating,
                            onValueChange = { regRating = it },
                            label = { Text("Current Chess Rating (Approx)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (regName.isNotBlank() && regEmail.isNotBlank()) {
                                    onRegisterStudent(
                                        regName,
                                        regEmail,
                                        regPhone,
                                        regRating.toIntOrNull() ?: 1200,
                                        regLevel,
                                        regMode
                                    )
                                    showRegisterForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Student Profile", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = CardBgElevated,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = TextMuted, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = LightBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
