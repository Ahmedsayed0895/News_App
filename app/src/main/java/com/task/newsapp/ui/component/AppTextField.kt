package com.task.newsapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.task.newsapp.ui.theme.IconsGray
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White

@Composable
fun AppTextField(
    value: String,
    placeholderText: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    ) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text(placeholderText) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "leadingIcon"
            )
        },
        shape = RoundedCornerShape(24.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = PrimaryBlue,
            unfocusedTextColor = PrimaryBlue,
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            unfocusedLabelColor = IconsGray,
            focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
            unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.2f),
            focusedLeadingIconColor = PrimaryBlue,
            unfocusedLeadingIconColor = IconsGray,
            focusedTrailingIconColor = PrimaryBlue,
            unfocusedTrailingIconColor = IconsGray,
            cursorColor = PrimaryBlue,
        ),
    )
}