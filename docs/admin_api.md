# API Documentation - Admin Module

## 1. Lấy danh sách người dùng

API cho phép Admin lấy danh sách toàn bộ người dùng trong hệ thống với các tùy chọn phân trang, lọc và tìm kiếm.

*   **Endpoint**: `/admins/users`
*   **Method**: `GET`
*   **Authentication**: Bearer Token (JWT)
*   **Role**: `ROLE_ADMIN`

### Tham số Request (Query Parameters)

| Tham số | Kiểu dữ liệu | Mô tả | Mặc định |
| :--- | :--- | :--- | :--- |
| `page` | `int` | Số trang (bắt đầu từ 0) | `0` |
| `size` | `int` | Số lượng record mỗi trang | `20` |
| `sort` | `string` | Sắp xếp (VD: `createdAt,desc` hoặc `email,asc`) | `id,asc` |
| `search` | `string` | Tìm kiếm theo email, firstName hoặc lastName | `null` |
| `role` | `string` | Lọc theo tên vai trò (VD: `PATIENT`, `DOCTOR`) | `null` |
| `status` | `string` | Lọc theo trạng thái (VD: `ACTIVE`, `PENDING`) | `null` |
| `startDate` | `string` | Lọc từ ngày (Định dạng: `YYYY-MM-DD`) | `null` |
| `endDate` | `string` | Lọc đến ngày (Định dạng: `YYYY-MM-DD`) | `null` |

### Phản hồi thành công (HTTP 200 OK)

```json
{
    "code": 1000,
    "message": "Users retrieved successfully",
    "result": {
        "content": [
            {
                "id": "550e8400-e29b-41d4-a716-446655440000",
                "username": "admin@gmail.com",
                "email": "admin@gmail.com",
                "fullName": "System Admin",
                "role": "ADMIN",
                "status": "ACTIVE",
                "createdAt": "2024-03-19T10:00:00"
            }
        ],
        "pageable": { ... },
        "totalPages": 1,
        "totalElements": 1,
        ...
    }
}
```

### Mã lỗi

| Mã lỗi (code) | HTTP Status | Mô tả |
| :--- | :--- | :--- |
| `1007` | `401 Unauthorized` | Token không hợp lệ hoặc đã hết hạn |
| `1009` | `403 Forbidden` | Không có quyền truy cập (không phải ADMIN) |
| `9999` | `500 Internal Server Error` | Lỗi hệ thống không xác định |

---

## 2. Các chức năng khác

*   **Caching**: Kết quả tìm kiếm được cache trong Redis để tối ưu hiệu suất.
*   **Logging**: Mọi yêu cầu truy cập danh sách người dùng đều được ghi log chi tiết (tham số tìm kiếm, số lượng kết quả, lỗi nếu có).
