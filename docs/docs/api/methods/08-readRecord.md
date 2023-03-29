---
title: readRecord
---

# `readRecord`

Retrieves a single record of the specified type.

# Method

```ts
function readRecord<T extends RecordType>(
  recordType: T,
  recordId: string
): Promise<RecordResult<T>>;
```

# Example

```ts
import { readRecord } from 'react-native-health-connect';

const readSampleDataSingle = () => {
  readRecord(
    'ActiveCaloriesBurned',
    'a7bdea65-86ce-4eb2-a9ef-a87e6a7d9df2'
  ).then((result) => {
    console.log('Retrieved record: ', JSON.stringify({ result }, null, 2));
  });
};
```
