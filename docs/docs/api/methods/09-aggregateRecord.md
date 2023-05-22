---
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
import { aggregateRecord } from 'react-native-health-connect';

const aggregateSampleData = () => {
  aggregateRecord({
    recordType: 'ActiveCaloriesBurned',
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z',
      endTime: '2023-01-09T23:53:15.405Z',
    },
  }).then((result) => {
    console.log('Aggregated record: ', { result }); // Aggregated record:  {"result": {"dataOrigins": ["com.healthconnectexample"], "inCalories": 15000000, "inJoules": 62760000.00989097, "inKilocalories": 15000, "inKilojoules": 62760.00000989097}}
  });
};
```
