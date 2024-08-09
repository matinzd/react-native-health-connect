export enum DeviceType {
  TYPE_UNKNOWN = 0,
  TYPE_PHONE = 2,
  TYPE_SCALE = 3,
  TYPE_RING = 4,
  TYPE_HEAD_MOUNTED = 5,
  TYPE_FITNESS_BAND = 6,
  TYPE_CHEST_STRAP = 7,
  TYPE_SMART_DISPLAY = 8,
}

export enum RecordingMethod {
  /** Unknown recording method. */
  RECORDING_METHOD_UNKNOWN = 0,

  /**
   * For actively recorded data by the user.
   *
   * For e.g. An exercise session actively recorded by the user using a phone or a watch
   * device.
   */
  RECORDING_METHOD_ACTIVELY_RECORDED = 1,

  /**
   * For passively recorded data by the app.
   *
   * For e.g. Steps data recorded by a watch or phone without the user starting a session.
   */
  RECORDING_METHOD_AUTOMATICALLY_RECORDED = 2,

  /**
   * For manually entered data by the user.
   *
   * For e.g. Nutrition or weight data entered by the user.
   */
  RECORDING_METHOD_MANUAL_ENTRY = 3,
}

export interface Device {
  manufacturer?: string;
  model?: string;
  type?: DeviceType;
}

export interface Metadata {
  id?: string;
  // package name of the app that created the record
  dataOrigin?: string;
  // ISO 8601 date time string
  lastModifiedTime?: string;
  clientRecordId?: string;
  clientRecordVersion?: number;
  // see: https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/metadata/Device
  device?: Device;
  // Use RecordingType constant to compare
  recordingMethod?: RecordingMethod;
}
