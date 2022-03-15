package ru.apps_point.sdk

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    styles: TextFieldStyles = EnhancedTextFieldDefaults.styles(),
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    clearIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    paddings: TextFieldPaddings = EnhancedTextFieldDefaults.paddings()
) {
    var focused by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Box {
            val defaultContentPadding = if (label == null) {
                TextFieldDefaults.textFieldWithoutLabelPadding()
            } else {
                TextFieldDefaults.textFieldWithLabelPadding()
            }
            val contentPadding = PaddingValues(
                start = if (paddings.textHorizontalPadding != Dp.Unspecified)
                    paddings.textHorizontalPadding
                else
                    defaultContentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                top = if (paddings.textTopPadding != Dp.Unspecified)
                    paddings.textTopPadding
                else
                    defaultContentPadding.calculateTopPadding(),
                end = if (paddings.textHorizontalPadding != Dp.Unspecified)
                    paddings.textHorizontalPadding
                else
                    defaultContentPadding.calculateRightPadding(LayoutDirection.Ltr),
                bottom = if (paddings.textBottomPadding != Dp.Unspecified)
                    paddings.textBottomPadding
                else
                    defaultContentPadding.calculateBottomPadding(),
            )
            ru.apps_point.sdk.text_field_impl.TextField(
                value,
                onValueChange,
                modifier.onFocusChanged { focused = it.isFocused },
                enabled,
                readOnly,
                styles.textStyle,
                label?.let {
                    @Composable {
                        Text(
                            text = it,
                            style = styles.labelStyle(focused, value.isEmpty())
                        )
                    }
                },
                placeholder?.let {
                    @Composable { Text(text = it, style = styles.placeholderStyle) }
                },
                leadingIcon,
                clearIcon?.let { if (value.isNotEmpty()) it else null } ?: trailingIcon,
                error != null,
                visualTransformation,
                keyboardOptions,
                keyboardActions,
                singleLine,
                maxLines,
                interactionSource,
                shape,
                colors,
                contentPadding
            )
        }
        error?.takeIf { isError }?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = it,
                style = styles.errorStyle.merge(
                    TextStyle(
                        color = colors.indicatorColor(
                            enabled = enabled,
                            isError = true,
                            interactionSource = interactionSource
                        ).value
                    )
                )
            )
        }
    }
}

class TextFieldStyles internal constructor(
    val textStyle: TextStyle,
    val initialLabelStyle: TextStyle,
    val focusedLabelStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val errorStyle: TextStyle,
) {
    fun labelStyle(focused: Boolean, valueIsEmpty: Boolean) =
        if (!focused && valueIsEmpty) initialLabelStyle else focusedLabelStyle
}

class TextFieldPaddings internal constructor(
    val textHorizontalPadding: Dp,
    val textTopPadding: Dp,
    val textBottomPadding: Dp,
    val iconHorizontalPadding: Dp,
)

object EnhancedTextFieldDefaults {

    @Composable
    fun styles(
        textStyle: TextStyle = LocalTextStyle.current,
        initialLabelStyle: TextStyle = LocalTextStyle.current,
        focusedLabelStyle: TextStyle = LocalTextStyle.current,
        placeholderStyle: TextStyle = LocalTextStyle.current,
        errorStyle: TextStyle = LocalTextStyle.current,
    ) = TextFieldStyles(
        textStyle,
        initialLabelStyle,
        focusedLabelStyle,
        placeholderStyle,
        errorStyle
    )

    fun paddings(
        textHorizontalPadding: Dp = Dp.Unspecified,
        textTopPadding: Dp = Dp.Unspecified,
        textBottomPadding: Dp = Dp.Unspecified,
        iconHorizontalPadding: Dp = Dp.Unspecified
    ) = TextFieldPaddings(
        textHorizontalPadding,
        textTopPadding,
        textBottomPadding,
        iconHorizontalPadding
    )
}