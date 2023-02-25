---
sidebar_position: 1
title: Overview
---

## Available methods

| **Method**                | **Description**                                                                                                                                                                                                                           |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| getSdkStatus              | Determines whether an implementation of HealthConnectClient is available on the device at the moment. If none is available, apps may choose to redirect to package installers to find suitable providers.                                 |
| initialize                | Initialize the health connect client.                                                                                                                                                                                                     |
| openHealthConnectSettings | Opens Health Connect app's settings app.                                                                                                                                                                                                  |
| requestPermission         | Request permission for specified record types and access types.                                                                                                                                                                           |
| getGrantedPermissions     | Returns a set of all health permissions granted by the user to the calling provider app.                                                                                                                                                  |
| revokeAllPermissions      | Revokes all previously granted permissions by the user to the calling app.                                                                                                                                                                |
| insertRecords             | Inserts one or more records and returns newly assigned generated UUIDs. Insertion of multiple records is executed in a transaction - if one fails, none is inserted.                                                                      |
| readRecords               | Retrieves a collection of records.                                                                                                                                                                                                        |
| aggregateRecord           | Reads aggregated results according to requested read criteria, for e.g, data origin filter and within a time range.                                                                                                                       |
| deleteRecordsByUuids      | Deletes one or more records by their identifiers. Deletion of multiple records is executed in a single transaction - if one fails, none is deleted.                                                                                       |
| deleteRecordsByTimeRange  | Deletes any record of the given record type in the given time range (automatically filtered to a record belonging to the calling application). Deletion of multiple records is executed in a transaction - if one fails, none is deleted. |
