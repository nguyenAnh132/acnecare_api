## Admin API: List users

### Endpoint

`GET /admins/users`

### Authorization

- Requires **admin permission**: caller must have authority `ROLE_ADMIN`.
- If caller is authenticated but not admin, API returns **403** with error code `1008` (ACCESS_DENIED).

### Query parameters

- **page** (optional, int, default `0`): page index (0-based)
- **size** (optional, int, default `20`): page size
- **search** (optional, string): keyword to match (case-insensitive) against `email`, `firstName`, `lastName`
- **role** (optional, string): filter by role name. Allowed values: `ADMIN`, `PATIENT`, `DOCTOR`, `BRAND`
- **status** (optional, string): filter by status. Allowed values: `ACTIVE`, `PENDING`, `DISABLED`
- **startDate** (optional, date `yyyy-MM-dd`): include users with `createdAt >= startDate 00:00:00`
- **endDate** (optional, date `yyyy-MM-dd`): include users with `createdAt <= (endDate + 1 day) 00:00:00`
- **sortBy** (optional, string, default `createdAt`): `createdAt` | `name` | `email` | `username`
- **sortDir** (optional, string, default `desc`): `asc` | `desc`

### Response (success)

HTTP **200**

Body shape:

```json
{
  "code": 1000,
  "message": "Users retrieved successfully",
  "result": {
    "content": [
      {
        "id": "uuid",
        "username": "bob@example.com",
        "email": "bob@example.com",
        "role": "ADMIN",
        "status": "PENDING",
        "createdAt": "2026-03-19T10:15:30.123"
      }
    ],
    "pageable": { },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": { },
    "first": true,
    "numberOfElements": 1,
    "empty": false
  }
}
```

Notes:

- The API returns only these user fields: **id, username, email, role, status, createdAt**.
- `username` is currently mapped from `email`.

### Error codes

- **1007 UNAUTHENTICATED**: missing/invalid authentication
- **1008 ACCESS_DENIED**: not an admin
- **1010 INVALID_ROLE**: role query param not in allowed values
- **1011 INVALID_STATUS**: status query param not in allowed values
- **9999 UNCATEGORIZED_ERROR**: unexpected server error

### Examples

Filter admin users, sort newest first:

`GET /admins/users?page=0&size=20&role=ADMIN&sortBy=createdAt&sortDir=desc`

Search by keyword, sort by name:

`GET /admins/users?page=0&size=10&search=alice&sortBy=name&sortDir=asc`

