package com.example.pianocoverbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pianocoverbook.ui.theme.PianoCoverBookTheme

// 以下、リファクタリング後のコード
class RecordFragment : Fragment() {
    private val viewModel: MusicInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RecordScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RecordScreen(viewModel: MusicInfoViewModel) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_default))
    ) {
        var textOfMusic by rememberSaveable { mutableStateOf("") }
        var textOfArtist by rememberSaveable { mutableStateOf("") }
        var textOfMemo by rememberSaveable { mutableStateOf("") }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))
        InputRow(
            label = stringResource(id = R.string.music_name),
            placeholder = stringResource(id = R.string.placeholder_music),
            onValueChange = { textOfMusic = it }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8)))
        InputRow(
            label = stringResource(id = R.string.artist_name),
            placeholder = stringResource(id = R.string.placeholder_artist),
            onValueChange = { textOfArtist = it }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8)))
        InputRow(
            label = stringResource(id = R.string.memo_name),
            placeholder = stringResource(id = R.string.placeholder_memo),
            onValueChange = { textOfMemo = it }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8)))
        ProgressSection(stringResource(id = R.string.right_hand)) { RightHandCircularProgressWithSeekBar() }
        ProgressSection(stringResource(id = R.string.left_hand)) { LeftHandCircularProgressWithSeekBar() }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SaveRecordButton(
                    onClick = {
                        viewModel.saveValues(
                            textOfMusic,
                            textOfArtist,
                            textOfMemo
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: MusicInfoViewModel
) {
    PianoCoverBookTheme {
        RecordScreen(viewModel = viewModel)
    }
}

@Composable
private fun InputRow(label: String, placeholder: String, onValueChange: (String) -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
        Text(text = label)
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.text_field_height)),
            value = "",
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(fontSize = dimensionResource(id = R.dimen.text_size_normal).value.sp),
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(10),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
    }
}

@Composable
private fun ProgressSection(label: String, progressContent: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label)
        progressContent()
    }
}

@Composable
private fun CircularProgressWithSeekBar(progress: MutableState<Float>) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.size(dimensionResource(id = R.dimen.circular_progress_with_seek_bar_size))) {
        CircularProgressIndicator(
            color = Color.Blue,
            strokeWidth = dimensionResource(id = R.dimen.circular_progress_indicator_stroke_width),
            progress = progress.value / 100,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.circular_progress_with_seek_bar_size))
                .padding(dimensionResource(id = R.dimen.space_8))
        )
        Text(
            text = "${progress.value.toInt()}%",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp),
        )
    }
    Slider(
        colors = SliderDefaults.colors(
            activeTrackColor = Color.Blue,
            inactiveTrackColor = Color.Gray,
            thumbColor = Color.Blue
        ),
        value = progress.value,
        onValueChange = { newValue -> progress.value = newValue },
        valueRange = 0f..100f,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16))
    )
}

@Composable
private fun RightHandCircularProgressWithSeekBar() {
    val rightHandProgress = rememberSaveable { mutableFloatStateOf(0f) }
    CircularProgressWithSeekBar(progress = rightHandProgress)
}

@Composable
private fun LeftHandCircularProgressWithSeekBar() {
    val leftHandProgress = rememberSaveable { mutableFloatStateOf(0f) }
    CircularProgressWithSeekBar(progress = leftHandProgress)
}

@Composable
private fun SaveRecordButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Blue)
    ) {
        Text(stringResource(id = R.string.record_button))
    }
}