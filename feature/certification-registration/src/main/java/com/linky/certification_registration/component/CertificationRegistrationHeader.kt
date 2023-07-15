package com.linky.certification_registration.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader

@Composable
internal fun CertificationRegistrationHeader(onBack: () -> Unit) {
    LinkyHeader(modifier = Modifier.padding(start = 12.dp)) {
        LinkyBackArrowButton(onBack)
    }
}