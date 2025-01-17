
$(document).ready(function () {
    let isClick = false;  // Khởi tạo biến isClick là false

    // Toggle dropdown menu khi nhấn vào nút tài khoản
    $("#accountButton").click(function (e) {
        e.stopPropagation();  // Ngừng sự kiện click để tránh đóng dropdown ngay khi bấm nút tài khoản
        isClick = !isClick; // Thay đổi giá trị isClick
        if (isClick) {
            $("#dropdownMenu").css("display", "block");  // Hiển thị menu
        } else {
            $("#dropdownMenu").css("display", "none");  // Ẩn menu
        }
    });

    // Đóng menu khi click ra ngoài
    $(document).click(function (event) {
        if (isClick && !$(event.target).closest("#accountButton, #dropdownMenu").length) {
            console.log('Clicked outside, hiding dropdown');
            isClick = false;  // Đặt lại isClick về false
            $("#dropdownMenu").css("display", "none");  // Ẩn menu
        }
    });
});




//logout

function confirmLogout(event) {
      event.preventDefault(); // Ngăn hành động mặc định của nút
      if (confirm("Bạn có chắc chắn muốn đăng xuất không?")) {
          document.getElementById("logoutForm").submit(); // Gửi form nếu người dùng chọn OK
      }
  }