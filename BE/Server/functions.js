const fs = require("fs");
const path = require('path');
function getDeviceById(deviceId, callback) {
  fs.readFile("../Data/devices.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      const jsonData = JSON.parse(data);
      const devices = jsonData.devices;
      const device = devices.find((d) => d.id === deviceId);
      callback(device);
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
      callback(null);
    }
  });
}

function updateDeviceById(deviceId, newData) {
  fs.readFile("../Data/devices.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Tìm kiếm thiết bị theo ID và cập nhật dữ liệu mới
      const updatedData = jsonData.devices.map((device) => {
        if (device.device_id === deviceId) {
          return { ...device, ...newData };
        }
        return device;
      });
      // Cập nhật dữ liệu mới
      jsonData.devices = updatedData;
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/devices.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
          } else {
            console.log("Dữ liệu đã được cập nhật thành công.");
          }
        }
      );
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
    }
  });
}

function addDevice(newDevice) {
  fs.readFile("../Data/devices.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Thêm thiết bị mới vào mảng devices
      jsonData.devices.push({
        ...newDevice
      });
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/devices.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
          } else {
            console.log("Thiết bị đã được thêm vào thành công.");
          }
        }
      );
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
    }
  });
}
function addScheduler(newSheduler) {
  fs.readFile("../Data/relay_events.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Thêm thiết bị mới vào mảng devices
      jsonData.relay_events.push({...newSheduler,status:true}
       
    );
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/relay_events.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
          } else {
            console.log("Thiết bị đã được thêm vào thành công.");
          }
        }
      );
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
    }
  });
}
function updateSchedulerStateById(newData) {
  fs.readFile("../Data/relay_events.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Tìm kiếm thiết bị theo ID và cập nhật dữ liệu mới
      const updatedData = jsonData.relay_events.map((scheduler) => {
        if (scheduler.scheduler_id === newData.scheduler_id) {
          return { ...scheduler,status:newData.status };
        }
        return scheduler;
      });
      // Cập nhật dữ liệu mới
      jsonData.relay_events = updatedData;
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/relay_events.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
          } else {
            console.log("Dữ liệu đã được cập nhật thành công.");
          }
        }
      );
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
    }
  });
}
function updateSchedulerById(newData) {
  fs.readFile("../Data/relay_events.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Tìm kiếm thiết bị theo ID và cập nhật dữ liệu mới
      const updatedData = jsonData.relay_events.map((scheduler) => {
        if (scheduler.scheduler_id === newData.scheduler_id) {
          return { ...scheduler,event_type:newData.event_type,time_selected:newData.time_selected,repeat_type:newData.repeat_type,days_repeat:newData.days_repeat };
        }
        return scheduler;
      });
      // Cập nhật dữ liệu mới
      jsonData.relay_events = updatedData;
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/relay_events.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
          } else {
            console.log("Dữ liệu đã được cập nhật thành công.");
          }
        }
      );
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
    }
  });
}
function getAllScheduler(callback) {
  fs.readFile("../Data/relay_events.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Trả về mảng các thiết bị
      const relay_events = jsonData.relay_events;
      // console.log("Danh sách thiết bị:", devices);
      callback(relay_events);
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
      callback(null);
    }
  });
}

function getAllDevices(callback) {
  fs.readFile("../Data/devices.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Trả về mảng các thiết bị
      const devices = jsonData.devices;
      // console.log("Danh sách thiết bị:", devices);
      callback(devices);
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
      callback(null);
    }
  });
}

function deleteDeviceById(idToDelete, callback) {
  fs.readFile("../Data/devices.json", "utf8", (err, data) => {
    if (err) {
      console.error("Lỗi khi đọc tệp JSON:", err);
      callback(false);
      return;
    }
    try {
      // Phân tích cú pháp dữ liệu JSON
      const jsonData = JSON.parse(data);
      // Lọc ra thiết bị cần xóa
      const updatedDevices = jsonData.devices.filter(
        (device) => device.id !== idToDelete
      );
      // Kiểm tra xem có thiết bị nào bị xóa không
      if (updatedDevices.length === jsonData.devices.length) {
        console.log("Không tìm thấy thiết bị với ID:", idToDelete);
        callback(false);
        return;
      }
      // Cập nhật danh sách thiết bị mới
      jsonData.devices = updatedDevices;
      // Ghi lại dữ liệu vào tệp JSON
      fs.writeFile(
        "../Data/devices.json",
        JSON.stringify(jsonData, null, 2),
        "utf8",
        (err) => {
          if (err) {
            console.error("Lỗi khi ghi lại tệp JSON:", err);
            callback(false);
            return;
          }
          console.log("Thiết bị đã được xóa thành công.");
          const file1Path = "../Data/DataChart/" + idToDelete + ".json";
          const file2Path = "../Data/DataParameter/" + idToDelete + ".json";
          const file3Path = "../Data/DataHistory/" + idToDelete + ".json";

          fs.unlink(file3Path, (err) => {
            if (err) {
              console.error(`Lỗi khi xóa ${file1Path}:`, err);
              callback(false);
              return;
            }
            console.log(`${file1Path} đã được xóa thành công.`);
            // Sau khi xóa file1, xóa file2
            callback(true);
          });

          fs.unlink(file1Path, (err) => {
            if (err) {
              console.error(`Lỗi khi xóa ${file1Path}:`, err);
              callback(false);
              return;
            }
            console.log(`${file1Path} đã được xóa thành công.`);
            // Sau khi xóa file1, xóa file2
            callback(true);
          });
          fs.unlink(file2Path, (err) => {
            if (err) {
              console.error(`Lỗi khi xóa ${file2Path}:`, err);
              callback(false);
              return;
            }
            console.log(`${file2Path} đã được xóa thành công.`);
            callback(true);
          });
          fs.readdir("../Data/DataHoursDevice/", (err, files) => {
            if (err) {
              console.error("Lỗi khi đọc thư mục:", err);
              callback(false);
              return;
            }

            let filesDeleted = 0;
            files.forEach((file) => {
              // Kiểm tra xem tên tệp có chứa idToDelete và có phần mở rộng là ".json" không
              if (file.includes(idToDelete) && file.endsWith(".json")) {
                const filePath = path.join("../Data/DataHoursDevice/", file);
                fs.unlink(filePath, (err) => {
                  if (err) {
                    console.error(`Lỗi khi xóa ${file2Path}:`, err);
                    callback(false);
                    return;
                  }
                  console.log(`${file2Path} đã được xóa thành công.`);
                  callback(true);
                });
                console.log(`${filePath} đã được xóa thành công.`);
                filesDeleted++;
              }
            });

            // Gọi callback với kết quả, số lượng tệp đã xóa và thành công nếu số lượng lớn hơn 0, ngược lại trả về false
            callback(filesDeleted > 0, filesDeleted);
          });
          fs.readdir("../Data/DataKWhDevice/", (err, files) => {
            if (err) {
              console.error("Lỗi khi đọc thư mục:", err);
              callback(false);
              return;
            }

            let filesDeleted = 0;
            files.forEach((file) => {
              // Kiểm tra xem tên tệp có chứa idToDelete và có phần mở rộng là ".json" không
              if (file.includes(idToDelete) && file.endsWith(".json")) {
                const filePath = path.join("../Data/DataKWhDevice/", file);
                fs.unlink(filePath, (err) => {
                  if (err) {
                    console.error(`Lỗi khi xóa ${file2Path}:`, err);
                    callback(false);
                    return;
                  }
                  console.log(`${file2Path} đã được xóa thành công.`);
                  callback(true);
                });
                console.log(`${filePath} đã được xóa thành công.`);
                filesDeleted++;
              }
            });

            // Gọi callback với kết quả, số lượng tệp đã xóa và thành công nếu số lượng lớn hơn 0, ngược lại trả về false
            callback(filesDeleted > 0, filesDeleted);
          });
        }
      );

      callback(true);
    } catch (error) {
      console.error("Lỗi khi phân tích cú pháp JSON:", error);
      callback(false);
    }
  });
}
function addHistory(newHistory) {
  fs.readFile(
    "../Data/DataHistory/" + newHistory.id + ".json",
    "utf8",
    (err, data) => {
      if (err) {
        console.error("Lỗi khi đọc tệp JSON:", err);
        return;
      }
      try {
        // Phân tích cú pháp dữ liệu JSON
        const jsonData = JSON.parse(data);
        // Thêm thiết bị mới vào mảng devices
        var currentDate = new Date();

        // Lấy giờ và phút hiện tại
        var currentHour = currentDate.getHours();
        var currentMinute = currentDate.getMinutes();

        // Lấy ngày, tháng và năm hiện tại
        var currentDay = currentDate.getDate();
        var currentMonth = currentDate.getMonth() + 1; // Tháng trong JavaScript bắt đầu từ 0
        var currentYear = currentDate.getFullYear();

        // Định dạng thời gian theo "10:10 06/04/2024"
        var formattedTime =
          (currentHour < 10 ? "0" : "") +
          currentHour +
          ":" +
          (currentMinute < 10 ? "0" : "") +
          currentMinute +
          " " +
          (currentDay < 10 ? "0" : "") +
          currentDay +
          "/" +
          (currentMonth < 10 ? "0" : "") +
          currentMonth +
          "/" +
          currentYear;
        jsonData.histories.push({
          id: newHistory.id,
          name: newHistory.name,
          date: formattedTime,
          description: newHistory.description,
        });
        // Ghi lại dữ liệu vào tệp JSON
        fs.writeFile(
          "../Data/DataHistory/" + newHistory.id + ".json",
          JSON.stringify(jsonData, null, 2),
          "utf8",
          (err) => {
            if (err) {
              console.error("Lỗi khi ghi lại tệp JSON:", err);
            } else {
              console.log("Thiết bị đã được thêm vào thành công.");
            }
          }
        );
      } catch (error) {
        console.error("Lỗi khi phân tích cú pháp JSON:", error);
      }
    }
  );
}

module.exports = {
  getDeviceById,
  updateDeviceById,
  addDevice,
  getAllDevices,
  deleteDeviceById,
  addHistory,
  addScheduler,
  updateSchedulerById,
  updateSchedulerStateById,
  getAllScheduler
};
