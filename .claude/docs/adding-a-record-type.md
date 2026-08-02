# Adding or changing a record type

The work is spread across parallel registries on both sides of the bridge. Missing one fails at
runtime, not at compile time.

Using `StepsCadence` as the reference implementation:

## Kotlin

1. `android/src/main/java/dev/matinzd/healthconnect/records/ReactStepsCadenceRecord.kt` —
   implement `ReactHealthRecordImpl<StepsCadenceRecord>`. If the type does not support
   aggregation, throw `AggregationNotSupported()` from the aggregate methods (existing records
   show the convention).
2. Register in **all three** maps in `utils/HealthConnectUtils.kt`:
   - `reactRecordTypeToClassMap` — `"StepsCadence" to StepsCadenceRecord::class`
   - `reactRecordTypeToReactClassMap` — `"StepsCadence" to ReactStepsCadenceRecord::class.java`
   - `healthConnectClassToReactClassMap` — `StepsCadenceRecord::class.java to ReactStepsCadenceRecord::class.java`

   The third is only exercised by `getChanges`, so omitting it passes every read/write test and
   breaks the change feed.

## TypeScript

3. `src/types/records.types.ts` — the record interface with its `recordType` literal, added to the
   `HealthConnectRecord` union.
4. `src/types/results.types.ts` — the `...RecordResult` interface and its union entry.
5. `src/types/aggregate.types.ts` — the aggregate result interface and union entry, if aggregable.

Reusable unit helpers (`massToJsMap`, energy, power, temperature, velocity, length …) already
exist in `utils/HealthConnectUtils.kt`; the corresponding `Invalid*` exceptions are in
`utils/ExceptionsUtils.kt`.

## Verify

`yarn typecheck` catches the TS side. The Kotlin registries are not type-checked against each
other — exercise the type through the example app (insert → read → aggregate → `getChanges`).
