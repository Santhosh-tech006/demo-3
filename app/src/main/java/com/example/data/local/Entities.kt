package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // Beginner Foundations, Opening Principles, Middlegame Strategy, etc.
    val description: String,
    val level: String, // Beginner, Intermediate, Advanced, Master
    val duration: String, // 8 Weeks, 12 Weeks, etc.
    val mode: String, // Online, Offline, Hybrid
    val fee: Double,
    val coachName: String,
    val coachTitle: String,
    val capacity: Int,
    val availableSeats: Int,
    val schedule: String,
    val isActive: Boolean = true
)

@Entity(tableName = "trial_bookings")
data class TrialBookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentName: String,
    val email: String,
    val phone: String,
    val program: String,
    val preferredDate: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. "10:00 AM - 11:30 AM"
    val mode: String, // Online, Offline
    val message: String = "",
    val status: String = "Pending", // Pending, Approved, Rescheduled, Cancelled, Completed
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "enquiries")
data class EnquiryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val subject: String,
    val message: String,
    val status: String = "New", // New, Contacted, Converted, Closed
    val adminNotes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val chessRating: Int = 1200,
    val experienceLevel: String = "Intermediate",
    val preferredMode: String = "Hybrid",
    val enrolledCoursesCount: Int = 1,
    val trialSessionStatus: String = "Confirmed",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: String,
    val location: String,
    val format: String, // Swiss System 7 Rounds, Blitz, Rapid
    val timeControl: String, // 15m + 10s, 3m + 2s
    val eligibility: String, // Open, Under 1800, Juniors
    val fee: Double,
    val registrationDeadline: String,
    val isOpen: Boolean = true,
    val registeredCount: Int = 24
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: String,
    val category: String, // Student Wins, FIDE Titles, Championships, Academy Milestone
    val description: String,
    val isVerified: Boolean = true
)

@Entity(tableName = "coaches")
data class CoachEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val title: String, // International Master, Grandmaster, FIDE Trainer
    val rating: Int,
    val specialization: String,
    val bio: String,
    val experienceYears: Int,
    val activeStudents: Int
)

@Entity(tableName = "site_settings")
data class SiteSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val academyName: String = "Grandmaster Academy",
    val tagline: String = "Think Deeper. Play Smarter. Become a Champion.",
    val contactEmail: String = "admissions@grandmasteracademy.com",
    val contactPhone: String = "+1 (800) 555-CHESS",
    val address: String = "450 Grandmaster Way, Suite 100, New York, NY",
    val announcement: String = "Spring Season Open Championship Registration Now Live!"
)
