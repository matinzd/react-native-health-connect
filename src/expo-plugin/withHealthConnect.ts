/* eslint-disable @typescript-eslint/no-shadow */
import {
  AndroidConfig,
  ConfigPlugin,
  createRunOncePlugin,
  withAndroidManifest,
  withDangerousMod,
  withMainActivity,
} from '@expo/config-plugins';
import { mergeContents } from '@expo/config-plugins/build/utils/generateCode';
import { promises as fs } from 'node:fs';
import path from 'node:path';

const pkg = require('../../../package.json');

const { getMainApplicationOrThrow } = AndroidConfig.Manifest;

type MainActivityLanguage = 'java' | 'kotlin';

type PluginProps = {
  permissionsRationaleActivityPath: string;
  providerPackageName?: string;
  mainActivityLanguage: MainActivityLanguage;
};

const MAIN_ACTIVITY_CHANGES: Record<
  MainActivityLanguage,
  { code: string[]; anchor: RegExp; offset: number; tag: string }[]
> = {
  java: [
    {
      code: [
        'import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate;',
      ],
      anchor:
        /import com\.facebook\.react\.defaults\.DefaultReactActivityDelegate;/,
      offset: 1,
      tag: 'react-native-health-connect import',
    },
    {
      code: [
        'HealthConnectPermissionDelegate hcpd = HealthConnectPermissionDelegate.INSTANCE;',
        'hcpd.setPermissionDelegate(this, "{{providerPackageName}}");',
      ],
      anchor: /super\.onCreate\(\w+\);/,
      offset: 1,
      tag: 'react-native-health-connect onCreate',
    },
  ],
  kotlin: [
    {
      code: [
        'import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate',
      ],
      anchor:
        /import com\.facebook\.react\.defaults\.DefaultReactActivityDelegate/,
      offset: 1,
      tag: 'react-native-health-connect import',
    },
    {
      code: [
        'HealthConnectPermissionDelegate.setPermissionDelegate(this, "{{providerPackageName}}")',
      ],
      anchor: /super\.onCreate\(\w+\)/,
      offset: 1,
      tag: 'react-native-health-connect onCreate',
    },
  ],
};

const withHealthConnect: ConfigPlugin<PluginProps> = (
  config,
  {
    mainActivityLanguage,
    providerPackageName = 'com.google.android.apps.healthdata',
    permissionsRationaleActivityPath,
  }
) => {
  // 1 - Add the permissions rationale activity to the AndroidManifest.xml
  config = withAndroidManifest(config, async (config) => {
    const application = getMainApplicationOrThrow(config.modResults);
    // Ensure activities array exists
    if (!application.activity) application.activity = [];
    const activities = application.activity;

    const permissionsRationaleActivityName = path
      .basename(permissionsRationaleActivityPath)
      .split('.')[0];

    // For supported versions through Android 13, create an activity to show the rationale
    // of Health Connect permissions once users click the privacy policy link.
    activities.push({
      '$': {
        'android:name': `.${permissionsRationaleActivityName}`,
        'android:exported': 'true',
      },
      'intent-filter': [
        {
          action: [
            {
              $: {
                'android:name':
                  'androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE',
              },
            },
          ],
        },
      ],
    });

    // For versions starting Android 14, create an activity alias to show the rationale
    // of Health Connect permissions once users click the privacy policy link.
    // @ts-expect-error activity-alias is not defined in the type
    if (!application['activity-alias']) application['activity-alias'] = [];
    // @ts-expect-error activity-alias is not defined in the type
    const activityAliases = application['activity-alias'];
    activityAliases.push({
      '$': {
        'android:name': 'ViewPermissionUsageActivity',
        'android:exported': 'true',
        'android:targetActivity': `.${permissionsRationaleActivityName}`,
        'android:permission': 'android.permission.START_VIEW_PERMISSION_USAGE',
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

    return config;
  });

  // 2 - Add the HealthConnectPermissionDelegate to the MainActivity
  config = withMainActivity(config, (config) => {
    const changes = MAIN_ACTIVITY_CHANGES[mainActivityLanguage];
    changes.forEach((change) => {
      config.modResults.contents = mergeContents({
        tag: change.tag,
        src: config.modResults.contents,
        newSrc: change.code
          .map((code) => {
            return code.replace('{{providerPackageName}}', providerPackageName);
          })
          .join('\n'),
        anchor: change.anchor,
        offset: change.offset,
        comment: '//',
      }).contents;
    });
    return config;
  });

  // 3 - Copy the PermissionsRationaleActivity.[java|kt] file to the Android project
  config = withDangerousMod(config, [
    'android',
    async (config) => {
      const packageName = config.android?.package;
      if (!packageName) throw new Error('No package id found');
      const projectRoot = config.modRequest.projectRoot;
      const destPath = path.resolve(
        projectRoot,
        `android/app/src/main/java/${packageName.split('.').join('/')}`
      );
      const fileName = path.basename(permissionsRationaleActivityPath);
      const buf = await fs.readFile(permissionsRationaleActivityPath);
      const content = buf.toString().replace('{{pkg}}', packageName);
      await fs.writeFile(path.resolve(destPath, fileName), content);
      return config;
    },
  ]);

  return config;
};

export default createRunOncePlugin(withHealthConnect, pkg.name, pkg.version);
