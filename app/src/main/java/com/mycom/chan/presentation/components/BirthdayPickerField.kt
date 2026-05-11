package com.mycom.chan.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayPickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Birthday"
) {
    var showPicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // Convert stored "yyyy-MM-dd" → display "dd/MM/yyyy" by splitting the string (no timezone needed)
    val displayValue = remember(value) {
        if (value.length == 10) {
            val parts = value.split("-")
            if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else value
        } else value
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) showPicker = true
        }
    }

    OutlinedTextField(
        value = displayValue,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text("DD/MM/YYYY") },
        singleLine = true,
        readOnly = true,
        interactionSource = interactionSource,
        modifier = modifier.fillMaxWidth()
    )

    if (showPicker) {
        val nowMillis = System.currentTimeMillis()
        // DatePicker uses UTC midnight millis — always parse/format in UTC to avoid off-by-one day
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val initialMillis = remember(value) {
            if (value.length == 10) runCatching { fmt.parse(value)?.time }.getOrNull() else null
        }
        val state = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= nowMillis
            }
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onValueChange(fmt.format(Date(it))) }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
