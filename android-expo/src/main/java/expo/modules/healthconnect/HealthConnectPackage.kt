package expo.modules.healthconnect

import expo.modules.core.interfaces.Package

/**
 * The Expo package used to register a ReactActivityLifecycleListener that set up the
 * permission delegate. Permission dialogs no longer need anything registered on the
 * activity, so nothing is contributed here anymore.
 */
class HealthConnectPackage : Package
