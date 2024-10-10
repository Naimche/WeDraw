package com.bupware.wedraw.android.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.theme.blueVariant2WeDraw

//region MainScreen

@Composable
fun TextFieldJoin(modificador: Modifier){

    Column(modifier = modificador, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = "aa",
            onValueChange = {},
            maxLines = 1,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = blueVariant2WeDraw,
                backgroundColor = Color.LightGray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

}

@Composable
fun TextFieldUsername(value:String,onValueChange:(String)->Unit){
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = value,
            onValueChange = {onValueChange(it)},
            maxLines = 1,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = blueVariant2WeDraw,
                backgroundColor = Color.LightGray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}
//endregion


@Composable
fun TextFieldMessage(value: String,onValueChange: (String) -> Unit, placeholder:String){


    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF0F0F0), RoundedCornerShape(15.dp))
        .padding(0.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        TextFieldNoPadding(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            placeholder = { Text(text = placeholder, color = Color.Black) },
            onValueChange = {onValueChange(it)},
            maxLines = 6,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = blueVariant2WeDraw,
                backgroundColor = Color(0xFFE4E4E4),
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = Color.Black

            )
        )
    }


}

@Composable
fun CreateGroupTextfield(value: String, onValueChange: (String) -> Unit){
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), RoundedCornerShape(10.dp))
            .padding(6.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {onValueChange(createGroupFilter(it))},
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.introduce_el_nombre_del_grupo),
                            color = Color(0xFF696969)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

fun createGroupFilter(text: String): String {
    val alphanumericText = text.filter { it.isLetterOrDigit() || it.isWhitespace() }
    return alphanumericText.take(20)
}

fun joinGroupFilter(text: String): String {
    val alphanumericText = text.filter { it.isLetterOrDigit() }
    val uppercaseText = alphanumericText.uppercase()
    return uppercaseText.take(6)
}

@Composable
fun JoinGroupTextfield(value: String, onValueChange: (String) -> Unit){
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), RoundedCornerShape(10.dp))
            .padding(6.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {onValueChange(joinGroupFilter(it))},
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.introduce_el_c_digo),
                            color = Color(0xFF696969)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun TextFieldNoPadding(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(15.dp),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))


    @OptIn(ExperimentalMaterialApi::class)
    BasicTextField(
        value = value,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon

            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = PaddingValues(10.dp)
            )

        }

    )


}