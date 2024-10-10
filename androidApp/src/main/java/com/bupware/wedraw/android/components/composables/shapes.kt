package com.bupware.wedraw.android.components.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density


val TriangleShape = GenericShape { size, _ ->
    // 1)
    moveTo(size.width / 2f, 0f)

    // 2)
    lineTo(size.width, size.height)

    // 3)
    lineTo(0f, size.height)
}

class RightBubbleShape(
    private val arrow: Boolean = true,
    private val cornerShape: Float,
    private val arrowWidth: Float,
    private val arrowHeight: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            reset()


            // 1. Move to x = cornerShape (16), y = 0
            moveTo(cornerShape, 0f)

            // 2. Draw a line till x = composableWidth + arrowWidth and y = 0
            lineTo(size.width + arrowWidth, 0f)


            // 3. From the above animation we can see that we need to draw an arc,
            // for that we will need to reach top left to draw a rectangle.

            //So we move to rect top left = [x = composable width + arrow width] and y = 0

            if (arrow) {
                arcTo(
                    rect = Rect(
                        offset = Offset(size.width + arrowWidth, 0f),
                        size = Size(10f, 10f)
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
            }



            // 4. Now draw the slanting line
            lineTo(size.width, arrowHeight)

            // 5. Move to bottom now.
            lineTo(size.width, size.height - cornerShape)


            // 6. Again draw the bottom left arc pointing the top left x & y coordinates
            arcTo(
                rect = Rect(
                    offset = Offset(size.width - cornerShape, size.height - cornerShape),
                    size = Size(cornerShape, cornerShape)
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // 7. Now draw the bottom line from left to right side
            lineTo(size.width - cornerShape, size.height)

            // 8. Again draw the bottom right arc pointing the top left x & y coordinates
            arcTo(
                rect = Rect(
                    offset = Offset(0f, size.height - cornerShape),
                    size = Size(cornerShape, cornerShape)
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )


            //9. Draw the bottom to top line on right side
            lineTo(0f, cornerShape)

            //Draw the final top right arc
            arcTo(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = Size(cornerShape, cornerShape)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )


            close()
        })
    }

}

fun Modifier.drawRightBubble(
    bubbleColor: Color,
    cornerShape: Float,
    arrowWidth: Float,
    arrowHeight: Float,
    arrow:Boolean = true
) = then(
    background(
        color = bubbleColor,
        shape = RightBubbleShape(
            arrow = arrow,
            cornerShape = cornerShape,
            arrowWidth = arrowWidth,
            arrowHeight = arrowHeight
        )
    )
)


