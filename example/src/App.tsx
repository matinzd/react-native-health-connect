import * as React from 'react';

import {
  Button,
  NativeSyntheticEvent,
  StyleSheet,
  TextInput,
  TextInputChangeEventData,
  View,
} from 'react-native';
import {
  aggregateRecord,
  getGrantedPermissions,
  initialize,
  insertRecords,
  getSdkStatus,
  readRecords,
  requestPermission,
  revokeAllPermissions,
  SdkAvailabilityStatus,
  openHealthConnectSettings,
  openHealthConnectDataManagement,
  readRecord,
  RecordingMethod,
  DeviceType,
  ExerciseType,
  requestExerciseRoute,
} from 'react-native-health-connect';
import { Location } from 'src/types/base.types';

const generateExerciseRoute = (startTime: Date): Location[] => {
  const route: Location[] = [];
  for (let i = 0; i < 10; i++) {
    route.push({
      latitude: 37.785834 + Math.random() * 0.01,
      longitude: -122.406417 + Math.random() * 0.01,
      altitude: {
        value: 0 + Math.random() * 100,
        unit: 'meters',
      },
      horizontalAccuracy: {
        value: 0 + Math.random() * 10,
        unit: 'meters',
      },
      verticalAccuracy: {
        value: 0 + Math.random() * 10,
        unit: 'meters',
      },
      time: new Date(startTime.getTime() + i * 1000).toISOString(),
    });
  }
  return route;
};

const getLastWeekDate = (): Date => {
  return new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000);
};

const getLastTwoWeeksDate = (): Date => {
  return new Date(new Date().getTime() - 2 * 7 * 24 * 60 * 60 * 1000);
};

const getTodayDate = (): Date => {
  return new Date();
};

const random64BitString = () => {
  return Math.floor(Math.random() * 0xffffffffffffffff).toString(16);
};

export default function App() {
  const [recordId, setRecordId] = React.useState<string>();

  const updateRecordId = (
    e: NativeSyntheticEvent<TextInputChangeEventData>
  ) => {
    setRecordId(e.nativeEvent.text);
  };

  const initializeHealthConnect = async () => {
    const result = await initialize();
    console.log({ result });
  };

  const checkAvailability = async () => {
    const status = await getSdkStatus();
    if (status === SdkAvailabilityStatus.SDK_AVAILABLE) {
      console.log('SDK is available');
    }

    if (status === SdkAvailabilityStatus.SDK_UNAVAILABLE) {
      console.log('SDK is not available');
    }

    if (
      status === SdkAvailabilityStatus.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
    ) {
      console.log('SDK is not available, provider update required');
    }
  };

  const insertSampleData = () => {
    insertRecords([
      {
        recordType: 'Steps',
        count: 1000,
        startTime: getLastWeekDate().toISOString(),
        endTime: getTodayDate().toISOString(),
        metadata: {
          clientRecordId: random64BitString(),
          recordingMethod:
            RecordingMethod.RECORDING_METHOD_AUTOMATICALLY_RECORDED,
          device: {
            manufacturer: 'Google',
            model: 'Pixel 4',
            type: DeviceType.TYPE_PHONE,
          },
        },
      },
    ])
      .then((ids) => {
        console.log('Records inserted ', { ids });
      })
      .catch((err) => {
        console.error('Error inserting records ', { err });
      });
  };

  const readSampleData = () => {
    readRecords('Steps', {
      timeRangeFilter: {
        operator: 'between',
        startTime: getLastTwoWeeksDate().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
    })
      .then((result) => {
        console.log('Retrieved records: ', JSON.stringify({ result }, null, 2));
      })
      .catch((err) => {
        console.error('Error reading records ', { err });
      });
  };

  const readSampleDataSingle = () => {
    readRecord('Steps', '40a67ecf-d929-4648-996e-e8d248727d95')
      .then((result) => {
        console.log('Retrieved record: ', JSON.stringify({ result }, null, 2));
      })
      .catch((err) => {
        console.error('Error reading record ', { err });
      });
  };

  const aggregateSampleData = () => {
    aggregateRecord({
      recordType: 'Steps',
      timeRangeFilter: {
        operator: 'between',
        startTime: getLastWeekDate().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
    }).then((result) => {
      console.log('Aggregated record: ', { result });
    });
  };

  const requestSamplePermissions = () => {
    requestPermission([
      {
        accessType: 'read',
        recordType: 'Steps',
      },
      {
        accessType: 'write',
        recordType: 'Steps',
      },
      {
        accessType: 'write',
        recordType: 'ExerciseSession',
      },
      {
        accessType: 'read',
        recordType: 'ExerciseSession',
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

  const insertRandomExercise = () => {
    const startTime = new Date('2021-09-01T00:00:00.000Z');
    insertRecords([
      {
        recordType: 'ExerciseSession',
        startTime: startTime.toISOString(),
        endTime: new Date(startTime.getTime() + 1000 * 60 * 10).toISOString(), // 10 minutes
        metadata: {
          clientRecordId: random64BitString(),
          recordingMethod:
            RecordingMethod.RECORDING_METHOD_AUTOMATICALLY_RECORDED,
          device: {
            manufacturer: 'Google',
            model: 'Pixel 4',
            type: DeviceType.TYPE_PHONE,
          },
        },
        exerciseType: ExerciseType.RUNNING,
        exerciseRoute: { route: generateExerciseRoute(startTime) },
        title: 'Morning Run - v' + Math.random().toFixed(2).toString(),
      },
    ])
      .then((ids) => {
        console.log('Records inserted ', { ids });
      })
      .catch((err) => {
        console.error('Error inserting records ', { err });
      });
  };

  const readExerciseRoute = () => {
    if (!recordId) {
      console.error('Record ID is required');
      return;
    }

    requestExerciseRoute(recordId)
      .then((route) => {
        console.log('Exercise route: ', { route });
      })
      .catch((err) => {
        console.error('Error reading exercise route ', { err });
      });
  };

  return (
    <View style={styles.container}>
      <Button title="Initialize" onPress={initializeHealthConnect} />
      <Button
        title="Open Health Connect settings"
        onPress={openHealthConnectSettings}
      />
      <Button
        title="Open Health Connect data management"
        onPress={() => openHealthConnectDataManagement()}
      />
      <Button title="Check availability" onPress={checkAvailability} />
      <Button
        title="Request sample permissions"
        onPress={requestSamplePermissions}
      />
      <Button title="Get granted permissions" onPress={grantedPermissions} />
      <Button title="Revoke all permissions" onPress={revokeAllPermissions} />
      <Button title="Insert sample data" onPress={insertSampleData} />
      <Button title="Read sample data" onPress={readSampleData} />
      <Button title="Read specific data" onPress={readSampleDataSingle} />
      <Button title="Aggregate sample data" onPress={aggregateSampleData} />
      <Button title="Insert random exercise" onPress={insertRandomExercise} />
      <TextInput
        id="record-id"
        placeholder="Record ID"
        value={recordId}
        onChange={updateRecordId}
      />
      <Button title="Request exercise route" onPress={readExerciseRoute} />
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
