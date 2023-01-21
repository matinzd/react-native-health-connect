import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import {
  aggregateRecord,
  initialize,
  insertRecords,
  isAvailable,
  readRecords,
  requestPermission,
  revokeAllPermissions,
} from 'react-native-health-connect';

export default function App() {
  const onInitialize = async () => {
    const result = await initialize();
    console.log({ result });
  };
  const checkAvailability = async () => {
    const available = await isAvailable();
    console.log({ available });
  };

  const insertSampleData = () => {
    insertRecords([
      {
        recordType: 'activeCaloriesBurned',
        energy: { unit: 'kilocalories', value: 10000 },
        startTime: '2023-01-09T10:00:00.405Z',
        endTime: '2023-01-09T11:53:15.405Z',
      },
      {
        recordType: 'activeCaloriesBurned',
        energy: { unit: 'kilocalories', value: 15000 },
        startTime: '2023-01-09T12:00:00.405Z',
        endTime: '2023-01-09T23:53:15.405Z',
      },
    ]).then((ids) => {
      console.log('Records inserted ', { ids });
    });
  };

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

  const aggreagetSampleData = () => {
    aggregateRecord({
      recordType: 'activeCaloriesBurned',
      timeRangeFilter: {
        operator: 'between',
        startTime: '2023-01-09T12:00:00.405Z',
        endTime: '2023-01-09T23:53:15.405Z',
      },
    }).then((result) => {
      console.log('Aggregated record: ', { result });
    });
  };

  const requestSamplePermissions = () => {
    requestPermission([
      {
        accessType: 'read',
        recordType: 'activeCaloriesBurned',
      },
      {
        accessType: 'write',
        recordType: 'activeCaloriesBurned',
      },
    ]).then((permissions) => {
      console.log('Granted permissions ', { permissions });
    });
  };

  return (
    <View style={styles.container}>
      <Button title="Initialize" onPress={onInitialize} />
      <Button title="Check availability" onPress={checkAvailability} />
      <Button
        title="Request sample permissions"
        onPress={requestSamplePermissions}
      />
      <Button title="Revoke all permissions" onPress={revokeAllPermissions} />
      <Button title="Insert sample data" onPress={insertSampleData} />
      <Button title="Read sample data" onPress={readSampleData} />
      <Button title="Aggregate sample data" onPress={aggreagetSampleData} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    rowGap: 16,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
