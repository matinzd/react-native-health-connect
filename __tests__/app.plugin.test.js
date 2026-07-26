const {
  setHealthConnectManifest,
  addViewPermissionUsageAlias,
} = require('../app.plugin');

const RATIONALE_ACTION = 'androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE';
const VIEW_PERMISSION_USAGE_ACTION =
  'android.intent.action.VIEW_PERMISSION_USAGE';

/** A manifest shaped like the one `expo prebuild` generates. */
function createAndroidManifest({ mainActivityIntentFilters } = {}) {
  return {
    manifest: {
      '$': { 'xmlns:android': 'http://schemas.android.com/apk/res/android' },
      'uses-permission': [
        { $: { 'android:name': 'android.permission.INTERNET' } },
      ],
      'application': [
        {
          $: { 'android:name': '.MainApplication' },
          activity: [
            {
              $: {
                'android:name': '.MainActivity',
                'android:exported': 'true',
              },
              ...(mainActivityIntentFilters === null
                ? {}
                : {
                    'intent-filter': mainActivityIntentFilters ?? [
                      {
                        action: [
                          {
                            $: { 'android:name': 'android.intent.action.MAIN' },
                          },
                        ],
                        category: [
                          {
                            $: {
                              'android:name':
                                'android.intent.category.LAUNCHER',
                            },
                          },
                        ],
                      },
                    ],
                  }),
            },
          ],
        },
      ],
    },
  };
}

const getMainActivity = (manifest) =>
  manifest.manifest.application[0].activity.find(
    (activity) => activity.$['android:name'] === '.MainActivity'
  );

const getAliases = (manifest) =>
  manifest.manifest.application[0]['activity-alias'] ?? [];

const hasAction = (node, action) =>
  node.action?.some((item) => item.$['android:name'] === action) ?? false;

describe('setHealthConnectManifest', () => {
  it('declares the rationale intent filter used through Android 13', () => {
    const manifest = setHealthConnectManifest(createAndroidManifest());

    const filters = getMainActivity(manifest)['intent-filter'];
    expect(
      filters.filter((filter) => hasAction(filter, RATIONALE_ACTION))
    ).toHaveLength(1);
  });

  it('declares the rationale component Android 14+ requires', () => {
    const manifest = setHealthConnectManifest(createAndroidManifest());

    const alias = getAliases(manifest).find(
      (item) => item.$['android:name'] === 'ViewPermissionUsageActivity'
    );

    expect(alias.$).toEqual({
      'android:name': 'ViewPermissionUsageActivity',
      'android:exported': 'true',
      'android:targetActivity': '.MainActivity',
      'android:permission': 'android.permission.START_VIEW_PERMISSION_USAGE',
    });
    expect(
      alias['intent-filter'].some((filter) =>
        hasAction(filter, VIEW_PERMISSION_USAGE_ACTION)
      )
    ).toBe(true);
    expect(alias['intent-filter'][0].category[0].$['android:name']).toBe(
      'android.intent.category.HEALTH_PERMISSIONS'
    );
  });

  it('is idempotent across repeated prebuilds', () => {
    const once = setHealthConnectManifest(createAndroidManifest());
    const twice = setHealthConnectManifest(
      setHealthConnectManifest(createAndroidManifest())
    );

    expect(twice).toEqual(once);
  });

  it('does not throw when the main activity has no intent filter yet', () => {
    const manifest = createAndroidManifest({ mainActivityIntentFilters: null });

    expect(() => setHealthConnectManifest(manifest)).not.toThrow();
    expect(getMainActivity(manifest)['intent-filter']).toHaveLength(1);
  });

  it('targets the main activity even when another activity is declared first', () => {
    const manifest = createAndroidManifest();
    manifest.manifest.application[0].activity.unshift({
      '$': { 'android:name': '.SomeOtherActivity' },
      'intent-filter': [],
    });

    setHealthConnectManifest(manifest);

    const other = manifest.manifest.application[0].activity.find(
      (activity) => activity.$['android:name'] === '.SomeOtherActivity'
    );
    expect(other['intent-filter']).toHaveLength(0);
    expect(
      getMainActivity(manifest)['intent-filter'].some((filter) =>
        hasAction(filter, RATIONALE_ACTION)
      )
    ).toBe(true);
  });

  it('preserves an unrelated activity alias', () => {
    const manifest = createAndroidManifest();
    manifest.manifest.application[0]['activity-alias'] = [
      {
        $: {
          'android:name': 'SomeOtherAlias',
          'android:targetActivity': '.MainActivity',
        },
      },
    ];

    setHealthConnectManifest(manifest);

    expect(
      getAliases(manifest).map((alias) => alias.$['android:name'])
    ).toEqual(['SomeOtherAlias', 'ViewPermissionUsageActivity']);
  });

  it('leaves existing intent filters on the main activity in place', () => {
    const manifest = setHealthConnectManifest(createAndroidManifest());

    const filters = getMainActivity(manifest)['intent-filter'];
    expect(filters[0].action[0].$['android:name']).toBe(
      'android.intent.action.MAIN'
    );
    expect(filters).toHaveLength(2);
  });

  it('throws a descriptive error when the main activity is missing', () => {
    const manifest = createAndroidManifest();
    manifest.manifest.application[0].activity = [];

    expect(() => setHealthConnectManifest(manifest)).toThrow(
      /missing the required MainActivity/
    );
  });
});

describe('addViewPermissionUsageAlias', () => {
  it('points at a renamed main activity instead of a hardcoded name', () => {
    const application = {};
    const mainActivity = { $: { 'android:name': 'com.example.HomeActivity' } };

    addViewPermissionUsageAlias(application, mainActivity);

    expect(application['activity-alias'][0].$['android:targetActivity']).toBe(
      'com.example.HomeActivity'
    );
  });

  it('repairs an alias left over from an earlier prebuild', () => {
    const application = {
      'activity-alias': [
        {
          $: {
            'android:name': 'ViewPermissionUsageActivity',
            'android:targetActivity': '.OldActivity',
          },
        },
      ],
    };

    addViewPermissionUsageAlias(application, {
      $: { 'android:name': '.MainActivity' },
    });

    expect(application['activity-alias']).toHaveLength(1);
    expect(application['activity-alias'][0].$['android:targetActivity']).toBe(
      '.MainActivity'
    );
  });
});
