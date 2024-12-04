import x.kotlin.commons.serialize.JSON.toJson
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Point(var x: Double = 0.0, var y: Double = 0.0)

fun main() {

    // basic info
    val cx = 100.0
    val cy = 100.0
    val R = 100.0
    val radiusRatio = sin(Math.toRadians(18.0)) / sin(Math.toRadians(126.0))
    val r = R * radiusRatio
    val degree = 2 * PI / 5

    // compute points
    val p1 = Point()
    val p2 = Point()
    val p3 = Point()
    val p4 = Point()
    val p5 = Point()
    val p6 = Point()
    val p7 = Point()
    val p8 = Point()
    val p9 = Point()
    val p10 = Point()
    p1.x = cx
    p1.y = cy - R
    p2.x = cx + r * sin(degree * 0.5)
    p2.y = cy - r * cos(degree * 0.5)
    p3.x = cx + R * sin(degree)
    p3.y = cy - R * cos(degree)
    p4.x = cx + r * cos(degree * 1.5 - PI / 2)
    p4.y = cy + r * sin(degree * 1.5 - PI / 2)
    p5.x = cx + R * cos(degree * 2 - PI / 2)
    p5.y = cy + R * sin(degree * 2 - PI / 2)
    p6.x = cx
    p6.y = cy + r
    p7.x = cx - R * cos(degree * 2 - PI / 2)
    p7.y = cy + R * sin(degree * 2 - PI / 2)
    p8.x = cx - r * cos(degree * 1.5 - PI / 2)
    p8.y = cy + r * sin(degree * 1.5 - PI / 2)
    p9.x = cx - R * sin(degree)
    p9.y = cy - R * cos(degree)
    p10.x = cx - r * sin(degree * 0.5)
    p10.y = cy - r * cos(degree * 0.5)

    // print points
    val points = listOf(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
    println(points.toJson())

    // print size
    val width = (p3.x - p9.x).toFloat()
    val height = (p7.y - p1.y).toFloat()
    val ratio = height / width
    println("cx=$cx cy=$cy r=$r width=$width height=$height ratio=$ratio")

    // print sides
    val sides = mutableListOf<List<FloatArray>>()
    for (i in 0 until points.size) {
        val first = points[i]
        val second = points[if (i == points.size - 1) 0 else i + 1]
        val pointPair = listOf(
            floatArrayOf(first.x.toFloat(), first.y.toFloat()),
            floatArrayOf(second.x.toFloat(), second.y.toFloat())
        )
        sides.add(pointPair)
    }
    print(sides.toJson())
}