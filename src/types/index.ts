import type { RecordType } from './records.types';

export interface Permission {
  accessType: 'read' | 'write';
  recordType: RecordType;
}

export interface WriteExerciseRoutePermission {
  accessType: 'write';
  recordType: 'ExerciseRoute';
}

export * from './records.types';
export * from './results.types';
export * from './aggregate.types';
export * from './changes.types';
export * from './metadata.types';
