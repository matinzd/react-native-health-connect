import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type {
  HealthConnectRecord,
  Permission,
} from './NativeHealthConnect.types';

type ReadRecordsOptions = {
  startTime: string;
  endTime: string;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
};

export interface Spec extends TurboModule {
  isAvailable(): Promise<boolean>;
  initialize(): Promise<boolean>;
  requestPermission(permissions: Permission[]): Promise<Permission[]>;
  revokeAllPermissions(): Promise<void>;
  insertRecords(records: HealthConnectRecord[]): Promise<string[]>;
  readRecords(
    recordType: string,
    options: ReadRecordsOptions
  ): Promise<string[]>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('HealthConnect');
