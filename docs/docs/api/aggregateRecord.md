---
sidebar_position: 6
title: aggregateRecord
---

# `aggregateRecord`

Reads aggregated result according to requested read criteria, for e.g, data origin filter and within a time range. Some record types do not support aggregation.

# Method

```ts
aggregateRecord<T extends AggregateResultRecordType>(
    request: AggregateRequest<T>
): Promise<AggregateResult<T>>
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
