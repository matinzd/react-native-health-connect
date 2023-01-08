import { NativeModules, Platform } from 'react-native';
import type {
  HealthConnectRecord,
  Permission,
} from './NativeHealthConnect.types';
import { HealthConnectError } from './errors';

const LINKING_ERROR =
  `The package 'react-native-health-connect' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const HealthConnectModule = isTurboModuleEnabled
  ? require('./NativeHealthConnect').default
  : NativeModules.HealthConnect;

const HealthConnect = HealthConnectModule
  ? HealthConnectModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function isAvailable(): Promise<boolean> {
  return HealthConnect.isAvailable();
}

export function initialize(): Promise<boolean> {
  return HealthConnect.initialize();
}

/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions
 */
export function requestPermission(
  permissions: Permission[]
): Promise<Permission> {
  return HealthConnect.requestPermission(permissions);
}

export function revokeAllPermissions(): void {
  return HealthConnect.revokeAllPermissions();
}

export function insertRecords(
  records: HealthConnectRecord[]
): Promise<string[]> {
  if (records.length === 0) {
    throw new HealthConnectError(
      'You must provide at least one record',
      'insertRecords'
    );
  }

  const recordTypes = records.map((record) => record.recordType);
  const uniqueRecordTypes = new Set(recordTypes);
  if (uniqueRecordTypes.size > 1) {
    throw new HealthConnectError(
      'All records must have the same type',
      'insertRecords'
    );
  }

  return HealthConnect.insertRecords(records);
}

export * from './constants';
