---
title: deleteRecordsByTimeRange
---

# `deleteRecordsByTimeRange`

Deletes one or more record by their identifiers. Deletion of multiple record is executed in single transaction - if one fails, none is deleted.

# Method

```ts
deleteRecordsByTimeRange(
    recordType: RecordType,
    timeRangeFilter: TimeRangeFilter
): Promise<void>
```

# Example

```ts
import { deleteRecordsByTimeRange } from 'react-native-health-connect';

// ...
deleteRecordsByTimeRange('ActiveCaloriesBurned', {
  operator: 'between',
  startTime: '2023-01-09T12:00:00.405Z',
  endTime: '2023-01-09T23:53:15.405Z',
});
```
