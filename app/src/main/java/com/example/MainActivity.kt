package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.CourseEntity
import com.example.ui.components.CourseDetailSheet
import com.example.ui.components.EnquiryDialog
import com.example.ui.components.GrandmasterAppBar
import com.example.ui.components.TrialBookingDialog
import com.example.ui.navigation.Screen
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProgramsScreen
import com.example.ui.screens.StudentDashboardScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.screens.TrialBookingScreen
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.GrandmasterAcademyTheme
import com.example.viewmodel.GrandmasterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrandmasterAcademyTheme {
                GrandmasterApp()
            }
        }
    }
}

@Composable
fun GrandmasterApp(viewModel: GrandmasterViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val filteredCourses by viewModel.filteredCourses.collectAsState()
    val allCourses by viewModel.allCourses.collectAsState()
    val coaches by viewModel.allCoaches.collectAsState()
    val tournaments by viewModel.allTournaments.collectAsState()
    val achievements by viewModel.allAchievements.collectAsState()
    val students by viewModel.allStudents.collectAsState()
    val bookings by viewModel.allBookings.collectAsState()
    val enquiries by viewModel.allEnquiries.collectAsState()
    val settings by viewModel.siteSettings.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedLevel by viewModel.selectedLevel.collectAsState()
    val selectedMode by viewModel.selectedMode.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()

    val selectedCourseDetail by viewModel.selectedCourseDetail.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog state
    var showTrialModal by remember { mutableStateOf(false) }
    var showEnquiryModal by remember { mutableStateOf(false) }
    var enquirySubjectContext by remember { mutableStateOf("Course Enquiry") }

    // Display snackbar toasts
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    // Hardware back handler for navigation stack
    BackHandler {
        if (!viewModel.handleBack()) {
            // Already at home screen
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            GrandmasterAppBar(
                currentScreen = currentScreen,
                isAdminMode = isAdminMode,
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                onToggleAdmin = { viewModel.toggleAdminMode() },
                onBookTrialClick = { showTrialModal = true }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BgPrimary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BgPrimary)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    Screen.HOME -> HomeScreen(
                        courses = allCourses,
                        coaches = coaches,
                        achievements = achievements,
                        onNavigate = { dest -> viewModel.navigateTo(dest) },
                        onBookTrialClick = { showTrialModal = true },
                        onSelectCourse = { c -> viewModel.selectCourse(c) },
                        onEnquireCourse = { c ->
                            enquirySubjectContext = "Inquiry: ${c.title}"
                            showEnquiryModal = true
                        }
                    )

                    Screen.ABOUT -> AboutScreen(
                        coaches = coaches,
                        onBookTrialClick = { showTrialModal = true }
                    )

                    Screen.PROGRAMS -> ProgramsScreen(
                        courses = filteredCourses,
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.searchQuery.value = it },
                        selectedCategory = selectedCategory,
                        onCategoryChange = { viewModel.selectedCategory.value = it },
                        selectedLevel = selectedLevel,
                        onLevelChange = { viewModel.selectedLevel.value = it },
                        selectedMode = selectedMode,
                        onModeChange = { viewModel.selectedMode.value = it },
                        selectedSort = selectedSort,
                        onSortChange = { viewModel.selectedSort.value = it },
                        onSelectCourse = { c -> viewModel.selectCourse(c) },
                        onBookTrial = { showTrialModal = true },
                        onEnquireCourse = { c ->
                            enquirySubjectContext = "Inquiry: ${c.title}"
                            showEnquiryModal = true
                        }
                    )

                    Screen.TRIAL_BOOKING -> TrialBookingScreen(
                        courses = allCourses,
                        onBookTrial = { name, email, phone, prog, date, slot, mode, msg ->
                            viewModel.submitBooking(name, email, phone, prog, date, slot, mode, msg) {}
                        },
                        onNavigate = { dest -> viewModel.navigateTo(dest) }
                    )

                    Screen.TOURNAMENTS -> TournamentsScreen(
                        tournaments = tournaments,
                        achievements = achievements,
                        onRegisterTournament = { t -> viewModel.registerForTournament(t) }
                    )

                    Screen.CONTACT -> ContactScreen(
                        settings = settings,
                        onSubmitEnquiry = { name, email, phone, subject, msg ->
                            viewModel.submitEnquiry(name, email, phone, subject, msg) {}
                        }
                    )

                    Screen.STUDENT_DASHBOARD -> StudentDashboardScreen(
                        students = students,
                        bookings = bookings,
                        enquiries = enquiries,
                        onRegisterStudent = { name, email, phone, rating, level, mode ->
                            viewModel.registerStudent(name, email, phone, rating, level, mode) {}
                        },
                        onBookTrialClick = { showTrialModal = true }
                    )

                    Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                        students = students,
                        courses = allCourses,
                        bookings = bookings,
                        enquiries = enquiries,
                        tournaments = tournaments,
                        settings = settings,
                        onUpdateBookingStatus = { b, st -> viewModel.adminUpdateBookingStatus(b, st) },
                        onDeleteBooking = { b -> viewModel.adminDeleteBooking(b) },
                        onUpdateEnquiryStatus = { enq, st, notes -> viewModel.adminUpdateEnquiryStatus(enq, st, notes) },
                        onDeleteEnquiry = { enq -> viewModel.adminDeleteEnquiry(enq) },
                        onSaveCourse = { c -> viewModel.adminSaveCourse(c) },
                        onDeleteCourse = { c -> viewModel.adminDeleteCourse(c) },
                        onSaveTournament = { t -> viewModel.adminSaveTournament(t) },
                        onSaveSettings = { s -> viewModel.adminSaveSettings(s) }
                    )
                }
            }

            // Quick Trial Booking Dialog
            if (showTrialModal) {
                TrialBookingDialog(
                    availableCourses = allCourses,
                    onDismiss = { showTrialModal = false },
                    onSubmit = { name, email, phone, prog, date, slot, mode, msg ->
                        viewModel.submitBooking(name, email, phone, prog, date, slot, mode, msg) {
                            showTrialModal = false
                        }
                    }
                )
            }

            // Quick Enquiry Dialog
            if (showEnquiryModal) {
                EnquiryDialog(
                    initialSubject = enquirySubjectContext,
                    onDismiss = { showEnquiryModal = false },
                    onSubmit = { name, email, phone, subject, msg ->
                        viewModel.submitEnquiry(name, email, phone, subject, msg) {
                            showEnquiryModal = false
                        }
                    }
                )
            }

            // Course Detail Sheet
            selectedCourseDetail?.let { course ->
                CourseDetailSheet(
                    course = course,
                    onDismiss = { viewModel.selectCourse(null) },
                    onBookTrial = {
                        viewModel.selectCourse(null)
                        showTrialModal = true
                    },
                    onEnquire = {
                        viewModel.selectCourse(null)
                        enquirySubjectContext = "Inquiry: ${course.title}"
                        showEnquiryModal = true
                    }
                )
            }
        }
    }
}
