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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.navigation.Screen
import com.example.ui.theme.BgPrimary
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
fun TrialBookingScreen(
    courses: List<CourseEntity>,
    onBookTrial: (name: String, email: String, phone: String, prog: String, date: String, slot: String, mode: String, msg: String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedProgram by remember {
        mutableStateOf(courses.firstOrNull()?.title ?: "Beginner Foundations: Rules, Vision & Fundamentals")
    }
    var selectedDate by remember { mutableStateOf("2026-05-05 (Tuesday)") }
    var selectedSlot by remember { mutableStateOf("10:00 AM - 11:30 AM") }
    var deliveryMode by remember { mutableStateOf("Online") }
    var message by remember { mutableStateOf("") }

    var isConfirmed by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val dateOptions = listOf(
        "2026-05-05 (Tuesday)",
        "2026-05-07 (Thursday)",
        "2026-05-09 (Saturday)",
        "2026-05-11 (Monday)"
    )

    val timeSlots = listOf(
        "10:00 AM - 11:30 AM",
        "2:00 PM - 3:30 PM",
        "6:00 PM - 7:30 PM"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgPrimary)
            .padding(16.dp)
            .testTag("trial_booking_screen_scroll")
    ) {
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "RESERVE YOUR SEAT",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Book a Free 1-on-1 Trial Session",
                    color = TextMain,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Get a comprehensive 60-minute rating diagnostic, tailored roadmap, and live coaching session with a titled Grandmaster or International Master.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        if (isConfirmed) {
            item {
                Surface(
                    color = CardBgElevated,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SuccessGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = SuccessGreen,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Free Trial Booking Confirmed!",
                            color = TextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Thank you $studentName! Your assessment session for \"$selectedProgram\" has been reserved for $selectedDate at $selectedSlot ($deliveryMode).\n\nA confirmation has been recorded in your Student Dashboard.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onNavigate(Screen.STUDENT_DASHBOARD) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Go to Student Portal")
                            }
                            Button(
                                onClick = { isConfirmed = false },
                                colors = ButtonDefaults.buttonColors(containerColor = CardBg),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Book Another", color = LightBlue)
                            }
                        }
                    }
                }
            }
        } else {
            item {
                if (errorMessage != null) {
                    Surface(
                        color = Color(0x33EF4444),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = ErrorRed,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("1. Select Target Program", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        courses.forEach { course ->
                            val isSel = selectedProgram == course.title
                            Surface(
                                color = if (isSel) CardBgElevated else Color.Transparent,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) ElectricBlue else BorderColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedProgram = course.title }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSel,
                                        onClick = { selectedProgram = course.title },
                                        colors = RadioButtonDefaults.colors(selectedColor = ElectricBlue)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(course.title, color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Text("${course.level} • ${course.coachName}", color = TextMuted, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("2. Session Format", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Online (Live Video)", "Offline (Campus)").forEach { m ->
                                val isSel = deliveryMode.startsWith(m.take(6))
                                Surface(
                                    color = if (isSel) CardBgElevated else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) ElectricBlue else BorderColor
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { deliveryMode = m.take(6).trim() }
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = m,
                                        color = if (isSel) ElectricBlue else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("3. Choose Date", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            dateOptions.forEach { date ->
                                val isSel = selectedDate == date
                                Surface(
                                    color = if (isSel) CardBgElevated else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) ElectricBlue else BorderColor
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedDate = date }
                                        .padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Event, contentDescription = null, tint = if (isSel) ElectricBlue else TextMuted, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = date, color = if (isSel) TextMain else TextSecondary, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("4. Select Time Slot (EST)", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            timeSlots.forEach { slot ->
                                val isSel = selectedSlot == slot
                                Surface(
                                    color = if (isSel) PrimaryBlue else CardBgElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) ElectricBlue else BorderColor
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedSlot = slot }
                                        .padding(10.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Schedule, contentDescription = null, tint = if (isSel) Color.White else LightBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = slot, color = if (isSel) Color.White else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("5. Student Information", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it; errorMessage = null },
                            label = { Text("Student Full Name *") },
                            placeholder = { Text("e.g. Leo Sterling") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMessage = null },
                            label = { Text("Parent / Student Email *") },
                            placeholder = { Text("leo@example.com") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it; errorMessage = null },
                            label = { Text("Phone Number *") },
                            placeholder = { Text("+1 (555) 123-4567") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Current Rating or Chess Background") },
                            placeholder = { Text("e.g. Rated 1250 on chess.com, wants to study endgames.") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain
                            ),
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (studentName.isBlank()) {
                                    errorMessage = "Please enter student name."
                                    return@Button
                                }
                                if (!email.contains("@") || email.isBlank()) {
                                    errorMessage = "Please enter a valid email address."
                                    return@Button
                                }
                                if (phone.isBlank()) {
                                    errorMessage = "Please provide contact telephone number."
                                    return@Button
                                }
                                onBookTrial(
                                    studentName,
                                    email,
                                    phone,
                                    selectedProgram,
                                    selectedDate,
                                    selectedSlot,
                                    deliveryMode,
                                    message
                                )
                                isConfirmed = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Confirm Booking (100% Free)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
