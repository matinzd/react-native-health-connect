import {
  initialize,
  isAvailable,
  readRecords,
} from 'react-native-health-connect';

export const HealthConnectHeadlessTask = async (data: any) => {
  console.log('Inside headless task ', { data });

  const initializedResult = await initialize();

  const isAvailableResult = await isAvailable();

  console.log({ isAvailableResult, initializedResult });

  const activeCaloriesBurned = await readRecords('ActiveCaloriesBurned', {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T00:00:00.000Z',
      endTime: '2023-01-09T23:59:59.999Z',
    },
  });

  console.log(
    'Retrieved records: ',
    JSON.stringify({ activeCaloriesBurned }, null, 2)
  );
};
