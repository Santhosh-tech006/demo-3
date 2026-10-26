package com.example

import com.example.data.local.CourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.GrandmasterDao
import com.example.data.local.SiteSettingsEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TournamentEntity
import com.example.data.local.TrialBookingEntity
import com.example.data.repository.GrandmasterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GrandmasterRepositoryTest {

    private lateinit var mockDao: FakeGrandmasterDao
    private lateinit var repository: GrandmasterRepository

    @Before
    fun setUp() {
        mockDao = FakeGrandmasterDao()
        repository = GrandmasterRepository(mockDao)
    }

    @Test
    fun bookTrial_success_whenSlotAvailable() = runBlocking {
        val result = repository.bookTrial(
            studentName = "Elena Vance",
            email = "elena@example.com",
            phone = "+1 555-1234",
            program = "Opening Principles",
            preferredDate = "2026-05-05",
            timeSlot = "10:00 AM - 11:30 AM",
            mode = "Online",
            message = "Excited to learn"
        )
        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())
    }

    @Test
    fun bookTrial_rejects_duplicateBookingForSameSlot() = runBlocking {
        repository.bookTrial(
            studentName = "Elena Vance",
            email = "elena@example.com",
            phone = "+1 555-1234",
            program = "Opening Principles",
            preferredDate = "2026-05-05",
            timeSlot = "10:00 AM - 11:30 AM",
            mode = "Online",
            message = "First booking"
        )

        val duplicateResult = repository.bookTrial(
            studentName = "Elena Vance",
            email = "elena@example.com",
            phone = "+1 555-1234",
            program = "Opening Principles",
            preferredDate = "2026-05-05",
            timeSlot = "10:00 AM - 11:30 AM",
            mode = "Online",
            message = "Second duplicate booking"
        )
        assertTrue(duplicateResult.isFailure)
        assertTrue(duplicateResult.exceptionOrNull()?.message?.contains("already booked") == true)
    }

    @Test
    fun submitEnquiry_validatesRequiredFields() = runBlocking {
        val blankResult = repository.submitEnquiry("", "", "", "", "")
        assertTrue(blankResult.isFailure)

        val validResult = repository.submitEnquiry(
            name = "Marcus",
            email = "marcus@example.com",
            phone = "123",
            subject = "Question",
            message = "Details"
        )
        assertTrue(validResult.isSuccess)
    }
}

class FakeGrandmasterDao : GrandmasterDao {
    private val bookings = mutableListOf<TrialBookingEntity>()
    private val enquiries = mutableListOf<EnquiryEntity>()

    override fun getAllCourses(): Flow<List<CourseEntity>> = flowOf(emptyList())
    override fun getActiveCourses(): Flow<List<CourseEntity>> = flowOf(emptyList())
    override suspend fun getCourseById(id: Long): CourseEntity? = null
    override suspend fun insertCourse(course: CourseEntity): Long = 1L
    override suspend fun insertCourses(courses: List<CourseEntity>) {}
    override suspend fun updateCourse(course: CourseEntity) {}
    override suspend fun deleteCourse(course: CourseEntity) {}

    override fun getAllBookings(): Flow<List<TrialBookingEntity>> = flowOf(bookings)
    override suspend fun getBookingsForSlot(date: String, slot: String): List<TrialBookingEntity> {
        return bookings.filter { it.preferredDate == date && it.timeSlot == slot }
    }
    override suspend fun insertBooking(booking: TrialBookingEntity): Long {
        bookings.add(booking.copy(id = (bookings.size + 1).toLong()))
        return bookings.last().id
    }
    override suspend fun insertBookings(bookings: List<TrialBookingEntity>) { this.bookings.addAll(bookings) }
    override suspend fun updateBooking(booking: TrialBookingEntity) {}
    override suspend fun deleteBooking(booking: TrialBookingEntity) { bookings.remove(booking) }

    override fun getAllEnquiries(): Flow<List<EnquiryEntity>> = flowOf(enquiries)
    override suspend fun insertEnquiry(enquiry: EnquiryEntity): Long {
        enquiries.add(enquiry.copy(id = (enquiries.size + 1).toLong()))
        return enquiries.last().id
    }
    override suspend fun insertEnquiries(enquiries: List<EnquiryEntity>) { this.enquiries.addAll(enquiries) }
    override suspend fun updateEnquiry(enquiry: EnquiryEntity) {}
    override suspend fun deleteEnquiry(enquiry: EnquiryEntity) { enquiries.remove(enquiry) }

    override fun getAllStudents(): Flow<List<StudentEntity>> = flowOf(emptyList())
    override suspend fun insertStudent(student: StudentEntity): Long = 1L
    override suspend fun insertStudents(students: List<StudentEntity>) {}
    override suspend fun updateStudent(student: StudentEntity) {}
    override suspend fun deleteStudent(student: StudentEntity) {}

    override fun getAllTournaments(): Flow<List<TournamentEntity>> = flowOf(emptyList())
    override suspend fun insertTournament(tournament: TournamentEntity): Long = 1L
    override suspend fun insertTournaments(tournaments: List<TournamentEntity>) {}
    override suspend fun updateTournament(tournament: TournamentEntity) {}

    override fun getAllAchievements(): Flow<List<com.example.data.local.AchievementEntity>> = flowOf(emptyList())
    override suspend fun insertAchievements(achievements: List<com.example.data.local.AchievementEntity>) {}

    override fun getAllCoaches(): Flow<List<com.example.data.local.CoachEntity>> = flowOf(emptyList())
    override suspend fun insertCoaches(coaches: List<com.example.data.local.CoachEntity>) {}

    override fun getSettings(): Flow<SiteSettingsEntity?> = flowOf(null)
    override suspend fun setSettings(settings: SiteSettingsEntity) {}
}
