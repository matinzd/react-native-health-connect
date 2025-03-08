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
      })

const {
  upsertionChanges, // Array<{ record: HealthConnectRecord }>
  deletionChanges, // Array<{ recordId: string }>
  nextChangesToken, // string
  changesTokenExpired, // boolean
  hasMore // boolean
} = changeRes
```


**Note:** `upsertionChanges` includes both updated existing records, and new records since last change token fetch

Changes tokens are only valid for 30 days. Ensure your app does the following:

- It regularly updates data for any changes within 30 days or less to avoid stale tokens.
- It handles cases where the token is no longer valid.
- It must have a fallback mechanism for obtaining the necessary data. ()

