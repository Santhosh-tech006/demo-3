package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AchievementEntity
import com.example.data.local.CoachEntity
import com.example.data.local.CourseEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.GrandmasterDatabase
import com.example.data.local.SiteSettingsEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TrialBookingEntity
import com.example.data.local.TournamentEntity
import com.example.data.repository.GrandmasterRepository
import com.example.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GrandmasterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GrandmasterRepository

    init {
        val database = GrandmasterDatabase.getDatabase(application, viewModelScope)
        repository = GrandmasterRepository(database.grandmasterDao())
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navStack = mutableListOf(Screen.HOME)

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _navStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        if (_navStack.size > 1) {
            _navStack.removeAt(_navStack.size - 1)
            _currentScreen.value = _navStack.last()
            return true
        }
        return false
    }

    // Admin mode toggle (for instant demo switching between student & academy owner)
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    fun toggleAdminMode() {
        val next = !_isAdminMode.value
        _isAdminMode.value = next
        if (next) {
            navigateTo(Screen.ADMIN_DASHBOARD)
            showToast("Admin Mode Activated: Full Management Controls Enabled")
        } else {
            navigateTo(Screen.HOME)
            showToast("Student & Visitor View Activated")
        }
    }

    // Raw data flows
    val allCourses: StateFlow<List<CourseEntity>> = repository.allCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCoaches: StateFlow<List<CoachEntity>> = repository.allCoaches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTournaments: StateFlow<List<TournamentEntity>> = repository.allTournaments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAchievements: StateFlow<List<AchievementEntity>> = repository.allAchievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudents: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<TrialBookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEnquiries: StateFlow<List<EnquiryEntity>> = repository.allEnquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val siteSettings: StateFlow<SiteSettingsEntity?> = repository.siteSettings
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SiteSettingsEntity(
                academyName = "Grandmaster Academy",
                tagline = "Think Deeper. Play Smarter. Become a Champion."
            )
        )

    // Course filters and search
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val selectedLevel = MutableStateFlow("All")
    val selectedMode = MutableStateFlow("All")
    val selectedSort = MutableStateFlow("Recommended")

    data class FilterOptions(
        val query: String,
        val category: String,
        val level: String,
        val mode: String,
        val sort: String
    )

    private val filterOptionsFlow = combine(
        searchQuery,
        selectedCategory,
        selectedLevel,
        selectedMode,
        selectedSort
    ) { query, category, level, mode, sort ->
        FilterOptions(query, category, level, mode, sort)
    }

    // Filtered courses
    val filteredCourses: StateFlow<List<CourseEntity>> = combine(
        allCourses,
        filterOptionsFlow
    ) { courses, filters ->
        var list = courses.filter { it.isActive }

        if (filters.query.isNotBlank()) {
            val q = filters.query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.coachName.lowercase().contains(q) ||
                it.category.lowercase().contains(q)
            }
        }

        if (filters.category != "All") {
            list = list.filter { it.category.equals(filters.category, ignoreCase = true) }
        }

        if (filters.level != "All") {
            list = list.filter { it.level.equals(filters.level, ignoreCase = true) }
        }

        if (filters.mode != "All") {
            list = list.filter { it.mode.equals(filters.mode, ignoreCase = true) }
        }

        when (filters.sort) {
            "Fee: Low to High" -> list.sortedBy { it.fee }
            "Fee: High to Low" -> list.sortedByDescending { it.fee }
            "Duration" -> list.sortedBy { it.duration }
            else -> list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected course details sheet
    private val _selectedCourseDetail = MutableStateFlow<CourseEntity?>(null)
    val selectedCourseDetail: StateFlow<CourseEntity?> = _selectedCourseDetail.asStateFlow()

    fun selectCourse(course: CourseEntity?) {
        _selectedCourseDetail.value = course
    }

    // Toast/Feedback state
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Trial Booking Action
    fun submitBooking(
        name: String,
        email: String,
        phone: String,
        program: String,
        date: String,
        slot: String,
        mode: String,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.bookTrial(
                studentName = name,
                email = email,
                phone = phone,
                program = program,
                preferredDate = date,
                timeSlot = slot,
                mode = mode,
                message = message
            )
            result.onSuccess {
                showToast("✓ Free Trial Booked for $date ($slot)! Confirmation saved.")
                onSuccess()
            }.onFailure { err ->
                showToast("Booking Error: ${err.message}")
            }
        }
    }

    // Enquiry Action
    fun submitEnquiry(
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.submitEnquiry(name, email, phone, subject, message)
            result.onSuccess {
                showToast("✓ Enquiry received! Our admissions team will respond within 24h.")
                onSuccess()
            }.onFailure { err ->
                showToast("Enquiry Error: ${err.message}")
            }
        }
    }

    // Student Registration Action
    fun registerStudent(
        fullName: String,
        email: String,
        phone: String,
        rating: Int,
        level: String,
        mode: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.registerStudent(fullName, email, phone, rating, level, mode)
            showToast("✓ Student Registration successful for $fullName!")
            onSuccess()
        }
    }

    // Tournament registration
    fun registerForTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            val updated = repository.registerForTournament(tournament)
            showToast("✓ Registered for ${updated.title}! Spot reserved (${updated.registeredCount} players).")
        }
    }

    // Admin Operations
    fun adminUpdateBookingStatus(booking: TrialBookingEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(booking, newStatus)
            showToast("Trial booking #${booking.id} updated to $newStatus")
        }
    }

    fun adminDeleteBooking(booking: TrialBookingEntity) {
        viewModelScope.launch {
            repository.deleteBooking(booking)
            showToast("Trial booking removed")
        }
    }

    fun adminUpdateEnquiryStatus(enquiry: EnquiryEntity, newStatus: String, notes: String? = null) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(enquiry, newStatus, notes)
            showToast("Enquiry #${enquiry.id} marked as $newStatus")
        }
    }

    fun adminDeleteEnquiry(enquiry: EnquiryEntity) {
        viewModelScope.launch {
            repository.deleteEnquiry(enquiry)
            showToast("Enquiry removed")
        }
    }

    fun adminSaveCourse(course: CourseEntity) {
        viewModelScope.launch {
            if (course.id == 0L) {
                repository.insertCourse(course)
                showToast("New Course '${course.title}' created!")
            } else {
                repository.updateCourse(course)
                showToast("Course '${course.title}' updated!")
            }
        }
    }

    fun adminDeleteCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.deleteCourse(course)
            showToast("Course deleted")
        }
    }

    fun adminSaveTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            repository.insertTournament(tournament)
            showToast("Tournament '${tournament.title}' saved!")
        }
    }

    fun adminSaveSettings(settings: SiteSettingsEntity) {
        viewModelScope.launch {
            repository.updateSettings(settings)
            showToast("Site settings updated successfully!")
        }
    }
}
