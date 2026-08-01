const {
  AndroidConfig,
  createRunOncePlugin,
  withAndroidManifest,
} = require('@expo/config-plugins');

const pkg = require('./package.json');

const { getMainActivityOrThrow, getMainApplicationOrThrow } =
  AndroidConfig.Manifest;

const RATIONALE_ACTION = 'androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE';
const VIEW_PERMISSION_USAGE_ALIAS = 'ViewPermissionUsageActivity';

const hasIntentFilter = (androidName, intentFilters) =>
  intentFilters.some((intentFilter) =>
    (intentFilter.action || []).some(
      (action) => action.$['android:name'] === androidName
    )
  );

const hasActivityAlias = (androidName, activityAliases) =>
  activityAliases.some(
    (activityAlias) => activityAlias.$['android:name'] === androidName
  );

const withHealthConnect = (config) =>
  withAndroidManifest(config, async (config) => {
    const mainApplication = getMainApplicationOrThrow(config.modResults);
    const mainActivity = getMainActivityOrThrow(config.modResults);

    // Through Android 13, the main activity handles the intent shown when users tap
    // the privacy policy link in the Health Connect permission dialog.
    if (!mainActivity['intent-filter']) {
      mainActivity['intent-filter'] = [];
    }

    if (!hasIntentFilter(RATIONALE_ACTION, mainActivity['intent-filter'])) {
      mainActivity['intent-filter'].push({
        action: [{ $: { 'android:name': RATIONALE_ACTION } }],
      });
    }

    // Starting with Android 14 the same rationale is reached through an activity
    // alias guarded by START_VIEW_PERMISSION_USAGE instead.
    if (!mainApplication['activity-alias']) {
      mainApplication['activity-alias'] = [];
    }

    if (
      !hasActivityAlias(
        VIEW_PERMISSION_USAGE_ALIAS,
        mainApplication['activity-alias']
      )
    ) {
      mainApplication['activity-alias'].push({
        $: {
          'android:name': VIEW_PERMISSION_USAGE_ALIAS,
          'android:exported': 'true',
          'android:targetActivity': mainActivity.$['android:name'],
          'android:permission':
            'android.permission.START_VIEW_PERMISSION_USAGE',
        },
        'intent-filter': [
          {
            action: [
              {
                $: {
                  'android:name': 'android.intent.action.VIEW_PERMISSION_USAGE',
                },
              },
            ],
            category: [
              {
                $: {
                  'android:name': 'android.intent.category.HEALTH_PERMISSIONS',
                },
              },
            ],
          },
        ],
      });
    }

    return config;
  });

module.exports = createRunOncePlugin(withHealthConnect, pkg.name, pkg.version);
