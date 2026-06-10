import * as React from 'react';

import {
  Alert,
  Button,
  ScrollView,
  StatusBar,
  StyleSheet,
  View,
} from 'react-native';
import {
  aggregateGroupByDuration,
  aggregateGroupByPeriod,
  aggregateRecord,
  DeviceType,
  ExerciseType,
  getGrantedPermissions,
  getSdkStatus,
  HealthConnectRecord,
  initialize,
  insertRecords,
  openHealthConnectDataManagement,
  openHealthConnectSettings,
  readRecord,
  readRecords,
  RecordingMethod,
  requestExerciseRoute,
  requestPermission,
  revokeAllPermissions,
  SdkAvailabilityStatus,
} from 'react-native-health-connect';
import { SafeAreaView } from 'react-native-safe-area-context';
import type { Location } from 'src/types/base.types';

function formatObject(obj: any) {
  return JSON.stringify(obj, null, 2);
}

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

const getBeginningOfLast7Days = () => {
  const date = new Date();
  date.setDate(date.getDate() - 7);
  date.setHours(0, 0, 0, 0);
  return date;
};

const getBeginningOfLast14Days = () => {
  const date = new Date();
  date.setDate(date.getDate() - 14);
  date.setHours(0, 0, 0, 0);
  return date;
};

const now = () => {
  return new Date();
};

const random64BitString = () => {
  return Math.floor(Math.random() * 0xffffffffffffffff).toString(16);
};

const insertedRecordIds: string[] = [];
let insertedExerciseId: string | undefined;

export default function App() {
  const initializeHealthConnect = async () => {
    const result = await initialize();
    Alert.alert('Result', formatObject({ result }));
  };

  const checkAvailability = async () => {
    const status = await getSdkStatus();
    if (status === SdkAvailabilityStatus.SDK_AVAILABLE) {
      Alert.alert('SDK Status', 'SDK is available');
    }

    if (status === SdkAvailabilityStatus.SDK_UNAVAILABLE) {
      Alert.alert('SDK Status', 'SDK is not available');
    }

    if (
      status === SdkAvailabilityStatus.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
    ) {
      Alert.alert(
        'SDK Status',
        'SDK is not available, provider update required'
      );
    }
  };

  const insertSampleData = () => {
    const days = 7;
    const startingStepCount = 1000;

    const records = Array.from({ length: days }).reduce(
      (acc: HealthConnectRecord[], _, i) => {
        // Step count increases by 1000 starting from the first day (7 days ago)
        const stepCount = startingStepCount + i * 1000;
        const date = new Date();

        // Get the date for each of the last 7 days, starting from 7 days ago (excluding today)
        date.setDate(date.getDate() - (days - i));

        // Set start time to 9am
        const startTime = new Date(date);
        startTime.setHours(9, 0, 0, 0);

        // Set end time to 11am
        const endTime = new Date(date);
        endTime.setHours(11, 0, 0, 0);

        const record: HealthConnectRecord = {
          recordType: 'Steps',
          count: stepCount,
          startTime: startTime.toISOString(),
          endTime: endTime.toISOString(),
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
        };

        return [...acc, record];
      },
      []
    );

    insertRecords(records)
      .then((ids) => {
        Alert.alert('Records inserted', formatObject({ ids }));
        insertedRecordIds.push(...ids);
      })
      .catch((err) => {
        console.error('Error inserting records ', { err });
      });
  };

  const readSampleData = () => {
    readRecords('Steps', {
      timeRangeFilter: {
        operator: 'between',
        startTime: getBeginningOfLast14Days().toISOString(),
        endTime: now().toISOString(),
      },
    })
      .then((result) => {
        Alert.alert('Retrieved records', formatObject(result));
      })
      .catch((err) => {
        console.error('Error reading records ', { err });
      });
  };

  const readSampleDataSingle = () => {
    if (insertedRecordIds.length === 0) {
      console.error(
        'No inserted record IDs available. Please insert sample data first.'
      );
      return;
    }

    const randomInsertedId =
      insertedRecordIds[Math.floor(Math.random() * insertedRecordIds.length)];
    Alert.alert('Reading record with ID', String(randomInsertedId));

    readRecord('Steps', '7bc394c6-56e6-3cd1-989d-fe5e0dcf0765')
      .then((result) => {
        Alert.alert('Retrieved record', formatObject(result));
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
        startTime: getBeginningOfLast7Days().toISOString(),
        endTime: now().toISOString(),
      },
    }).then((result) => {
      Alert.alert('Aggregated record', formatObject({ result }));
    });
  };

  const aggregateSampleGroupByDuration = () => {
    aggregateGroupByDuration({
      recordType: 'Steps',
      timeRangeFilter: {
        operator: 'between',
        startTime: getBeginningOfLast7Days().toISOString(),
        endTime: now().toISOString(),
      },
      timeRangeSlicer: {
        duration: 'DAYS',
        length: 2,
      },
    }).then((result) => {
      Alert.alert('Aggregated Group by Duration', formatObject(result));
    });
  };

  const aggregateSampleGroupByPeriod = () => {
    aggregateGroupByPeriod({
      recordType: 'Steps',
      timeRangeFilter: {
        operator: 'between',
        startTime: getBeginningOfLast7Days().toISOString(),
        endTime: now().toISOString(),
      },
      timeRangeSlicer: {
        period: 'DAYS',
        length: 1,
      },
    }).then((result) => {
      Alert.alert('Aggregated Group by Period', JSON.stringify(result));
    });
  };

  const requestSamplePermissions = () => {
    requestPermission([
      {
        accessType: 'read',
        recordType: 'BackgroundAccessPermission',
      },
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
      {
        accessType: 'write',
        recordType: 'ExerciseRoute',
      },
      {
        accessType: 'read',
        recordType: 'ReadHealthDataHistory',
      },
    ]).then((permissions) => {
      Alert.alert(
        'Granted permissions on request',
        formatObject({ permissions })
      );
    });
  };

  const grantedPermissions = () => {
    getGrantedPermissions().then((permissions) => {
      Alert.alert('Granted permissions', formatObject({ permissions }));
    });
  };

  const insertRandomExercise = () => {
    const startTime = new Date(
      Date.now() - Math.random() * 1000 * 60 * 60 * 48
    );
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
        Alert.alert('Records inserted', formatObject({ ids }));
        insertedExerciseId = ids[0];
      })
      .catch((err) => {
        console.error('Error inserting records ', { err });
      });
  };

  const readExercise = () => {
    if (!insertedExerciseId) {
      console.error(
        'No inserted exercise IDs available. Please insert an exercise first.'
      );
      return;
    }

    readRecord('ExerciseSession', insertedExerciseId)
      .then((exercise) => {
        Alert.alert('Exercise record', formatObject(exercise));
      })
      .catch((err) => {
        console.error('Error reading exercise record ', { err });
      });
  };

  const readExerciseRoute = () => {
    if (!insertedExerciseId) {
      console.error('Record ID is required');
      return;
    }

    requestExerciseRoute(insertedExerciseId)
      .then((route) => {
        Alert.alert('Exercise route', formatObject({ route }));
      })
      .catch((err) => {
        console.error('Error reading exercise route ', { err });
      });
  };

  return (
    <SafeAreaView>
      <StatusBar barStyle={'dark-content'} />
      <ScrollView>
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
          <Button
            title="Get granted permissions"
            onPress={grantedPermissions}
          />
          <Button
            title="Revoke all permissions"
            onPress={revokeAllPermissions}
          />
          <Button title="Insert sample data" onPress={insertSampleData} />
          <Button title="Read sample data" onPress={readSampleData} />
          <Button
            title="Read random inserted data"
            onPress={readSampleDataSingle}
          />
          <Button title="Aggregate sample data" onPress={aggregateSampleData} />
          <Button
            title="Aggregate sample group data by duration"
            onPress={aggregateSampleGroupByDuration}
          />
          <Button
            title="Aggregate sample group data by period"
            onPress={aggregateSampleGroupByPeriod}
          />
          <Button
            title="Insert random exercise"
            onPress={insertRandomExercise}
          />
          <Button title="Read exercise" onPress={readExercise} />
          <Button title="Request exercise route" onPress={readExerciseRoute} />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    rowGap: 16,
    padding: 16,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
