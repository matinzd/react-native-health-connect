import { AppRegistry } from 'react-native';
import App from './src/App';
import { HealthConnectHeadlessTask } from './src/HealthConnectHeadlessTask';
import { name as appName } from './app.json';

AppRegistry.registerComponent(appName, () => App);

AppRegistry.registerHeadlessTask(
  'HealthConnectTask',
  () => HealthConnectHeadlessTask
);
