---
sidebar_position: 5
title: readRecords
---

# `readRecords`

Retrieves a collection of records.

# Method

```ts
readRecords<T extends RecordType>(
    // record type e.g activeCaloriesBurned 
    recordType: T,

    // read options such as time range filter, data origin filter, ordering and pagination
    options: ReadRecordsOptions
): Promise<RecordResult<T>[]>
```

# Example

```ts
import { readRecords } from 'react-native-health-connect';

const readSampleData = () => {
  readRecords('activeCaloriesBurned', {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z',
      endTime: '2023-01-09T23:53:15.405Z',
    },
  }).then((result) => {
    console.log('Retrieved records: ', JSON.stringify({ result }, null, 2));
  });
};
```
