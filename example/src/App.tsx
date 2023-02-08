import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import {
  aggregateRecord,
  getGrantedPermissions,
  initialize,
  insertRecords,
  isAvailable,
  readRecords,
  requestPermission,
  revokeAllPermissions,
} from 'react-native-health-connect';

export default function App() {
  const initializeHealthConnect = async () => {
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
        recordType: 'ActiveCaloriesBurned',
        energy: { unit: 'kilocalories', value: 10000 },
        startTime: '2023-01-09T09:00:00.000Z',
        endTime: '2023-01-09T10:00:00.000Z',
      },
      {
        recordType: 'ActiveCaloriesBurned',
        energy: { unit: 'kilocalories', value: 15000 },
        startTime: '2023-01-09T12:00:00.000Z',
        endTime: '2023-01-09T14:00:00.000Z',
      },
    ]).then((ids) => {
      console.log('Records inserted ', { ids });
    });
  };

  const readSampleData = () => {
    readRecords('ActiveCaloriesBurned', {
      timeRangeFilter: {
        operator: 'between',
        startTime: '2023-01-09T00:00:00.000Z',
        endTime: '2023-01-09T23:59:59.999Z',
      },
    }).then((result) => {
      console.log('Retrieved records: ', JSON.stringify({ result }, null, 2));
    });
  };

  const aggreagetSampleData = () => {
    aggregateRecord({
      recordType: 'ActiveCaloriesBurned',
      timeRangeFilter: {
        operator: 'between',
        startTime: '2023-01-09T00:00:00.000Z',
        endTime: '2023-01-09T23:59:59.999Z',
      },
    }).then((result) => {
      console.log('Aggregated record: ', { result });
    });
  };

  const requestSamplePermissions = () => {
    requestPermission([
      {
        accessType: 'read',
        recordType: 'ActiveCaloriesBurned',
      },
      {
        accessType: 'write',
        recordType: 'ActiveCaloriesBurned',
      },
    ]).then((permissions) => {
      console.log('Granted permissions on request ', { permissions });
    });
  };

  const grantedPermissions = () => {
    getGrantedPermissions().then((permissions) => {
      console.log('Granted permissions ', { permissions });
    });
  };

  return (
    <View style={styles.container}>
      <Button title="Initialize" onPress={initializeHealthConnect} />
      <Button title="Check availability" onPress={checkAvailability} />
      <Button
        title="Request sample permissions"
        onPress={requestSamplePermissions}
      />
      <Button title="Get granted permissions" onPress={grantedPermissions} />
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
