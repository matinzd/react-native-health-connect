const {
  AndroidConfig,
  createRunOncePlugin,
  withAndroidManifest,
  withMainActivity,
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

const DELEGATE_IMPORT =
  'dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate';
const BUNDLE_IMPORT = 'android.os.Bundle';
const HEALTH_CONNECT_PACKAGE = 'com.google.android.apps.healthdata';
const DELEGATE_CALL = {
  kt: 'HealthConnectPermissionDelegate.setPermissionDelegate(this)',
  java: `HealthConnectPermissionDelegate.INSTANCE.setPermissionDelegate(this, "${HEALTH_CONNECT_PACKAGE}");`,
};

function addImport(contents, importPath, language) {
  const statement =
    language === 'java' ? `import ${importPath};` : `import ${importPath}`;

  if (contents.includes(statement)) {
    return contents;
  }

  const imports = [...contents.matchAll(/^import .*$/gm)];

  if (imports.length) {
    const last = imports[imports.length - 1];
    const insertAt = last.index + last[0].length;
    return `${contents.slice(0, insertAt)}\n${statement}${contents.slice(
      insertAt
    )}`;
  }

  const packageDeclaration = contents.match(/^package .*$/m);

  if (!packageDeclaration) {
    throw new Error(
      'react-native-health-connect: could not find a package declaration in MainActivity.'
    );
  }

  const insertAt = packageDeclaration.index + packageDeclaration[0].length;
  return `${contents.slice(0, insertAt)}\n\n${statement}${contents.slice(
    insertAt
  )}`;
}

/**
 * Adds an `onCreate` override to a MainActivity that does not declare one,
 * which is the case for the bare React Native template.
 */
function addOnCreateOverride(contents, language) {
  const body =
    language === 'java'
      ? [
          '  @Override',
          '  protected void onCreate(Bundle savedInstanceState) {',
          '    super.onCreate(savedInstanceState);',
          `    ${DELEGATE_CALL.java}`,
          '  }',
        ]
      : [
          '  override fun onCreate(savedInstanceState: Bundle?) {',
          '    super.onCreate(savedInstanceState)',
          `    ${DELEGATE_CALL.kt}`,
          '  }',
        ];

  const classDeclaration = contents.search(/\bclass\s+MainActivity\b/);

  if (classDeclaration === -1) {
    throw new Error(
      'react-native-health-connect: could not find the MainActivity class declaration.'
    );
  }

  const openingBrace = contents.indexOf('{', classDeclaration);

  if (openingBrace === -1) {
    throw new Error(
      'react-native-health-connect: could not find the MainActivity class body.'
    );
  }

  const withBundle = addImport(contents, BUNDLE_IMPORT, language);
  const offset = withBundle.length - contents.length;
  const insertAt = openingBrace + offset + 1;

  return `${withBundle.slice(0, insertAt)}\n${body.join(
    '\n'
  )}\n${withBundle.slice(insertAt)}`;
}

/**
 * Registers the permission delegate so `requestPermission()` has an
 * ActivityResultLauncher to launch. Without it the request throws
 * `UninitializedPropertyAccessException: lateinit property requestPermission
 * has not been initialized`.
 */
function setPermissionDelegate(contents, language) {
  if (language !== 'kt' && language !== 'java') {
    throw new Error(
      `react-native-health-connect: unsupported MainActivity language "${language}". ` +
        'Register the permission delegate manually: https://matinzd.github.io/react-native-health-connect/docs/get-started'
    );
  }

  // Also covers a delegate the developer registered by hand.
  if (contents.includes('setPermissionDelegate')) {
    return contents;
  }

  const withImport = addImport(contents, DELEGATE_IMPORT, language);
  const superCall = withImport.match(/^([ \t]*)super\.onCreate\(.*\)[ \t]*;?/m);

  if (!superCall) {
    if (/\bonCreate\s*\(/.test(withImport)) {
      throw new Error(
        'react-native-health-connect: MainActivity overrides onCreate without calling super.onCreate(). ' +
          'Register the permission delegate manually: https://matinzd.github.io/react-native-health-connect/docs/get-started'
      );
    }

    return addOnCreateOverride(withImport, language);
  }

  const indent = superCall[1];
  const insertAt = superCall.index + superCall[0].length;

  return `${withImport.slice(0, insertAt)}\n${indent}${
    DELEGATE_CALL[language]
  }${withImport.slice(insertAt)}`;
}

const withHealthConnect = (config) => {
  const withManifest = withAndroidManifest(config, (manifestConfig) => {
    manifestConfig.modResults = setHealthConnectManifest(
      manifestConfig.modResults
    );
    return manifestConfig;
  });

  return withMainActivity(withManifest, (activityConfig) => {
    activityConfig.modResults.contents = setPermissionDelegate(
      activityConfig.modResults.contents,
      activityConfig.modResults.language
    );
    return activityConfig;
  });
};

module.exports = createRunOncePlugin(withHealthConnect, pkg.name, pkg.version);

module.exports.setHealthConnectManifest = setHealthConnectManifest;
module.exports.addRationaleIntentFilter = addRationaleIntentFilter;
module.exports.addViewPermissionUsageAlias = addViewPermissionUsageAlias;
module.exports.setPermissionDelegate = setPermissionDelegate;
