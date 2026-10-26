package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CourseEntity::class,
        TrialBookingEntity::class,
        EnquiryEntity::class,
        StudentEntity::class,
        TournamentEntity::class,
        AchievementEntity::class,
        CoachEntity::class,
        SiteSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GrandmasterDatabase : RoomDatabase() {
    abstract fun grandmasterDao(): GrandmasterDao

    companion object {
        @Volatile
        private var INSTANCE: GrandmasterDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GrandmasterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GrandmasterDatabase::class.java,
                    "grandmaster_academy_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(GrandmasterDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class GrandmasterDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.grandmasterDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: GrandmasterDao) {
            // Seed Settings
            dao.setSettings(
                SiteSettingsEntity(
                    id = 1,
                    academyName = "Grandmaster Academy",
                    tagline = "Think Deeper. Play Smarter. Become a Champion.",
                    contactEmail = "admissions@grandmasteracademy.com",
                    contactPhone = "+1 (800) 555-CHESS",
                    address = "450 Grandmaster Way, Suite 100, New York, NY",
                    announcement = "Summer Grand Prix Championship Registrations Are Now Open!"
                )
            )

            // Seed Courses
            val courses = listOf(
                CourseEntity(
                    title = "Beginner Foundations: Rules, Vision & Fundamentals",
                    category = "Beginner Foundations",
                    description = "Master board vision, tactical piece coordination, basic checkmates, and avoid common novice pitfalls.",
                    level = "Beginner",
                    duration = "8 Weeks",
                    mode = "Online",
                    fee = 249.0,
                    coachName = "IM Elena Rostova",
                    coachTitle = "Senior Trainer",
                    capacity = 16,
                    availableSeats = 5,
                    schedule = "Tue & Thu • 5:00 PM EST"
                ),
                CourseEntity(
                    title = "Opening Principles & Repertoire Architecture",
                    category = "Opening Principles",
                    description = "Build a solid, lifelong opening repertoire with both 1.e4 and 1.d4, understanding plans rather than rote memorization.",
                    level = "Intermediate",
                    duration = "10 Weeks",
                    mode = "Hybrid",
                    fee = 380.0,
                    coachName = "GM Alexander Vance",
                    coachTitle = "Head Coach",
                    capacity = 12,
                    availableSeats = 3,
                    schedule = "Mon & Wed • 6:30 PM EST"
                ),
                CourseEntity(
                    title = "Dynamic Middlegame Strategy & Pawn Structures",
                    category = "Middlegame Strategy",
                    description = "Learn how to formulate plans based on pawn structure, exploit outposts, and execute king attacks with confidence.",
                    level = "Intermediate",
                    duration = "10 Weeks",
                    mode = "Online",
                    fee = 399.0,
                    coachName = "GM Magnus Thornton",
                    coachTitle = "Grandmaster Analyst",
                    capacity = 12,
                    availableSeats = 2,
                    schedule = "Saturdays • 11:00 AM EST"
                ),
                CourseEntity(
                    title = "Deep Calculation, Tactics & Intuitive Sacrifices",
                    category = "Calculation and Tactics",
                    description = "Develop candidate-move discipline, evaluate complex tactical trees, and build the calculation speed required in blitz and classical time controls.",
                    level = "Advanced",
                    duration = "12 Weeks",
                    mode = "Hybrid",
                    fee = 499.0,
                    coachName = "GM Magnus Thornton",
                    coachTitle = "Grandmaster Analyst",
                    capacity = 10,
                    availableSeats = 4,
                    schedule = "Sundays • 2:00 PM EST"
                ),
                CourseEntity(
                    title = "Endgame Mastery: Theoretical & Practical Play",
                    category = "Endgame Mastery",
                    description = "Master rook endgames, opposite-colored bishops, king activity, and precision conversion under extreme time pressure.",
                    level = "Advanced",
                    duration = "8 Weeks",
                    mode = "Online",
                    fee = 350.0,
                    coachName = "IM David Chen",
                    coachTitle = "Youth Program Director",
                    capacity = 14,
                    availableSeats = 7,
                    schedule = "Fridays • 6:00 PM EST"
                ),
                CourseEntity(
                    title = "Tournament Preparation & Competitive Psychology",
                    category = "Tournament Preparation",
                    description = "Simulate real Swiss tournament pressures, opponent profiling, clock management, and psychological resilience.",
                    level = "Master",
                    duration = "12 Weeks",
                    mode = "Hybrid",
                    fee = 650.0,
                    coachName = "GM Alexander Vance",
                    coachTitle = "Head Coach",
                    capacity = 8,
                    availableSeats = 2,
                    schedule = "Wed & Sat • 7:00 PM EST"
                ),
                CourseEntity(
                    title = "Elite 1-on-1 Grandmaster Private Mentorship",
                    category = "Personalized Coaching",
                    description = "Tailored game analysis, personalized homework, weekly private sparring, and direct WhatsApp mentorship with a Grandmaster.",
                    level = "All Levels",
                    duration = "Flexible",
                    mode = "Online",
                    fee = 140.0,
                    coachName = "GM Alexander Vance",
                    coachTitle = "Head Coach",
                    capacity = 6,
                    availableSeats = 1,
                    schedule = "Custom Scheduled Hours"
                )
            )
            dao.insertCourses(courses)

            // Seed Coaches
            val coaches = listOf(
                CoachEntity(
                    name = "GM Alexander Vance",
                    title = "Grandmaster & Head Coach",
                    rating = 2595,
                    specialization = "Dynamic Attacking & King's Indian Systems",
                    bio = "Grandmaster with 14 years of international competitive play. Coached 3 National Champions and multiple Olympiad representatives.",
                    experienceYears = 14,
                    activeStudents = 42
                ),
                CoachEntity(
                    name = "IM Elena Rostova",
                    title = "International Master & Senior Trainer",
                    rating = 2430,
                    specialization = "Positional Strategy & Opening Repertoire",
                    bio = "Former Women's Continental Champion and FIDE Senior Trainer, celebrated for breaking down complex concepts into actionable mental models.",
                    experienceYears = 11,
                    activeStudents = 35
                ),
                CoachEntity(
                    name = "GM Magnus Thornton",
                    title = "Grandmaster & Tactical Analyst",
                    rating = 2620,
                    specialization = "Deep Calculation & Tournament Psychology",
                    bio = "Olympiad team member and elite computer-assisted tactician specializing in rapid calculation and psychological preparation.",
                    experienceYears = 16,
                    activeStudents = 28
                ),
                CoachEntity(
                    name = "IM David Chen",
                    title = "International Master & Youth Director",
                    rating = 2390,
                    specialization = "Endgame Mechanics & Junior Talent",
                    bio = "Award-winning youth mentor passionate about cultivating foundational clarity and competitive joy in young chess enthusiasts.",
                    experienceYears = 9,
                    activeStudents = 48
                )
            )
            dao.insertCoaches(coaches)

            // Seed Tournaments
            val tournaments = listOf(
                TournamentEntity(
                    title = "Grandmaster Academy Spring Open 2026",
                    date = "May 16–18, 2026",
                    location = "Main Academy Arena & Online Live",
                    format = "7-Round FIDE Rated Swiss",
                    timeControl = "90m + 30s increment",
                    eligibility = "Open to All Rated Players",
                    fee = 65.0,
                    registrationDeadline = "May 10, 2026",
                    isOpen = true,
                    registeredCount = 54
                ),
                TournamentEntity(
                    title = "Metropolitan Blitz Grand Prix",
                    date = "June 6, 2026",
                    location = "Academy Pavilion (In-Person)",
                    format = "9-Round Double Blitz",
                    timeControl = "3m + 2s increment",
                    eligibility = "USCF / FIDE > 1400",
                    fee = 40.0,
                    registrationDeadline = "June 2, 2026",
                    isOpen = true,
                    registeredCount = 38
                ),
                TournamentEntity(
                    title = "Junior Prodigies Invitational (U16)",
                    date = "July 11, 2026",
                    location = "Global Online Arena",
                    format = "5-Round Rapid Swiss",
                    timeControl = "15m + 10s increment",
                    eligibility = "Ages 8–16, Rating < 2000",
                    fee = 30.0,
                    registrationDeadline = "July 7, 2026",
                    isOpen = true,
                    registeredCount = 29
                )
            )
            dao.insertTournaments(tournaments)

            // Seed Achievements
            val achievements = listOf(
                AchievementEntity(
                    title = "First Place — National Junior Championship",
                    date = "April 2026",
                    category = "Student Wins",
                    description = "Academy student Marcus Vance scored an undefeated 6.5/7 to secure the National Under-16 Title.",
                    isVerified = true
                ),
                AchievementEntity(
                    title = "FIDE International Master Norm Conferred",
                    date = "March 2026",
                    category = "FIDE Titles",
                    description = "Sofia Lin achieved her second official IM norm following an exceptional 2490 tournament performance.",
                    isVerified = true
                ),
                AchievementEntity(
                    title = "Ranked #1 Regional Youth Training Program",
                    date = "January 2026",
                    category = "Academy Milestone",
                    description = "Honored by the Regional Chess Federation for excellence in junior development and sportsmanship.",
                    isVerified = true
                ),
                AchievementEntity(
                    title = "Metropolitan Team League Gold Champions",
                    date = "November 2025",
                    category = "Championships",
                    description = "The Grandmaster Academy elite team completed an 8-0 undefeated sweep in the inter-city league.",
                    isVerified = true
                )
            )
            dao.insertAchievements(achievements)

            // Seed initial Students
            val students = listOf(
                StudentEntity(
                    fullName = "Liam Sterling",
                    email = "liam.sterling@example.com",
                    phone = "+1 (555) 234-5678",
                    chessRating = 1540,
                    experienceLevel = "Intermediate",
                    preferredMode = "Online",
                    enrolledCoursesCount = 2,
                    trialSessionStatus = "Completed"
                ),
                StudentEntity(
                    fullName = "Chloe Davenport",
                    email = "chloe.d@example.com",
                    phone = "+1 (555) 876-5432",
                    chessRating = 1820,
                    experienceLevel = "Advanced",
                    preferredMode = "Hybrid",
                    enrolledCoursesCount = 1,
                    trialSessionStatus = "Approved"
                ),
                StudentEntity(
                    fullName = "Noah Patel",
                    email = "noah.patel@example.com",
                    phone = "+1 (555) 345-6789",
                    chessRating = 1180,
                    experienceLevel = "Beginner",
                    preferredMode = "Offline",
                    enrolledCoursesCount = 1,
                    trialSessionStatus = "Confirmed"
                )
            )
            dao.insertStudents(students)

            // Seed sample Trial Bookings
            val bookings = listOf(
                TrialBookingEntity(
                    studentName = "Arthur Pendelton",
                    email = "arthur.p@example.com",
                    phone = "+1 (555) 901-2345",
                    program = "Beginner Foundations: Rules, Vision & Fundamentals",
                    preferredDate = "2026-05-02",
                    timeSlot = "10:00 AM - 11:30 AM",
                    mode = "Online",
                    message = "Looking to build sound habits and stop blundering pieces.",
                    status = "Approved"
                ),
                TrialBookingEntity(
                    studentName = "Seraphina Moore",
                    email = "seraphina@example.com",
                    phone = "+1 (555) 890-1234",
                    program = "Dynamic Middlegame Strategy & Pawn Structures",
                    preferredDate = "2026-05-04",
                    timeSlot = "2:00 PM - 3:30 PM",
                    mode = "Hybrid",
                    message = "Rated 1450 on chess.com, stuck at plateau.",
                    status = "Pending"
                )
            )
            dao.insertBookings(bookings)

            // Seed sample Enquiries
            val enquiries = listOf(
                EnquiryEntity(
                    name = "Julian Thorne",
                    email = "julian.t@example.com",
                    phone = "+1 (555) 789-0123",
                    subject = "Corporate Team Building Chess Workshop",
                    message = "We have a group of 20 engineers interested in a 2-day strategic thinking workshop.",
                    status = "Contacted",
                    adminNotes = "Sent corporate brochure; follow up scheduled for Friday."
                ),
                EnquiryEntity(
                    name = "Maya Kowalski",
                    email = "maya.k@example.com",
                    phone = "+1 (555) 678-9012",
                    subject = "1-on-1 Grandmaster Coaching Inquiry",
                    message = "Interested in weekly private sessions with GM Vance for my 12-year-old daughter.",
                    status = "New",
                    adminNotes = ""
                )
            )
            dao.insertEnquiries(enquiries)
        }
    }
}
