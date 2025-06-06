package vn.utc.service.config;

public enum ResponseCode {
    EXISTS_DATA(){
        @Override
        public String getMessage() {
            return "Dữ liệu đã tồn tại";
        }
    },
    ERROR(){
        @Override
        public String getMessage() {
            return "Có lỗi phát sinh";
        }
    },
    NOT_FOUND(){
        @Override
        public String getMessage() {
            return "Không tìm thấy dữ liệu";
        }
    },
    ALREADY_CHECKIN(){
        @Override
        public String getMessage() {
            return "Đã thực hiện checkin. Không thể thực hiện được checkin thêm.";
        }
    },
    SUCCESS(){
        @Override
        public String getMessage() {
            return "Thành công";
        }
    };

    public abstract String getMessage();
}
