const {
  AndroidConfig,
  createRunOncePlugin,
  withAndroidManifest,
} = require('@expo/config-plugins');

const pkg = require('./package.json');

const { getMainActivityOrThrow, getMainApplicationOrThrow } =
  AndroidConfig.Manifest;

const RATIONALE_ACTION = 'androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE';
const VIEW_PERMISSION_USAGE_ACTION =
  'android.intent.action.VIEW_PERMISSION_USAGE';
const HEALTH_PERMISSIONS_CATEGORY =
  'android.intent.category.HEALTH_PERMISSIONS';
const VIEW_PERMISSION_USAGE_ALIAS = 'ViewPermissionUsageActivity';
const START_VIEW_PERMISSION_USAGE_PERMISSION =
  'android.permission.START_VIEW_PERMISSION_USAGE';

function hasAction(node, action) {
  return (
    node.action?.some((item) => item.$?.['android:name'] === action) ?? false
  );
}

/**
 * Declares the rationale intent filter Health Connect uses through Android 13
 * to open the app when the user taps the privacy policy link.
 */
function addRationaleIntentFilter(mainActivity) {
  const intentFilters = mainActivity['intent-filter'] ?? [];

  if (!intentFilters.some((filter) => hasAction(filter, RATIONALE_ACTION))) {
    intentFilters.push({
      action: [{ $: { 'android:name': RATIONALE_ACTION } }],
    });
  }

  mainActivity['intent-filter'] = intentFilters;
  return mainActivity;
}

/**
 * Declares the rationale component Android 14+ requires. Without it, the
 * platform silently revokes every `android.permission.health.*` permission:
 * `requestPermission()` resolves with `[]`, no dialog is shown, and the app
 * never appears in the Health Connect app permissions list.
 */
function addViewPermissionUsageAlias(mainApplication, mainActivity) {
  const aliases = mainApplication['activity-alias'] ?? [];
  const existing = aliases.find(
    (alias) => alias.$?.['android:name'] === VIEW_PERMISSION_USAGE_ALIAS
  );

  // `targetActivity` is read from the manifest rather than hardcoded, so apps
  // that rename or fully qualify their main activity still resolve.
  const targetActivity = mainActivity.$['android:name'];

  if (existing) {
    existing.$['android:targetActivity'] = targetActivity;
    return mainApplication;
  }

  aliases.push({
    '$': {
      'android:name': VIEW_PERMISSION_USAGE_ALIAS,
      'android:exported': 'true',
      'android:targetActivity': targetActivity,
      'android:permission': START_VIEW_PERMISSION_USAGE_PERMISSION,
    },
    'intent-filter': [
      {
        action: [{ $: { 'android:name': VIEW_PERMISSION_USAGE_ACTION } }],
        category: [{ $: { 'android:name': HEALTH_PERMISSIONS_CATEGORY } }],
      },
    ],
  });

  mainApplication['activity-alias'] = aliases;
  return mainApplication;
}

function setHealthConnectManifest(androidManifest) {
  const mainApplication = getMainApplicationOrThrow(androidManifest);
  const mainActivity = getMainActivityOrThrow(androidManifest);

  addRationaleIntentFilter(mainActivity);
  addViewPermissionUsageAlias(mainApplication, mainActivity);

  return androidManifest;
}

const withHealthConnect = (config) =>
  withAndroidManifest(config, (manifestConfig) => {
    manifestConfig.modResults = setHealthConnectManifest(
      manifestConfig.modResults
    );
    return manifestConfig;
  });

module.exports = createRunOncePlugin(withHealthConnect, pkg.name, pkg.version);

module.exports.setHealthConnectManifest = setHealthConnectManifest;
module.exports.addRationaleIntentFilter = addRationaleIntentFilter;
module.exports.addViewPermissionUsageAlias = addViewPermissionUsageAlias;
