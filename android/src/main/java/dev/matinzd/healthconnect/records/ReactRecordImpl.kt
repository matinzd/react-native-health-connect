package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import com.facebook.react.bridge.ReadableArray

interface ReactRecordImpl {
  fun parseReactArray(readableArray: ReadableArray): List<Record>
}
