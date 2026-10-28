package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.ExerciseRouteResult
import com.facebook.react.bridge.JavaOnlyArray
import com.facebook.react.bridge.JavaOnlyMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReactExerciseSessionRecordTest {
  @Test
  fun parseWriteRecordAcceptsRoutePointWithoutAccuracy() {
    val point = JavaOnlyMap.of(
      "time", "2026-01-01T00:00:00Z",
      "latitude", 37.78,
      "longitude", -122.4,
      "altitude", JavaOnlyMap.of("value", 12.5, "unit", "meters"),
    )
    val pointWithoutAltitude = JavaOnlyMap.of(
      "time", "2026-01-01T00:01:00Z",
      "latitude", 37.79,
      "longitude", -122.41,
    )
    val record = JavaOnlyMap.of(
      "startTime", "2026-01-01T00:00:00Z",
      "endTime", "2026-01-01T00:10:00Z",
      "exerciseRoute", JavaOnlyMap.of("route", JavaOnlyArray.of(point, pointWithoutAltitude)),
    )

    val parsed = ReactExerciseSessionRecord().parseWriteRecord(JavaOnlyArray.of(record)).single()
    val route = (parsed.exerciseRouteResult as ExerciseRouteResult.Data).exerciseRoute.route

    assertEquals(2, route.size)
    assertEquals(12.5, route[0].altitude!!.inMeters, 0.0)
    assertNull(route[0].horizontalAccuracy)
    assertNull(route[0].verticalAccuracy)
    assertNull(route[1].altitude)
    assertNull(route[1].horizontalAccuracy)
    assertNull(route[1].verticalAccuracy)
  }
}
