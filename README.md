1. Tại sao Gateway không gọi trực tiếp IP/Port?
   Vì Service Discovery (Eureka) cho phép Gateway tra cứu động vị trí service theo tên logic. Khi gọi lb://student-service, Gateway hỏi Eureka danh sách instance khả dụng và tự cân bằng tải (LoadBalancer). Lợi ích:

Không hard-code IP/Port → dễ scale, đổi port, thay container.

Tự động chịu lỗi (instance chết bị loại khỏi registry).

Load balancing giữa nhiều instance.

Decoupling vị trí triển khai với cấu hình gateway.

2. Request tăng đột biến – scale StudentService mà không đổi cấu hình Gateway?
   Chạy thêm nhiều instance student-service (port khác nhau, cùng spring.application.name=student-service).

Các instance tự đăng ký vào Eureka.

Gateway với lb://student-service tự động phân phối qua Spring Cloud LoadBalancer (Round-Robin mặc định) — không cần sửa cấu hình.

Có thể bật circuit breaker / rate limiting ở Gateway (Resilience4j) để bảo vệ.

3. So sánh OpenFeign vs Kafka
- OpenFeign:
Cơ chế: Request/Response trực tiếp
Độ trễ: Client chờ kết quả ngay
Coupling: Cao (Student phụ thuộc Course online)
Chịu lỗi: Nếu Course down → enroll fail
Nhất quán dữ liệu: Mạnh (biết chắc kết quả)
Khả năng scale: Hạn chế (phụ thuộc service kia)
Độ phức tạp: Đơn giản, dễ debug
Phù hợp: Enroll cần phản hồi tức thì, kiểm tra capacity ngay

- Kafka
Cơ chế: Gửi event qua broker, consumer xử lý sau
Độ trễ: Trả về ngay, xử lý ngầm
Chịu lỗi: Thấp (chỉ phụ thuộc topic)
Coupling: Cuối cùng (eventual consistency)
Nhất quán dữ liệu: Event xếp hàng, xử lý lại khi Course sống
Khả năng scale: Cao (producer/consumer tách rời, replay được)
Độ phức tạp: Phức tạp hơn (broker, idempotent, retry)
Phù hợp: Xử lý hàng loạt, chịu tải cao, hệ thống lớn

SƠ ĐỒ KIẾN TRÚC

Client
│
▼
API Gateway (8080)
│  GlobalFilter: log Time, Method, URI
├──► /api/students/** ──► student-service (8081) ──Feign──► course-service (8082)
│                                                  
└──► /api/courses/**  ──► course-service (8082)
Tất cả service đăng ký vào Eureka Server (8761)
Gateway gọi service qua lb://<service-name> (LoadBalanced)
