package awm.dev.volume8d_vuvqnphuc.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ButtonCommonContent(
    paddingHorizontal: Int,
    paddingVertical: Int,
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(color = Color.Black)
            .clickable { onClick() }
            .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}