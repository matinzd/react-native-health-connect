---
title: readRecords
---

# `readRecords`

Retrieves a collection of records.

# Method

```ts
function readRecords<T extends RecordType>(
    // record type e.g activeCaloriesBurned
    recordType: T,

    // read options such as time range filter, data origin filter, ordering and pagination
    options: ReadRecordsOptions
): Promise<ReadRecordsResult<T>>
```

# Example

```ts
import { readRecords } from 'react-native-health-connect';

const readSampleData = () => {
  readRecords('ActiveCaloriesBurned', {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z',
      endTime: '2023-01-09T23:53:15.405Z',
    },
  }).then(({ records }) => {
    console.log('Retrieved records: ', JSON.stringify({ records }, null, 2)); // Retrieved records:  {"records":[{"startTime":"2023-01-09T12:00:00.405Z","endTime":"2023-01-09T23:53:15.405Z","energy":{"inCalories":15000000,"inJoules":62760000.00989097,"inKilojoules":62760.00000989097,"inKilocalories":15000},"metadata":{"id":"239a8cfd-990d-42fc-bffc-c494b829e8e1","lastModifiedTime":"2023-01-17T21:06:23.335Z","clientRecordId":null,"dataOrigin":"com.healthconnectexample","clientRecordVersion":0,"device":0}}]}
  });
};
```
