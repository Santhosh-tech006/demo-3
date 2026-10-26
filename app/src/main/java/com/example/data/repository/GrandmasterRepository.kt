package com.example.data.repository

import com.example.data.local.AchievementEntity
import com.example.data.local.CoachEntity
import com.example.data.local.CourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.GrandmasterDao
import com.example.data.local.SiteSettingsEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TournamentEntity
import com.example.data.local.TrialBookingEntity
import kotlinx.coroutines.flow.Flow

class GrandmasterRepository(private val dao: GrandmasterDao) {

    // Courses
    val allCourses: Flow<List<CourseEntity>> = dao.getAllCourses()
    val activeCourses: Flow<List<CourseEntity>> = dao.getActiveCourses()

    suspend fun insertCourse(course: CourseEntity): Long = dao.insertCourse(course)
    suspend fun updateCourse(course: CourseEntity) = dao.updateCourse(course)
    suspend fun deleteCourse(course: CourseEntity) = dao.deleteCourse(course)
    suspend fun getCourseById(id: Long): CourseEntity? = dao.getCourseById(id)

    // Trial Bookings
    val allBookings: Flow<List<TrialBookingEntity>> = dao.getAllBookings()

    suspend fun bookTrial(
        studentName: String,
        email: String,
        phone: String,
        program: String,
        preferredDate: String,
        timeSlot: String,
        mode: String,
        message: String
    ): Result<Long> {
        // Enforce slot availability check & duplicate check
        val existing = dao.getBookingsForSlot(preferredDate, timeSlot)
        val maxSlotCapacity = 5
        if (existing.size >= maxSlotCapacity) {
            return Result.failure(Exception("This time slot ($timeSlot on $preferredDate) is fully booked. Please select another slot."))
        }
        val duplicate = existing.any { it.email.equals(email, ignoreCase = true) }
        if (duplicate) {
            return Result.failure(Exception("You have already booked this time slot. Check your confirmation or contact academy staff."))
        }

        val booking = TrialBookingEntity(
            studentName = studentName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            program = program,
            preferredDate = preferredDate,
            timeSlot = timeSlot,
            mode = mode,
            message = message.trim(),
            status = "Pending"
        )
        val id = dao.insertBooking(booking)
        return Result.success(id)
    }

    suspend fun updateBookingStatus(booking: TrialBookingEntity, newStatus: String) {
        dao.updateBooking(booking.copy(status = newStatus))
    }

    suspend fun deleteBooking(booking: TrialBookingEntity) = dao.deleteBooking(booking)

    // Enquiries
    val allEnquiries: Flow<List<EnquiryEntity>> = dao.getAllEnquiries()

    suspend fun submitEnquiry(
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String
    ): Result<Long> {
        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            return Result.failure(Exception("Please complete all required fields."))
        }
        val enquiry = EnquiryEntity(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            subject = subject.trim(),
            message = message.trim(),
            status = "New"
        )
        val id = dao.insertEnquiry(enquiry)
        return Result.success(id)
    }

    suspend fun updateEnquiryStatus(enquiry: EnquiryEntity, newStatus: String, adminNotes: String? = null) {
        dao.updateEnquiry(
            enquiry.copy(
                status = newStatus,
                adminNotes = adminNotes ?: enquiry.adminNotes
            )
        )
    }

    suspend fun deleteEnquiry(enquiry: EnquiryEntity) = dao.deleteEnquiry(enquiry)

    // Students
    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()

    suspend fun registerStudent(
        fullName: String,
        email: String,
        phone: String,
        chessRating: Int,
        experienceLevel: String,
        preferredMode: String
    ): Long {
        val student = StudentEntity(
            fullName = fullName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            chessRating = chessRating,
            experienceLevel = experienceLevel,
            preferredMode = preferredMode,
            enrolledCoursesCount = 1
        )
        return dao.insertStudent(student)
    }

    suspend fun updateStudent(student: StudentEntity) = dao.updateStudent(student)
    suspend fun deleteStudent(student: StudentEntity) = dao.deleteStudent(student)

    // Tournaments
    val allTournaments: Flow<List<TournamentEntity>> = dao.getAllTournaments()

    suspend fun registerForTournament(tournament: TournamentEntity): TournamentEntity {
        val updated = tournament.copy(registeredCount = tournament.registeredCount + 1)
        dao.updateTournament(updated)
        return updated
    }

    suspend fun insertTournament(tournament: TournamentEntity): Long = dao.insertTournament(tournament)

    // Achievements
    val allAchievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()

    // Coaches
    val allCoaches: Flow<List<CoachEntity>> = dao.getAllCoaches()

    // Settings
    val siteSettings: Flow<SiteSettingsEntity?> = dao.getSettings()

    suspend fun updateSettings(settings: SiteSettingsEntity) = dao.setSettings(settings)
}
