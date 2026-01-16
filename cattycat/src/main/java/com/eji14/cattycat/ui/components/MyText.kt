package com.eji14.cattycat.ui.components

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

data class MyTextData(
    val style: TextStyle,
    val color: Color = Color.Unspecified,
    val autoSize: TextAutoSize? = null,
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontStyle: FontStyle? = null,
    val fontWeight: FontWeight? = null,
    val fontFamily: FontFamily? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val textDecoration: TextDecoration? = null,
    val textAlign: TextAlign? = null,
    val lineHeight: TextUnit = TextUnit.Unspecified,
    val overflow: TextOverflow = TextOverflow.Clip,
    val softWrap: Boolean = true,
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1
)


@Composable
fun MyText(
    text: String,
    data: MyTextData,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = data.style,
        color = data.color,
        autoSize = data.autoSize,
        fontSize = data.fontSize,
        fontStyle = data.fontStyle,
        fontWeight = data.fontWeight,
        fontFamily = data.fontFamily,
        letterSpacing = data.letterSpacing,
        textDecoration = data.textDecoration,
        textAlign = data.textAlign,
        lineHeight = data.lineHeight,
        overflow = data.overflow,
        softWrap = data.softWrap,
        maxLines = data.maxLines,
        minLines = data.minLines
    )
}