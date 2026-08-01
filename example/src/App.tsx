import * as React from 'react';

import {
  ActivityIndicator,
  Alert,
  Button,
  Pressable,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import {
  aggregateGroupByDuration,
  aggregateGroupByPeriod,
  aggregateRecord,
  AggregateResultRecordType,
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

type DemoRecordType =
  | 'Steps'
  | 'Distance'
  | 'HeartRate'
  | 'Weight'
  | 'Hydration'
  | 'TotalCaloriesBurned'
  | 'ExerciseSession';

const DEMO_RECORD_TYPES: { type: DemoRecordType; label: string }[] = [
  { type: 'Steps', label: 'Steps' },
  { type: 'Distance', label: 'Distance' },
  { type: 'HeartRate', label: 'Heart Rate' },
  { type: 'Weight', label: 'Weight' },
  { type: 'Hydration', label: 'Hydration' },
  { type: 'TotalCaloriesBurned', label: 'Calories' },
  { type: 'ExerciseSession', label: 'Exercise' },
];

const AGGREGATABLE_TYPES: AggregateResultRecordType[] = [
  'Steps',
  'Distance',
  'HeartRate',
  'Weight',
  'Hydration',
  'TotalCaloriesBurned',
  'ExerciseSession',
];

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

const now = () => {
  return new Date();
};

const random64BitString = () => {
  return Math.floor(Math.random() * 0xffffffffffffffff).toString(16);
};

const toNumberOrFallback = (value: string, fallback: number) => {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
};

const getTimeRange = (daysBack: number) => ({
  operator: 'between' as const,
  startTime: new Date(
    Date.now() - daysBack * 24 * 60 * 60 * 1000
  ).toISOString(),
  endTime: now().toISOString(),
});

const buildMetadata = () => ({
  clientRecordId: random64BitString(),
  recordingMethod: RecordingMethod.RECORDING_METHOD_AUTOMATICALLY_RECORDED,
  device: {
    manufacturer: 'Google',
    model: 'Pixel 8',
    type: DeviceType.TYPE_PHONE,
  },
});

const createSampleRecord = (
  recordType: DemoRecordType
): HealthConnectRecord => {
  const startTime = new Date(Date.now() - 60 * 60 * 1000);
  const endTime = new Date();

  switch (recordType) {
    case 'Steps':
      return {
        recordType: 'Steps',
        count: 1200 + Math.floor(Math.random() * 2500),
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        metadata: buildMetadata(),
      };
    case 'Distance':
      return {
        recordType: 'Distance',
        distance: {
          unit: 'meters',
          value: 300 + Math.floor(Math.random() * 5000),
        },
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        metadata: buildMetadata(),
      };
    case 'HeartRate': {
      const sampleCount = 5;
      return {
        recordType: 'HeartRate',
        samples: Array.from({ length: sampleCount }, (_, i) => ({
          time: new Date(startTime.getTime() + i * 5 * 60 * 1000).toISOString(),
          beatsPerMinute: 60 + Math.floor(Math.random() * 80),
        })),
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        metadata: buildMetadata(),
      };
    }
    case 'Weight':
      return {
        recordType: 'Weight',
        time: endTime.toISOString(),
        weight: {
          unit: 'grams',
          value: 70000 + Math.floor(Math.random() * 10000),
        },
        metadata: buildMetadata(),
      };
    case 'Hydration':
      return {
        recordType: 'Hydration',
        volume: {
          unit: 'liters',
          value: Number((0.2 + Math.random() * 0.8).toFixed(2)),
        },
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        metadata: buildMetadata(),
      };
    case 'TotalCaloriesBurned':
      return {
        recordType: 'TotalCaloriesBurned',
        energy: {
          unit: 'kilocalories',
          value: 150 + Math.floor(Math.random() * 650),
        },
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        metadata: buildMetadata(),
      };
    case 'ExerciseSession':
      return {
        recordType: 'ExerciseSession',
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        exerciseType: ExerciseType.RUNNING,
        title: `Sample Run ${Math.floor(Math.random() * 1000)}`,
        exerciseRoute: { route: generateExerciseRoute(startTime) },
        metadata: buildMetadata(),
      };
  }
};

type RequestPermissionItem = Parameters<typeof requestPermission>[0][number];

const buildPermissionRequest = (
  recordType: DemoRecordType
): RequestPermissionItem[] => {
  const permissions: RequestPermissionItem[] = [
    { accessType: 'read', recordType },
    { accessType: 'write', recordType },
  ];

  if (recordType === 'ExerciseSession') {
    permissions.push({ accessType: 'write', recordType: 'ExerciseRoute' });
  }

  return permissions;
};

export default function App() {
  const [selectedRecordType, setSelectedRecordType] =
    React.useState<DemoRecordType>('Steps');
  const [daysBackInput, setDaysBackInput] = React.useState('14');
  const [readLimitInput, setReadLimitInput] = React.useState('20');
  const [resultText, setResultText] = React.useState(
    'Ready. Select a record type and run an action.'
  );
  const [isBusy, setIsBusy] = React.useState(false);

  const insertedIdsByTypeRef = React.useRef<Record<DemoRecordType, string[]>>({
    Steps: [],
    Distance: [],
    HeartRate: [],
    Weight: [],
    Hydration: [],
    TotalCaloriesBurned: [],
    ExerciseSession: [],
  });

  const selectedInsertedIds =
    insertedIdsByTypeRef.current[selectedRecordType] ?? [];

  const runAction = async (actionName: string, action: () => Promise<void>) => {
    try {
      setIsBusy(true);
      await action();
    } catch (error) {
      const message = `${actionName} failed`;
      console.error(message, error);
      setResultText(formatObject({ action: actionName, error }));
      Alert.alert('Error', message);
    } finally {
      setIsBusy(false);
    }
  };

  const initializeHealthConnect = async () => {
    await runAction('initialize', async () => {
      const result = await initialize();
      const payload = { result };
      setResultText(formatObject(payload));
      Alert.alert('Initialize', formatObject(payload));
    });
  };

  const checkAvailability = async () => {
    await runAction('checkAvailability', async () => {
      const status = await getSdkStatus();
      const payload = { status };
      setResultText(formatObject(payload));

      if (status === SdkAvailabilityStatus.SDK_AVAILABLE) {
        Alert.alert('SDK Status', 'SDK is available');
      }

      if (status === SdkAvailabilityStatus.SDK_UNAVAILABLE) {
        Alert.alert('SDK Status', 'SDK is not available');
      }

      if (
        status ===
        SdkAvailabilityStatus.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
      ) {
        Alert.alert(
          'SDK Status',
          'SDK is not available, provider update required'
        );
      }
    });
  };

  const requestSamplePermissions = async () => {
    await runAction('requestPermission', async () => {
      const permissions = await requestPermission(
        buildPermissionRequest(selectedRecordType)
      );
      const payload = { selectedRecordType, permissions };
      setResultText(formatObject(payload));
      Alert.alert('Permissions granted', formatObject(payload));
    });
  };

  const grantedPermissions = async () => {
    await runAction('getGrantedPermissions', async () => {
      const permissions = await getGrantedPermissions();
      const payload = { permissions };
      setResultText(formatObject(payload));
      Alert.alert('Granted permissions', formatObject(payload));
    });
  };

  const insertSampleData = async () => {
    await runAction('insertRecords', async () => {
      const sample = createSampleRecord(selectedRecordType);
      const ids = await insertRecords([sample]);
      insertedIdsByTypeRef.current[selectedRecordType].push(...ids);

      const payload = {
        selectedRecordType,
        inserted: ids.length,
        ids,
        preview: sample,
      };
      setResultText(formatObject(payload));
      Alert.alert('Record inserted', formatObject(payload));
    });
  };

  const readSampleData = async () => {
    await runAction('readRecords', async () => {
      const daysBack = toNumberOrFallback(daysBackInput, 14);
      const pageSize = toNumberOrFallback(readLimitInput, 20);
      const result = await readRecords(selectedRecordType, {
        timeRangeFilter: getTimeRange(daysBack),
        pageSize,
      });

      const payload = {
        selectedRecordType,
        daysBack,
        pageSize,
        count: result.records?.length ?? 0,
        result,
      };
      setResultText(formatObject(payload));
    });
  };

  const readSampleDataSingle = async () => {
    await runAction('readRecord', async () => {
      const ids = insertedIdsByTypeRef.current[selectedRecordType];
      const latestId = ids[ids.length - 1];

      if (!latestId) {
        Alert.alert('No inserted records', 'Insert a sample record first.');
        return;
      }

      const result = await readRecord(selectedRecordType, latestId);
      const payload = { selectedRecordType, id: latestId, result };
      setResultText(formatObject(payload));
    });
  };

  const aggregateSampleData = async () => {
    await runAction('aggregateRecord', async () => {
      if (!AGGREGATABLE_TYPES.includes(selectedRecordType)) {
        Alert.alert(
          'Not supported',
          `${selectedRecordType} does not support aggregateRecord`
        );
        return;
      }

      const daysBack = toNumberOrFallback(daysBackInput, 14);
      const result = await aggregateRecord({
        recordType: selectedRecordType,
        timeRangeFilter: getTimeRange(daysBack),
      });
      const payload = { selectedRecordType, daysBack, result };
      setResultText(formatObject(payload));
    });
  };

  const aggregateSampleGroupByDuration = async () => {
    await runAction('aggregateGroupByDuration', async () => {
      if (!AGGREGATABLE_TYPES.includes(selectedRecordType)) {
        Alert.alert(
          'Not supported',
          `${selectedRecordType} does not support aggregateGroupByDuration`
        );
        return;
      }

      const daysBack = toNumberOrFallback(daysBackInput, 14);
      const result = await aggregateGroupByDuration({
        recordType: selectedRecordType,
        timeRangeFilter: getTimeRange(daysBack),
        timeRangeSlicer: {
          duration: 'DAYS',
          length: 1,
        },
      });
      const payload = { selectedRecordType, daysBack, result };
      setResultText(formatObject(payload));
    });
  };

  const aggregateSampleGroupByPeriod = async () => {
    await runAction('aggregateGroupByPeriod', async () => {
      if (!AGGREGATABLE_TYPES.includes(selectedRecordType)) {
        Alert.alert(
          'Not supported',
          `${selectedRecordType} does not support aggregateGroupByPeriod`
        );
        return;
      }

      const daysBack = toNumberOrFallback(daysBackInput, 14);
      const result = await aggregateGroupByPeriod({
        recordType: selectedRecordType,
        timeRangeFilter: getTimeRange(daysBack),
        timeRangeSlicer: {
          period: 'DAYS',
          length: 1,
        },
      });
      const payload = { selectedRecordType, daysBack, result };
      setResultText(formatObject(payload));
    });
  };

  const readExerciseRoute = async () => {
    await runAction('requestExerciseRoute', async () => {
      const latestExerciseId =
        insertedIdsByTypeRef.current.ExerciseSession[
          insertedIdsByTypeRef.current.ExerciseSession.length - 1
        ];

      if (!latestExerciseId) {
        Alert.alert('No exercise records', 'Insert an ExerciseSession first.');
        return;
      }

      const route = await requestExerciseRoute(latestExerciseId);
      const payload = { exerciseId: latestExerciseId, route };
      setResultText(formatObject(payload));
    });
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} />
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.container}>
          <Text style={styles.title}>Health Connect Playground</Text>
          <Text style={styles.subtitle}>
            Pick a record type, grant permission, then insert and query test
            data.
          </Text>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Record Type</Text>
            <View style={styles.chipRow}>
              {DEMO_RECORD_TYPES.map((item) => {
                const selected = item.type === selectedRecordType;
                return (
                  <Pressable
                    key={item.type}
                    onPress={() => setSelectedRecordType(item.type)}
                    style={[styles.chip, selected && styles.chipSelected]}
                  >
                    <Text
                      style={[
                        styles.chipLabel,
                        selected && styles.chipLabelSelected,
                      ]}
                    >
                      {item.label}
                    </Text>
                  </Pressable>
                );
              })}
            </View>
            <Text style={styles.metaText}>Selected: {selectedRecordType}</Text>
            <Text style={styles.metaText}>
              Inserted IDs for selected type: {selectedInsertedIds.length}
            </Text>
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Query Options</Text>
            <TextInput
              style={styles.input}
              keyboardType="number-pad"
              value={daysBackInput}
              onChangeText={setDaysBackInput}
              placeholder="Days back (e.g. 14)"
            />
            <TextInput
              style={styles.input}
              keyboardType="number-pad"
              value={readLimitInput}
              onChangeText={setReadLimitInput}
              placeholder="Read limit (e.g. 20)"
            />
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Setup</Text>
            <Button title="Initialize" onPress={initializeHealthConnect} />
            <View style={styles.buttonGap} />
            <Button title="Check availability" onPress={checkAvailability} />
            <View style={styles.buttonGap} />
            <Button
              title="Open Health Connect settings"
              onPress={openHealthConnectSettings}
            />
            <View style={styles.buttonGap} />
            <Button
              title="Open data management"
              onPress={() => openHealthConnectDataManagement()}
            />
            <View style={styles.buttonGap} />
            <Button
              title="Request selected permissions"
              onPress={requestSamplePermissions}
            />
            <View style={styles.buttonGap} />
            <Button
              title="Get granted permissions"
              onPress={grantedPermissions}
            />
            <View style={styles.buttonGap} />
            <Button
              title="Revoke all permissions"
              onPress={revokeAllPermissions}
            />
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Data Actions</Text>
            <Button
              title={`Insert sample ${selectedRecordType}`}
              onPress={insertSampleData}
            />
            <View style={styles.buttonGap} />
            <Button
              title={`Read ${selectedRecordType} list`}
              onPress={readSampleData}
            />
            <View style={styles.buttonGap} />
            <Button
              title={`Read latest inserted ${selectedRecordType}`}
              onPress={readSampleDataSingle}
            />
            <View style={styles.buttonGap} />
            <Button
              title={`Aggregate ${selectedRecordType}`}
              onPress={aggregateSampleData}
            />
            <View style={styles.buttonGap} />
            <Button
              title={`Aggregate duration (${selectedRecordType})`}
              onPress={aggregateSampleGroupByDuration}
            />
            <View style={styles.buttonGap} />
            <Button
              title={`Aggregate period (${selectedRecordType})`}
              onPress={aggregateSampleGroupByPeriod}
            />
            <View style={styles.buttonGap} />
            <Button
              title="Read latest exercise route"
              onPress={readExerciseRoute}
            />
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Result</Text>
            {isBusy ? <ActivityIndicator size="small" color="#2057d4" /> : null}
            <Text style={styles.resultText}>{resultText}</Text>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#f5f7ff',
  },
  scrollContent: {
    paddingBottom: 28,
  },
  container: {
    gap: 14,
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    color: '#1a1e2e',
  },
  subtitle: {
    fontSize: 14,
    color: '#414a64',
    lineHeight: 20,
  },
  section: {
    backgroundColor: '#ffffff',
    borderRadius: 14,
    padding: 12,
    borderWidth: 1,
    borderColor: '#dce3ff',
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    marginBottom: 8,
    color: '#1a1e2e',
  },
  chipRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  chip: {
    borderWidth: 1,
    borderColor: '#c3cff9',
    borderRadius: 999,
    paddingHorizontal: 10,
    paddingVertical: 8,
    backgroundColor: '#f3f6ff',
  },
  chipSelected: {
    backgroundColor: '#2057d4',
    borderColor: '#2057d4',
  },
  chipLabel: {
    color: '#1f2c57',
    fontWeight: '600',
  },
  chipLabelSelected: {
    color: '#ffffff',
  },
  metaText: {
    marginTop: 6,
    color: '#3d4662',
    fontSize: 13,
  },
  input: {
    borderWidth: 1,
    borderColor: '#c3cff9',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
    marginBottom: 8,
    backgroundColor: '#f9fbff',
  },
  buttonGap: {
    height: 8,
  },
  resultText: {
    fontFamily: 'Courier',
    marginTop: 10,
    color: '#17213f',
    fontSize: 12,
    lineHeight: 18,
  },
});
