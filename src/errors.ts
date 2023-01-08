export class HealthConnectError extends Error {
  constructor(message: string, method: string) {
    super(`HealthConnect.${method}: ${message}`);
  }
}
