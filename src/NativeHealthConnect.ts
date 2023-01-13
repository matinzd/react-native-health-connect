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

//@TODO: Fix and refactor types when codegen starts supporting type imports and generics
export interface Spec extends TurboModule {
  isAvailable(providerPackageNames: string[]): Promise<boolean>;
  initialize(providerPackageNames: string[]): Promise<boolean>;
  requestPermission(
    permissions: Permission[],
    providerPackageName: string
  ): Promise<Permission[]>;
  revokeAllPermissions(): Promise<void>;
  insertRecords(records: HealthConnectRecord[]): Promise<string[]>;
  readRecords(
    recordType: string,
    options: ReadRecordsOptions
  ): Promise<Array<{}>>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('HealthConnect');
