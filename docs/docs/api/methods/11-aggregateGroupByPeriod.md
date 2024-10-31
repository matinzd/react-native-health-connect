---
title: aggregateGroupByPeriod
---

# `aggregateGroupByPeriod`

Reads aggregated group result by Period according to requested read criteria. `timeRangeSlicer` needs to be specified for the Period type (`'DAYS' | 'WEEKS' | 'MONTHS' | 'YEARS'`) and length. `Period` is date-based amount of time as opposed to `Duration`, which is a fixed length of time. Some record types do not support aggregation.

# Method

```ts
aggregateGroupByPeriod<T extends AggregateResultRecordType>(
  request: AggregateGroupByPeriodRequest<T>
): Promise<AggregationGroupResult<T>[]>
```

# Example

```ts
import { aggregateGroupByPeriod } from 'react-native-health-connect';

const aggregateSampleData = () => {
  aggregateGroupByPeriod({
    recordType: 'Steps',
    timeRangeFilter: {
      operator: 'between',
      startTime: '2024-09-03T15:00',
      endTime: '2024-09-11T10:50:12.182',
    },
    timeRangeSlicer: {
      period: 'DAYS',
      length: 1,
    },
  }).then((result) => {
    console.log('Aggregated Group by Period: ', { result }); // Aggregated record: {"result": [{"endTime": "2024-09-04T15:00", "startTime": "2024-09-03T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 1000}}, {"endTime": "2024-09-05T15:00", "startTime": "2024-09-04T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 2000}}, {"endTime": "2024-09-06T15:00", "startTime": "2024-09-05T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 3000}}, {"endTime": "2024-09-07T15:00", "startTime": "2024-09-06T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 4000}}, {"endTime": "2024-09-08T15:00", "startTime": "2024-09-07T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 5000}}, {"endTime": "2024-09-09T15:00", "startTime": "2024-09-08T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 6000}}, {"endTime": "2024-09-10T15:00", "startTime": "2024-09-09T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 7000}}, {"endTime": "2024-09-11T10:50:12.182", "startTime": "2024-09-10T15:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 0}}]}
  });
};
```
