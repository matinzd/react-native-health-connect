import * as React from 'react';
import {
  Button,
  ScrollView,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {
  getGrantedPermissions,
  getSdkStatus,
  initialize,
  readRecords,
  requestPermission,
  SdkAvailabilityStatus,
} from 'react-native-health-connect';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { StatusBar } from 'expo-status-bar';

const PERMISSIONS = [
  { accessType: 'read', recordType: 'Steps' },
  { accessType: 'write', recordType: 'Steps' },
  { accessType: 'read', recordType: 'HeartRate' },
] as const;

const lastWeek = () => {
  const start = new Date();
  start.setDate(start.getDate() - 7);
  return start;
};

export default function App() {
  const isDark = useColorScheme() === 'dark';
  const [log, setLog] = React.useState<string>('Nothing yet.');

  // No MainActivity edit anywhere in this app: the permission delegate is registered
  // by the Expo module bundled in react-native-health-connect. If that wiring breaks,
  // "Request permissions" below throws UninitializedPropertyAccessException.
  const run = React.useCallback(
    async (label: string, fn: () => Promise<unknown>) => {
      try {
        const result = await fn();
        setLog(`${label}\n\n${JSON.stringify(result, null, 2)}`);
      } catch (error) {
        setLog(`${label} FAILED\n\n${String(error)}`);
      }
    },
    []
  );

  const readSteps = () =>
    readRecords('Steps', {
      timeRangeFilter: {
        operator: 'between',
        startTime: lastWeek().toISOString(),
        endTime: new Date().toISOString(),
      },
    });

  return (
    <SafeAreaProvider>
      <SafeAreaView
        style={[styles.container, isDark && styles.containerDark]}
        edges={['top', 'bottom']}
      >
        <StatusBar style={isDark ? 'light' : 'dark'} />
        <Text style={[styles.title, isDark && styles.textDark]}>
          Health Connect · Expo prebuild
        </Text>
        <View style={styles.actions}>
          <Button
            title="Check SDK status"
            onPress={() =>
              run('getSdkStatus', async () => {
                const status = await getSdkStatus();
                return {
                  status,
                  available: status === SdkAvailabilityStatus.SDK_AVAILABLE,
                };
              })
            }
          />
          <Button
            title="Initialize"
            onPress={() => run('initialize', initialize)}
          />
          <Button
            title="Request permissions"
            onPress={() =>
              run('requestPermission', () =>
                requestPermission([...PERMISSIONS])
              )
            }
          />
          <Button
            title="Granted permissions"
            onPress={() => run('getGrantedPermissions', getGrantedPermissions)}
          />
          <Button
            title="Read steps (last 7 days)"
            onPress={() => run('readRecords(Steps)', readSteps)}
          />
        </View>
        <ScrollView style={styles.output}>
          <Text style={[styles.mono, isDark && styles.textDark]}>{log}</Text>
        </ScrollView>
      </SafeAreaView>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16, backgroundColor: '#fff' },
  containerDark: { backgroundColor: '#111' },
  title: { fontSize: 18, fontWeight: '600', marginBottom: 16 },
  textDark: { color: '#eee' },
  actions: { gap: 8 },
  output: { flex: 1, marginTop: 16 },
  mono: { fontFamily: 'monospace', fontSize: 12 },
});
