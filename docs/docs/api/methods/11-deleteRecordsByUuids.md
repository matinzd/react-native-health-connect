---
title: deleteRecordsByUuids
---

# `deleteRecordsByUuids`

Deletes one or more Record by their identifiers. Deletion of multiple Record is executed in single transaction - if one fails, none is deleted.

# Method

```ts
deleteRecordsByUuids(
    recordType: RecordType,
    recordIdsList: string[],
    clientRecordIdsList: string[] = []
): Promise<void>
```

# Example

```ts
import { deleteRecordsByUuids } from 'react-native-health-connect';

// ...
deleteRecordsByUuids('ActiveCaloriesBurned', [
  '47066a7c-8994-4caf-b031-c54b2b6a2023',
  '2279fa69-683b-44c7-8cfa-930fb5fc64eb',
]);
```
