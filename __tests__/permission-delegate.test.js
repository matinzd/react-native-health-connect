const { setPermissionDelegate } = require('../app.plugin');

const KOTLIN_IMPORT =
  'import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate';
const KOTLIN_CALL =
  'HealthConnectPermissionDelegate.setPermissionDelegate(this)';
const JAVA_CALL =
  'HealthConnectPermissionDelegate.INSTANCE.setPermissionDelegate(this, "com.google.android.apps.healthdata");';

/** MainActivity as `expo prebuild` generates it. */
const EXPO_MAIN_ACTIVITY = `package com.example

import android.os.Bundle
import expo.modules.splashscreen.SplashScreenManager
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate

class MainActivity : ReactActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    // @generated begin expo-splashscreen - expo prebuild (DO NOT MODIFY)
    SplashScreenManager.registerOnActivity(this)
    // @generated end expo-splashscreen
    super.onCreate(null)
  }

  override fun getMainComponentName(): String = "example"
}
`;

/** MainActivity as the bare React Native template generates it: no onCreate. */
const BARE_MAIN_ACTIVITY = `package com.example

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate

class MainActivity : ReactActivity() {
  override fun getMainComponentName(): String = "example"

  override fun createReactActivityDelegate(): ReactActivityDelegate =
    DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
`;

const JAVA_MAIN_ACTIVITY = `package com.example;

import android.os.Bundle;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected String getMainComponentName() {
    return "example";
  }
}
`;

const JAVA_MAIN_ACTIVITY_WITHOUT_ON_CREATE = `package com.example;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
  @Override
  protected String getMainComponentName() {
    return "example";
  }
}
`;

describe('setPermissionDelegate', () => {
  it('registers the delegate after super.onCreate in a Kotlin MainActivity', () => {
    const result = setPermissionDelegate(EXPO_MAIN_ACTIVITY, 'kt');

    expect(result).toContain(KOTLIN_IMPORT);
    expect(result).toMatch(
      /super\.onCreate\(null\)\n {4}HealthConnectPermissionDelegate\.setPermissionDelegate\(this\)/
    );
  });

  it('leaves the generated splash screen block untouched', () => {
    const result = setPermissionDelegate(EXPO_MAIN_ACTIVITY, 'kt');

    expect(result).toContain(
      '// @generated begin expo-splashscreen - expo prebuild (DO NOT MODIFY)\n    SplashScreenManager.registerOnActivity(this)\n    // @generated end expo-splashscreen'
    );
  });

  it('adds an onCreate override when the Kotlin MainActivity has none', () => {
    const result = setPermissionDelegate(BARE_MAIN_ACTIVITY, 'kt');

    expect(result).toContain('import android.os.Bundle');
    expect(result).toContain(
      'override fun onCreate(savedInstanceState: Bundle?) {\n    super.onCreate(savedInstanceState)\n    ' +
        KOTLIN_CALL
    );
    // The synthesized override must land inside the class body.
    expect(result.indexOf('class MainActivity')).toBeLessThan(
      result.indexOf('override fun onCreate')
    );
  });

  it('uses the Java call form in a Java MainActivity', () => {
    const result = setPermissionDelegate(JAVA_MAIN_ACTIVITY, 'java');

    expect(result).toContain(`${KOTLIN_IMPORT};`);
    expect(result).toContain(
      `super.onCreate(savedInstanceState);\n    ${JAVA_CALL}`
    );
  });

  it('adds an onCreate override when the Java MainActivity has none', () => {
    const result = setPermissionDelegate(
      JAVA_MAIN_ACTIVITY_WITHOUT_ON_CREATE,
      'java'
    );

    expect(result).toContain('import android.os.Bundle;');
    expect(result).toContain(
      '  @Override\n  protected void onCreate(Bundle savedInstanceState) {\n    super.onCreate(savedInstanceState);\n    ' +
        JAVA_CALL
    );
  });

  it('is idempotent across repeated prebuilds', () => {
    const once = setPermissionDelegate(EXPO_MAIN_ACTIVITY, 'kt');
    const twice = setPermissionDelegate(once, 'kt');

    expect(twice).toBe(once);
  });

  it('does not duplicate a delegate the developer registered by hand', () => {
    const manual = setPermissionDelegate(BARE_MAIN_ACTIVITY, 'kt');

    expect(setPermissionDelegate(manual, 'kt')).toBe(manual);
    expect(manual.match(/setPermissionDelegate/g)).toHaveLength(1);
  });

  it('throws for an unsupported MainActivity language', () => {
    expect(() => setPermissionDelegate(EXPO_MAIN_ACTIVITY, 'swift')).toThrow(
      /unsupported MainActivity language/
    );
  });

  it('throws when onCreate is overridden without calling super', () => {
    const contents = BARE_MAIN_ACTIVITY.replace(
      'override fun getMainComponentName(): String = "example"',
      'override fun onCreate(savedInstanceState: Bundle?) {}'
    );

    expect(() => setPermissionDelegate(contents, 'kt')).toThrow(
      /without calling super\.onCreate/
    );
  });

  it('throws when MainActivity has no class declaration to patch', () => {
    expect(() =>
      setPermissionDelegate('package com.example\n\nimport foo.Bar\n', 'kt')
    ).toThrow(/could not find the MainActivity class declaration/);
  });
});
