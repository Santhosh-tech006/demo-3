package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GrandmasterDao {
    // Courses
    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE isActive = 1 ORDER BY id ASC")
    fun getActiveCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    suspend fun getCourseById(id: Long): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    // Trial Bookings
    @Query("SELECT * FROM trial_bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<TrialBookingEntity>>

    @Query("SELECT * FROM trial_bookings WHERE preferredDate = :date AND timeSlot = :slot")
    suspend fun getBookingsForSlot(date: String, slot: String): List<TrialBookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: TrialBookingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<TrialBookingEntity>)

    @Update
    suspend fun updateBooking(booking: TrialBookingEntity)

    @Delete
    suspend fun deleteBooking(booking: TrialBookingEntity)

    // Enquiries
    @Query("SELECT * FROM enquiries ORDER BY createdAt DESC")
    fun getAllEnquiries(): Flow<List<EnquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiries(enquiries: List<EnquiryEntity>)

    @Update
    suspend fun updateEnquiry(enquiry: EnquiryEntity)

    @Delete
    suspend fun deleteEnquiry(enquiry: EnquiryEntity)

    // Students
    @Query("SELECT * FROM students ORDER BY id DESC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    // Tournaments
    @Query("SELECT * FROM tournaments ORDER BY id ASC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournaments(tournaments: List<TournamentEntity>)

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    // Achievements
    @Query("SELECT * FROM achievements ORDER BY id DESC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    // Coaches
    @Query("SELECT * FROM coaches ORDER BY id ASC")
    fun getAllCoaches(): Flow<List<CoachEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoaches(coaches: List<CoachEntity>)

    // Settings
    @Query("SELECT * FROM site_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<SiteSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSettings(settings: SiteSettingsEntity)
}
