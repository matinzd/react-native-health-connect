import type { RecordType } from './records.types';

export interface Permission {
  accessType: 'read' | 'write';
  recordType: RecordType;
}

export * from './records.types';
export * from './results.types';
export * from './aggregate.types';
