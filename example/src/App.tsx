import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import {
  aggregateRecord,
  aggregateGroupByPeriod,
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
  HealthConnectRecord,
} from 'react-native-health-connect';

const getBeginningOfLast7Days = () => {
  const day = new Date();
  day.setDate(day.getDate() - 7);
  day.setHours(0, 0, 0, 0);
  return day;
};

const getBeginningOfLast14Days = () => {
  const day = new Date();
  day.setDate(day.getDate() - 14);
  day.setHours(0, 0, 0, 0);
  return day;
};

const getTodayDate = (): Date => {
  return new Date();
};

const random64BitString = () => {
  return Math.floor(Math.random() * 0xffffffffffffffff).toString(16);
};

export default function App() {
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
    const days = 7;
    const startingStepCount = 1000;

    const records = Array.from({ length: days }).reduce(
      (acc: HealthConnectRecord[], _, i) => {
        // Step count increases by 1000 starting from the first day (7 days ago)
        const stepCount = startingStepCount + i * 1000;
        const day = new Date();

        // Get the date for each of the last 7 days, starting from 7 days ago (excluding today)
        day.setDate(day.getDate() - (days - i));

        // Set start time to 9am
        const startTime = new Date(day);
        startTime.setHours(9, 0, 0, 0);

        // Set end time to 11am
        const endTime = new Date(day);
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
        startTime: getBeginningOfLast14Days().toISOString(),
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
        startTime: getBeginningOfLast7Days().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
    }).then((result) => {
      console.log('Aggregated record: ', { result });
    });
  };

  const aggregateSampleGroupByPeriod = () => {
    aggregateGroupByPeriod({
      recordType: 'Steps',
      timeRangeFilter: {
        operator: 'between',
        startTime: getBeginningOfLast7Days().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
      timeRangeSlicer: {
        period: 'DAYS',
        duration: 1,
      },
    }).then((result) => {
      console.log('Aggregated Group: ', JSON.stringify({ result }, null, 2));
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
      <Button
        title="Aggregate sample group data"
        onPress={aggregateSampleGroupByPeriod}
      />
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
