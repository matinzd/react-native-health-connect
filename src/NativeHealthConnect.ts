import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type {
  HealthConnectRecord,
  Permission,
} from './NativeHealthConnect.types';

export interface Spec extends TurboModule {
  isAvailable(): Promise<boolean>;
  initialize(): Promise<boolean>;
  requestPermission(permissions: Permission[]): Promise<Permission[]>;
  revokeAllPermissions(): void;
  insertRecords(records: HealthConnectRecord[]): Promise<string[]>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('HealthConnect');
