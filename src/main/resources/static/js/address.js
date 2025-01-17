document.addEventListener('DOMContentLoaded', function() {
    // Tải dữ liệu từ file JSON (giả sử file JSON nằm trong thư mục static của Spring Boot)
    fetch('/data/addresses.json')  // URL của file JSON
        .then(response => response.json())
        .then(data => {
            // Gán dữ liệu các tỉnh/thành phố vào dropdown tỉnh
            const provincesSelect = document.getElementById('province');
            data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.Id;
                option.textContent = province.Name;
                provincesSelect.appendChild(option);
            });

            // Lắng nghe sự kiện thay đổi tỉnh/thành phố
            provincesSelect.addEventListener('change', function() {
                updateDistricts(data, this.value);
            });
        })
        .catch(error => console.error('Lỗi khi tải dữ liệu địa chỉ:', error));

    // Cập nhật danh sách Quận/Huyện khi tỉnh thay đổi
    function updateDistricts(data, provinceId) {
        const districtSelect = document.getElementById('district');
        districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>'; // Reset danh sách quận

        // Tìm tỉnh trong dữ liệu JSON
        const province = data.find(prov => prov.Id === provinceId);
        if (province) {
            province.Districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.Id;
                option.textContent = district.Name;
                districtSelect.appendChild(option);
            });
        }

        // Reset danh sách phường/xã khi thay đổi quận/huyện
        const wardSelect = document.getElementById('ward');
        wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
    }

    // Lắng nghe sự kiện thay đổi Quận/Huyện
    document.getElementById('district').addEventListener('change', function() {
        const provinceId = document.getElementById('province').value;
        const districtId = this.value;
        updateWards(provinceId, districtId);
    });

    // Cập nhật danh sách Phường/Xã khi Quận/Huyện thay đổi
    function updateWards(provinceId, districtId) {
        const wardSelect = document.getElementById('ward');
        wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>'; // Reset danh sách phường

        fetch('/data/addresses.json')
            .then(response => response.json())
            .then(data => {
                const province = data.find(prov => prov.Id === provinceId);
                if (province) {
                    const district = province.Districts.find(d => d.Id === districtId);
                    if (district) {
                        district.Wards.forEach(ward => {
                            const option = document.createElement('option');
                            option.value = ward.Id;
                            option.textContent = ward.Name;
                            wardSelect.appendChild(option);
                        });
                    }
                }
            })
            .catch(error => console.error('Lỗi khi tải dữ liệu phường/xã:', error));
    }
});
