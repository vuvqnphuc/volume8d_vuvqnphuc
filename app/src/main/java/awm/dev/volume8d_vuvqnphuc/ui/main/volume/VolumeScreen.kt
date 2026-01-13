package awm.dev.volume8d_vuvqnphuc.ui.main.volume

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VolumeScreen (){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Volume ")

    }
}
@Composable
@Preview(showSystemUi = true)
fun VolumeScreenP(){
    VolumeScreen()
}