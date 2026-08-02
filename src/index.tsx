import { NativeModules, Platform } from 'react-native';
import { HealthConnectError } from './errors';
import type {
  AggregateRequest,
  AggregateResult,
  AggregateGroupByDurationRequest,
  AggregateGroupByPeriodRequest,
  AggregationGroupResult,
  AggregateResultRecordType,
  HealthConnectRecord,
  Permission,
  ReadRecordsOptions,
  RecordResult,
  RecordType,
  ReadRecordsResult,
  GetChangesRequest,
  GetChangesResults,
  WriteExerciseRoutePermission,
  ReadHealthDataHistoryPermission,
  BackgroundAccessPermission,
  RevokeAllPermissionsResponse,
} from './types';
import type { Location, TimeRangeFilter } from './types/base.types';

const LINKING_ERROR =
  `The package 'react-native-health-connect' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PLATFORM_NOT_SUPPORTED_ERROR = `Platform not supported. This package only supports Android.`;

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const moduleProxy = (message: string) =>
  new Proxy(
    {},
    {
      get() {
        throw new Error(message);
      },
    }
  );

const HealthConnectModule = Platform.select({
  android: isTurboModuleEnabled
    ? require('./NativeHealthConnect').default
    : NativeModules.HealthConnect,
  ios: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
  default: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
});

const HealthConnect = HealthConnectModule
  ? HealthConnectModule
  : moduleProxy(LINKING_ERROR);

const DEFAULT_PROVIDER_PACKAGE_NAME = 'com.google.android.apps.healthdata';

/**
 * Gets the status of the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns the status of the SDK - check SdkAvailabilityStatus constants
 */
export function getSdkStatus(
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME
): Promise<number> {
  return HealthConnect.getSdkStatus(providerPackageName);
}

/**
 * Initializes the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns true if the SDK was initialized successfully
 */
export function initialize(
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME
): Promise<boolean> {
  return HealthConnect.initialize(providerPackageName);
}

/**
 * Opens Health Connect settings app
 */
export function openHealthConnectSettings(): void {
  return HealthConnect.openHealthConnectSettings();
}

/**
 * Opens Health Connect data management screen
 */
export function openHealthConnectDataManagement(
  providerPackageName?: string
): void {
  return HealthConnect.openHealthConnectDataManagement(providerPackageName);
}

/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions, including special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission
 */
export function requestPermission(
  permissions: (
    | Permission
    | WriteExerciseRoutePermission
    | BackgroundAccessPermission
    | ReadHealthDataHistoryPermission
  )[]
): Promise<
  (
    | Permission
    | WriteExerciseRoutePermission
    | ReadHealthDataHistoryPermission
    | BackgroundAccessPermission
  )[]
> {
  return HealthConnect.requestPermission(permissions);
}

export function requestExerciseRoute(recordId: string): Promise<Location[]> {
  return HealthConnect.requestExerciseRoute(recordId);
}

/**
 * Returns a set of all health permissions granted by the user to the calling app.
 * This includes regular permissions as well as special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission.
 *
 * If your app called {@link revokeAllPermissions}, this keeps returning the revoked permissions
 * until the app process restarts. See {@link revokeAllPermissions} for details.
 *
 * @returns A promise that resolves to an array of granted permissions
 */
export function getGrantedPermissions(): Promise<
  (Permission | WriteExerciseRoutePermission | BackgroundAccessPermission)[]
> {
  return HealthConnect.getGrantedPermissions();
}

/**
 * Revokes all previously granted permissions by the user to the calling app.
 *
 * The revocation does not take effect until the app process restarts. This is a documented
 * Health Connect platform limitation, not a bug in this library. Until the restart:
 * - {@link getGrantedPermissions} keeps returning the revoked permissions.
 * - Reads and writes keep succeeding, and written data really is persisted.
 *
 * Because of this, do not use this as the mechanism behind an in-app "disconnect from Health
 * Connect" toggle. Prefer sending the user to Health Connect via {@link openHealthConnectSettings},
 * track the disconnected state in your own app and stop syncing yourself, and if you do call this,
 * tell the user the change takes effect after restarting the app.
 *
 * @see https://developer.android.com/health-and-fitness/health-connect/ui/permissions#sync-with-health-connect
 * @returns A promise that resolves to a RevokeAllPermissionsResponse object containing information about the revocation status,
 * or void for backward compatibility with older versions
 */
export function revokeAllPermissions(): Promise<RevokeAllPermissionsResponse | void> {
  return HealthConnect.revokeAllPermissions();
}

export function readRecords<T extends RecordType>(
  recordType: T,
  options: ReadRecordsOptions
): Promise<ReadRecordsResult<T>> {
  return HealthConnect.readRecords(recordType, options);
}

export function readRecord<T extends RecordType>(
  recordType: T,
  recordId: string
): Promise<RecordResult<T>> {
  return HealthConnect.readRecord(recordType, recordId);
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

export function aggregateRecord<T extends AggregateResultRecordType>(
  request: AggregateRequest<T>
): Promise<AggregateResult<T>> {
  return HealthConnect.aggregateRecord(request);
}

export function aggregateGroupByDuration<T extends AggregateResultRecordType>(
  request: AggregateGroupByDurationRequest<T>
): Promise<AggregationGroupResult<T>[]> {
  return HealthConnect.aggregateGroupByDuration(request);
}

export function aggregateGroupByPeriod<T extends AggregateResultRecordType>(
  request: AggregateGroupByPeriodRequest<T>
): Promise<AggregationGroupResult<T>[]> {
  return HealthConnect.aggregateGroupByPeriod(request);
}

export function getChanges(
  request: GetChangesRequest
): Promise<GetChangesResults> {
  return HealthConnect.getChanges(request);
}

export function deleteRecordsByUuids(
  recordType: RecordType,
  recordIdsList: string[],
  clientRecordIdsList: string[]
): Promise<void> {
  return HealthConnect.deleteRecordsByUuids(
    recordType,
    recordIdsList,
    clientRecordIdsList
  );
}

export function deleteRecordsByTimeRange(
  recordType: RecordType,
  timeRangeFilter: TimeRangeFilter
): Promise<void> {
  return HealthConnect.deleteRecordsByTimeRange(recordType, timeRangeFilter);
}

export * from './constants';
export * from './types';
