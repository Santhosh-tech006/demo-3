package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CoachEntity
import com.example.ui.components.CoachCard
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CardBgElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LightBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun AboutScreen(
    coaches: List<CoachEntity>,
    onBookTrialClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgPrimary)
            .padding(16.dp)
            .testTag("about_screen_scroll")
    ) {
        // Page Header
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "ABOUT GRANDMASTER ACADEMY",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Forging Strategic Minds & Champions",
                    color = TextMain,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Founded with the ambition to make world-class chess training accessible, disciplined, and transformative for aspiring players of all ages.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // Mission & Vision Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Our Mission", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "To elevate student reasoning, resilience, and analytical precision through structured chess education.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = LightBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Our Vision", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "To be the premier global academy where dedicated players transition into titled FIDE competitors.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Training Methodology
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "PEDAGOGICAL PILLARS",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Our Four-Corner Training Method",
                    color = TextMain,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                MethodologyItem("1. Calculation & Candidate Discipline", "We train players to visualize 3 to 6 moves ahead while systematically evaluating counter-intuitive candidate moves before committing.")
                MethodologyItem("2. Pawn Structure Geometry", "Every opening leads to specific middlegame pawn skeletons (Carlsbad, Isolani, Hedgehog). We teach universal strategies linked to each pawn formation.")
                MethodologyItem("3. Reverse Learning (Endgames First)", "Following Capablanca's golden rule, we master simplified positions first to comprehend the true harmonic value of pieces.")
                MethodologyItem("4. Psychological Tournament Readiness", "We simulate classical clock strain, tilt recovery, and competitive time trouble management.")
            }
        }

        // 5-Stage Student Development Journey
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "THE LEARNING ROADMAP",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "From Novice to Grandmaster",
                    color = TextMain,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                JourneyStage(stage = "Stage 1", title = "Tactical Foundations (0–1200)", desc = "Board vision, piece coordination, basic checkmate patterns, eliminating unforced blunders.")
                JourneyStage(stage = "Stage 2", title = "Positional Fluency (1200–1600)", desc = "Pawn structure play, controlling open files, outposts, and opening repertoire construction.")
                JourneyStage(stage = "Stage 3", title = "Strategic Maturity (1600–1900)", desc = "Prophylactic thinking, piece sacrifices, initiative conversion, and complex rook endgames.")
                JourneyStage(stage = "Stage 4", title = "Master Class Candidate (1900–2200)", desc = "Deep dynamic calculation, opponent profiling, opening preparation against engine novelties.")
                JourneyStage(stage = "Stage 5", title = "FIDE Titled Elite (2200+)", desc = "International norm campaigns, Olympiad qualification, Grandmaster sparring sessions.")
            }
        }

        // Faculty Showcase
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "ACADEMY TRAINERS",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Meet Your Mentors",
                    color = TextMain,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        items(coaches) { coach ->
            Box(modifier = Modifier.padding(vertical = 6.dp)) {
                CoachCard(coach = coach)
            }
        }

        // Core Values
        item {
            Surface(
                color = CardBgElevated,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Our Core Values", color = TextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Intellectual Curiosity: Chess as a lifelong journey of self-discovery.", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Sportsmanship & Grace: Honor your opponent, learn relentlessly from losses.", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Uncompromising Rigor: Deep practice surpasses casual play.", color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBookTrialClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Experience Our Teaching — Book a Free Trial", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MethodologyItem(title: String, desc: String) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun JourneyStage(stage: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stage.split(" ").last(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
