import type { RecordType, HealthConnectRecord } from './records.types';

export interface GetChangesRequest {
  changesToken?: string;
  recordTypes?: RecordType[];
  dataOriginFilter?: string[];
}

export interface GetChangesResults {
  upsertionChanges: Array<{ record: HealthConnectRecord }>;
  deletionChanges: Array<{ recordId: string }>;
  nextChangesToken: string;
  changesTokenExpired: boolean;
  hasMore: boolean;
}
