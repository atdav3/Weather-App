# 🌤️ Ứng dụng Thời tiết Android

Ứng dụng thời tiết Android hiện đại với giao diện đẹp mắt và đầy đủ tính năng, cung cấp thông tin thời tiết chi tiết, dự báo và nhiều công cụ hữu ích khác.

## ✨ Tính năng

### 🌡️ Thông tin thời tiết
- **Thời tiết hiện tại**: Nhiệt độ, độ ẩm, tốc độ gió, áp suất, tầm nhìn, chỉ số UV
- **Dự báo theo giờ**: Xem dự báo thời tiết 24 giờ tới
- **Dự báo 3 ngày**: Dự báo thời tiết cho 3 ngày tiếp theo
- **Chi tiết thời tiết**: Thông tin chi tiết về điều kiện thời tiết hiện tại

### 🗺️ Bản đồ thời tiết
- Tích hợp Google Maps
- Hiển thị thời tiết trên bản đồ
- Chọn vị trí trên bản đồ để xem thời tiết
- Các lớp thời tiết: lượng mưa, gió

### 📊 Biểu đồ thời tiết
- Biểu đồ nhiệt độ theo thời gian
- Thống kê: trung bình, cao nhất, thấp nhất
- Nhiều loại biểu đồ khác nhau

### 📅 Lịch thời tiết
- Xem dự báo thời tiết theo lịch
- Thêm sự kiện thời tiết
- Quản lý các sự kiện thời tiết

### 🔍 Tìm kiếm
- Tìm kiếm thành phố
- Lịch sử tìm kiếm
- Tự động lấy vị trí hiện tại

### 👤 Quản lý người dùng
- Đăng nhập / Đăng ký
- Quản lý tài khoản
- Lịch sử xem thời tiết
- Yêu thích địa điểm

### 👨‍💼 Admin Panel
- Quản lý người dùng
- Thống kê tổng quan
- Theo dõi hoạt động người dùng
- Phân tích hiệu suất

### ⚙️ Cài đặt
- Chọn đơn vị đo (Celsius/Fahrenheit, km/h/mph)
- Chủ đề sáng/tối
- Tùy chỉnh giao diện
- Đa ngôn ngữ

### 📱 Widget
- Widget thời tiết cho màn hình chính
- Cập nhật tự động

## 🛠️ Yêu cầu hệ thống

- **Android**: 7.0 (API level 24) trở lên
- **Target SDK**: 36
- **Java**: 17
- **Internet**: Kết nối internet để lấy dữ liệu thời tiết
- **Quyền**: 
  - Vị trí (để lấy thời tiết tại vị trí hiện tại)
  - Internet
  - Thông báo (tùy chọn)

## 📦 Cài đặt

### Yêu cầu
- Android Studio Hedgehog | 2023.1.1 trở lên
- JDK 17
- Android SDK
- Google Maps API Key (cho tính năng bản đồ)

### Các bước cài đặt

1. **Clone repository**
```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd App
```

2. **Mở project trong Android Studio**
   - Mở Android Studio
   - Chọn `Open an existing project`
   - Chọn thư mục `App`

3. **Cấu hình Google Maps API Key**
   - Tạo API key tại [Google Cloud Console](https://console.cloud.google.com/)
   - Mở file `app/src/main/AndroidManifest.xml`
   - Thay thế `YOUR_GOOGLE_MAPS_API_KEY` bằng API key của bạn:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_GOOGLE_MAPS_API_KEY" />
   ```

4. **Cấu hình Weather API**
   - Mở file `app/src/main/java/com/example/app/network/ApiClient.java`
   - Thay thế `YOUR_WEATHER_API_KEY` bằng API key của bạn

5. **Sync Gradle**
   - Android Studio sẽ tự động sync
   - Hoặc chọn `File > Sync Project with Gradle Files`

6. **Build và chạy**
   - Kết nối thiết bị Android hoặc khởi động emulator
   - Nhấn `Run` hoặc `Shift + F10`

## 🏗️ Cấu trúc Project

```
App/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/app/
│   │   │   │   ├── activities/          # Các Activity
│   │   │   │   ├── adapters/            # RecyclerView Adapters
│   │   │   │   ├── admin/               # Admin Panel Fragments
│   │   │   │   ├── database/            # Database Helpers
│   │   │   │   ├── models/              # Data Models
│   │   │   │   ├── network/             # API Services
│   │   │   │   ├── services/            # Background Services
│   │   │   │   ├── utils/               # Utility Classes
│   │   │   │   └── widget/              # App Widget
│   │   │   ├── res/                     # Resources
│   │   │   │   ├── layout/              # XML Layouts
│   │   │   │   ├── drawable/            # Icons & Drawables
│   │   │   │   ├── values/              # Strings, Colors, Themes
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml
│   │   └── test/                        # Unit Tests
│   └── build.gradle.kts                 # App Dependencies
├── build.gradle.kts                     # Project Dependencies
├── settings.gradle.kts
└── README.md
```

## 🧩 Công nghệ sử dụng

### Core
- **Kotlin/Java**: Ngôn ngữ lập trình
- **Android SDK**: Android development framework

### UI/UX
- **Material Design**: Google Material Design Components
- **CardView**: Hiển thị thông tin dạng card
- **RecyclerView**: Danh sách hiệu năng cao
- **SwipeRefreshLayout**: Pull to refresh
- **Lottie**: Animations

### Networking
- **Retrofit 2**: REST API client
- **Gson**: JSON parsing
- **OkHttp**: HTTP client với logging interceptor

### Maps & Location
- **Google Maps SDK**: Hiển thị bản đồ
- **Google Play Services Location**: Lấy vị trí

### Charts
- **MPAndroidChart**: Biểu đồ thời tiết

### Image Loading
- **Glide**: Load và cache hình ảnh

### Database
- **SQLite**: Local database (UserDatabaseHelper, WeatherDatabaseHelper)

## 📱 Sử dụng

### Lần đầu sử dụng
1. Mở ứng dụng
2. Cấp quyền vị trí (nếu muốn sử dụng vị trí hiện tại)
3. Đăng ký tài khoản hoặc đăng nhập
4. Ứng dụng sẽ tự động lấy thời tiết tại vị trí của bạn

### Các tính năng chính
- **Xem thời tiết**: Màn hình chính hiển thị thời tiết hiện tại
- **Tìm kiếm**: Nhấn vào ô tìm kiếm để tìm thành phố
- **Bản đồ**: Mở menu và chọn "Bản đồ thời tiết"
- **Biểu đồ**: Mở menu và chọn "Biểu đồ"
- **Lịch**: Mở menu và chọn "Lịch thời tiết"
- **Cài đặt**: Mở menu và chọn "Cài đặt"

## 🔧 Cấu hình

### Thay đổi API Key
1. **Google Maps API Key**: `app/src/main/AndroidManifest.xml`
2. **Weather API Key**: `app/src/main/java/com/example/app/network/ApiClient.java`

### Tùy chỉnh Theme
- Màu sắc: `app/src/main/res/values/colors.xml`
- Theme: `app/src/main/res/values/themes.xml`
- Dark theme: `app/src/main/res/values-night/themes.xml`

## 🤝 Đóng góp

Mọi đóng góp đều được chào đón! Vui lòng:

1. Fork project
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

## 📄 License

Project này được phân phối dưới giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.

## 👨‍💻 Tác giả

Được phát triển bởi [Tên của bạn]

## 📞 Liên hệ

Nếu có câu hỏi hoặc đề xuất, vui lòng tạo issue trên GitHub.

---

⭐ Nếu project này hữu ích, hãy cho một star!

