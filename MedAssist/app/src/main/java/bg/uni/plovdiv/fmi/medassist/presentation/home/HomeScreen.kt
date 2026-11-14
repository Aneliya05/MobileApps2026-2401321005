package bg.uni.plovdiv.fmi.medassist.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bg.uni.plovdiv.fmi.medassist.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToGuide: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToKit: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            CustomBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        1 -> onNavigateToGuide()
                        2 -> onNavigateToMap()
                        3 -> onNavigateToContacts()
                        4 -> onNavigateToSettings()
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x26E53935), Color.Transparent),
                        center = Offset(900f, 100f),
                        radius = 700f
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // 1. HEADER
                item {
                    HeaderSection(onSettingsClick = onNavigateToSettings)
                }

                // 2. EMERGENCY 112 BUTTON
                item {
                    EmergencyCallButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                            context.startActivity(intent)
                        }
                    )
                }

                // 3. STATUS BANNER
                item {
                    SafetyBanner()
                }

                // 4. QUICK ACCESS (2x2 Grid)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Quick Access",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            QuickAccessCard(
                                title = "First Aid Guide",
                                subtitle = "Learn what to do in emergencies",
                                icon = Icons.AutoMirrored.Outlined.MenuBook,
                                iconTint = EmergencyRedLight,
                                onClick = onNavigateToGuide,
                                modifier = Modifier.weight(1f)
                            )
                            QuickAccessCard(
                                title = "Medical Map",
                                subtitle = "Find nearby hospitals, pharmacies & more",
                                icon = Icons.Outlined.LocationOn,
                                iconTint = EmergencyRedLight,
                                onClick = onNavigateToMap,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            QuickAccessCard(
                                title = "Emergency Contacts",
                                subtitle = "Manage important contacts",
                                icon = Icons.Outlined.Person,
                                iconTint = EmergencyRedLight,
                                onClick = onNavigateToContacts,
                                modifier = Modifier.weight(1f)
                            )
                            QuickAccessCard(
                                title = "First Aid Kit",
                                subtitle = "Check your kit checklist",
                                icon = Icons.Outlined.Checklist,
                                iconTint = EmergencyRedLight,
                                onClick = onNavigateToKit,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // 5. HELPFUL TIPS BANNER
                item {
                    HelpfulTipsBanner(onClick = onNavigateToGuide)
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun HeaderSection(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "App Logo",
                tint = EmergencyRedLight,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Medical\nAssistant",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 24.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Be prepared. Stay safe.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(42.dp)
                .background(IconLightGrayBg, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color(0xFF444444)
            )
        }
    }
}

@Composable
fun EmergencyCallButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(EmergencyRedLight, EmergencyRedDark)
                )
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Call 112",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Emergency – 112",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap to call emergency services",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun SafetyBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftPinkBanner)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFAD2D2), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VerifiedUser,
                        contentDescription = null,
                        tint = EmergencyRedLight,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "You're safe!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Stay informed and be prepared for any situation.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.MedicalServices,
                contentDescription = "First Aid Kit",
                tint = EmergencyRedLight,
                modifier = Modifier.size(44.dp)
            )
        }
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(105.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 17.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun HelpfulTipsBanner(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFFFF3CD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Helpful Tips",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Keep calm. Assess the situation. Call 112 if needed.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun CustomBottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        Pair("Home", Icons.Filled.Home),
        Pair("Guide", Icons.AutoMirrored.Outlined.MenuBook),
        Pair("Map", Icons.Outlined.LocationOn),
        Pair("Contacts", Icons.Outlined.Person),
        Pair("Settings", Icons.Outlined.Settings)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = EmergencyRedLight,
                    selectedTextColor = EmergencyRedLight,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                ),
                icon = {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.first,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .height(3.dp)
                                .width(32.dp)
                                .background(
                                    color = if (isSelected) EmergencyRedLight else Color.Transparent,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            )
        }
    }
}