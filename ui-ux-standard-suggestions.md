# Smart Home Design Standards Suggestions- 2024 Update

## 1. Color System

### 1.1 Primary Colors
```kotlin
object SmartColors {
    val PrimaryGradientStart = Color(0xFFB7A8F8)  // Lighter purple
    val PrimaryGradientEnd = Color(0xFF7052F2)    // Deeper purple
    val SecondaryColor = Color(0xFFF8F7FD)        // Very soft purple for cards
    val AccentColor = Color(0xFF7052F2).copy(alpha = 0.08f)  // Ultra-soft purple for interactive elements
    val Background = Color.White
    val TextPrimary = Color.Black
    val TextSecondary = Color.Gray
}
```

### 1.2 Status Colors
```kotlin
object StatusColors {
    val Success = Color(0xFF4CAF50)      // Green for positive states
    val Warning = Color(0xFFFFA000)      // Amber for warnings
    val Error = Color(0xFFF44336)        // Red for errors
    val Info = Color(0xFF2196F3)         // Blue for information
}
```

### 1.3 Color Usage Guidelines
- Use white background for main screens
- Apply SecondaryColor for cards and containers
- Use AccentColor for interactive elements backgrounds
- Apply gradient (PrimaryGradientStart to PrimaryGradientEnd) for primary actions
- Use status colors sparingly and only for their intended purposes

## 2. Typography

### 2.1 Type Scale
```kotlin
object SmartTypography {
    val headingLarge = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    val headingMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    val headingSmall = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )
    val bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
    val bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    val bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
    val caption = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal
    )
}
```

### 2.2 Typography Usage
- Use headingLarge for screen titles
- Use headingMedium for section headers
- Use bodyLarge for primary content
- Use bodyMedium for secondary content and labels
- Use bodySmall for supporting text
- Use caption for timestamps and supplementary information

## 3. Spacing & Dimensions

### 3.1 Base Measurements
```kotlin
object SmartDimensions {
    // Corner Radius
    val cornerRadiusLarge = 24.dp
    val cornerRadiusMedium = 20.dp
    val cornerRadiusSmall = 16.dp
    
    // Padding
    val paddingLarge = 32.dp
    val paddingMedium = 16.dp
    val paddingSmall = 8.dp
    val paddingTiny = 4.dp
    
    // Component Sizes
    val buttonHeight = 66.dp
    val iconSize = 24.dp
    val iconSizeLarge = 32.dp
    val cardWidth = 160.dp
    val cardHeightSmall = 120.dp
    val cardHeightMedium = 180.dp
    val cardHeightLarge = 240.dp
}
```

### 3.2 Layout Guidelines
- Use consistent spacing between elements
- Maintain proper hierarchy through spacing
- Apply appropriate padding based on container size
- Use consistent corner radius for similar components

## 4. Component Standards

### 4.1 Cards
```kotlin
val standardCardModifier = Modifier
    .fillMaxWidth()
    .clip(RoundedCornerShape(SmartDimensions.cornerRadiusLarge))
    .background(SmartColors.SecondaryColor)
```

### 4.2 Buttons
```kotlin
// Primary Button (Gradient)
val primaryButtonModifier = Modifier
    .height(SmartDimensions.buttonHeight)
    .clip(RoundedCornerShape(SmartDimensions.cornerRadiusLarge))
    .background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                SmartColors.PrimaryGradientStart,
                SmartColors.PrimaryGradientEnd
            )
        )
    )

// Icon Button
val iconButtonModifier = Modifier
    .size(48.dp)
    .clip(CircleShape)
    .background(SmartColors.AccentColor)
```

### 4.3 Input Fields
```kotlin
val inputFieldColors = TextFieldDefaults.colors(
    focusedContainerColor = SmartColors.SecondaryColor,
    unfocusedContainerColor = SmartColors.SecondaryColor,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    focusedLabelColor = SmartColors.PrimaryGradientEnd,
    unfocusedLabelColor = SmartColors.TextSecondary
)
```

## 5. Interactive Elements

### 5.1 Switches and Toggles
```kotlin
val switchColors = SwitchDefaults.colors(
    checkedThumbColor = SmartColors.PrimaryGradientEnd,
    checkedTrackColor = SmartColors.PrimaryGradientStart,
    uncheckedThumbColor = SmartColors.TextSecondary,
    uncheckedTrackColor = SmartColors.TextSecondary.copy(alpha = 0.5f)
)
```

### 5.2 Sliders
```kotlin
val sliderColors = SliderDefaults.colors(
    thumbColor = SmartColors.PrimaryGradientEnd,
    activeTrackColor = SmartColors.PrimaryGradientEnd,
    inactiveTrackColor = SmartColors.AccentColor
)
```

## 6. Navigation

### 6.1 Top App Bar
```kotlin
val topAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = SmartColors.Background,
    titleContentColor = SmartColors.TextPrimary,
    navigationIconContentColor = SmartColors.TextPrimary,
    actionIconContentColor = SmartColors.TextPrimary
)
```

### 6.2 Bottom Navigation
```kotlin
val bottomNavColors = NavigationBarItemDefaults.colors(
    selectedIconColor = SmartColors.PrimaryGradientEnd,
    selectedTextColor = SmartColors.PrimaryGradientEnd,
    unselectedIconColor = SmartColors.TextSecondary,
    unselectedTextColor = SmartColors.TextSecondary,
    indicatorColor = Color.Transparent
)
```

## 7. Device-Specific Components

### 7.1 Status Indicators
- Use consistent icons for similar functions across devices
- Implement clear visual feedback for device states
- Use status colors appropriately for different states

### 7.2 Control Patterns
- Maintain consistent control layouts for similar functions
- Use standard iconography for common actions
- Implement clear visual hierarchy in control groups

## 8. Animation Standards

### 8.1 Transitions
```kotlin
val standardTransitionSpec = tween<Float>(
    durationMillis = 300,
    easing = FastOutSlowInEasing
)
```

### 8.2 Interaction Feedback
- Provide immediate visual feedback for user actions
- Use subtle animations for state changes
- Maintain consistent animation patterns across the app

## 9. Accessibility Guidelines

### 9.1 Touch Targets
- Minimum touch target size: 48.dp x 48.dp
- Adequate spacing between interactive elements
- Clear visual feedback for interactive elements

### 9.2 Text Contrast
- Maintain minimum contrast ratios for all text
- Use appropriate text sizes for readability
- Provide clear visual hierarchy through typography

## 10. Implementation Notes

### 10.1 Code Organization
- Use composable functions for reusable components
- Maintain consistent naming conventions
- Implement proper state management
- Use remember and mutableStateOf for local state
- Follow Compose best practices for performance

### 10.2 Performance Considerations
- Minimize recomposition scope
- Use proper key usage in lists
- Implement efficient state management
- Optimize layouts for performance
