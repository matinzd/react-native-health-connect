---
title: insertRecords
---

# `insertRecords`

Inserts one or more record and returns newly generated records' uuids. Insertion of multiple records is executed in a transaction - if one fails, none is inserted.

# Method

```ts
insertRecords(records: HealthConnectRecord[]): Promise<string[]>;
```

# Example

```ts
import { insertRecords } from 'react-native-health-connect';

const insertSampleData = () => {
  insertRecords([
    {
      recordType: 'ActiveCaloriesBurned',
      energy: { unit: 'kilocalories', value: 10000 },
      startTime: '2023-01-09T10:00:00.405Z',
      endTime: '2023-01-09T11:53:15.405Z',
      metadata: {
        recordingMethod:
          RecordingMethod.RECORDING_METHOD_AUTOMATICALLY_RECORDED,
        device: {
          manufacturer: 'Google',
          model: 'Pixel 4',
          type: DeviceType.TYPE_PHONE,
        },
      },
    },
    {
      recordType: 'ActiveCaloriesBurned',
      energy: { unit: 'kilocalories', value: 15000 },
      startTime: '2023-01-09T12:00:00.405Z',
      endTime: '2023-01-09T23:53:15.405Z',
    },
  ]).then((ids) => {
    console.log('Records inserted ', { ids }); // Records inserted  {"ids": ["06bef46e-9383-4cc1-94b6-07a5045b764a", "a7bdea65-86ce-4eb2-a9ef-a87e6a7d9df2"]}
  });
};
```
