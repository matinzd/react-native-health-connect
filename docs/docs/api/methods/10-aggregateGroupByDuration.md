---
title: aggregateGroupByDuration
---

# `aggregateGroupByDuration`

Reads aggregated group result by Duration according to requested read criteria. `timeRangeSlicer` needs to be specified for the Duration type (`MILLIS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS`) and length. Some record types do not support aggregation.

# Method

```ts
aggregateGroupByDuration<T extends AggregateResultRecordType>(
  request: AggregateGroupByDurationRequest<T>
): Promise<AggregationGroupResult<T>[]>
```

# Example

```ts
import { aggregateGroupByDuration } from 'react-native-health-connect';

const aggregateSampleData = () => {
  aggregateGroupByDuration({
    recordType: 'Steps',
    timeRangeFilter: {
      operator: 'between',
      startTime: '2024-10-04T15:00:00Z',
      endTime: '2024-10-12T14:57:39.714Z',
    },
    timeRangeSlicer: {
      duration: 'DAYS',
      length: 2,
    },
  }).then((result) => {
    console.log('Aggregated Group by Duration: ', { result }); // Aggregated record: {"result": [{"endTime": "2024-10-06T15:00:00Z", "startTime": "2024-10-04T15:00:00Z", "zoneOffset": "+09:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 3000}}, {"endTime": "2024-10-08T15:00:00Z", "startTime": "2024-10-06T15:00:00Z", "zoneOffset": "+09:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 7000}}, {"endTime": "2024-10-10T15:00:00Z", "startTime": "2024-10-08T15:00:00Z", "zoneOffset": "+09:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 11000}}, {"endTime": "2024-10-12T14:57:39.714Z", "startTime": "2024-10-10T15:00:00Z", "zoneOffset": "+09:00", "result": {"dataOrigins": [], "COUNT_TOTAL": 7000}}]}
  });
};
```
