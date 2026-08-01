---
title: getChanges
---

# `getChanges`

# Method

```ts
getChanges(changeRequest:GetChangesRequest): Promise<GetChangesResults>
```

# Example

```ts
import {
  getChanges,
} from "react-native-health-connect";

const changeRes = await getChanges({
  recordTypes: ['Nutrition', 'HeartRate'], // RecordType[]
  changesToken: undefined, // string | undefined
  dataOriginFilter: [] // optional array for more specific filtering by dataOrigin
})

const {
  upsertionChanges, // Array<{ record: HealthConnectRecordResult }>
  deletionChanges, // Array<{ recordId: string }>
  nextChangesToken, // string
  changesTokenExpired, // boolean
  hasMore // boolean
} = changeRes
```

# Reading upserted records

Upserted records are serialized exactly like [`readRecords`](./07-readRecords.md) results, **not** like the objects you pass to `insertRecords`. Unit-bearing fields therefore come back as the `*Result` variants — a `Weight` record exposes `inKilograms`, `inGrams`, … rather than `{ value, unit }`:

```json
{
  "recordType": "Weight",
  "weight": {
    "inGrams": 75000,
    "inKilograms": 75,
    "inMilligrams": 75000000,
    "inMicrograms": 75000000000,
    "inOunces": 2645.5474378402178,
    "inPounds": 165.3466966386582
  }
}
```

Narrow on `recordType` to get at the typed record:

```ts
const weightsInKilograms = upsertionChanges.flatMap(({ record }) =>
  record.recordType === 'Weight' ? [record.weight.inKilograms] : []
);
```

**Note:** `upsertionChanges` includes both updated existing records, and new records since last change token fetch

Changes tokens are only valid for 30 days. Ensure your app does the following:

- It regularly updates data for any changes within 30 days or less to avoid stale tokens.
- It handles cases where the token is no longer valid.
- It must have a fallback mechanism for obtaining the necessary data. ()

