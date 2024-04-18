const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const swaggerUi = require("swagger-ui-express");
const YAML = require("yamljs");
const app = express();
const fs = require("fs");
const { PythonShell } = require("python-shell");

const {
  getDeviceById,
  updateDeviceById,
  addDevice,
  getAllDevices,
  deleteDeviceById,
  addHistory,
  addScheduler,
  updateSchedulerById,
  getAllScheduler,
  updateSchedulerStateById,
} = require("./functions");

app.use(cors());
app.use((req, res, next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
  res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
  next();
});
// Cấu hình middleware
app.use(express.json()); // Cho phép xử lý dữ liệu dạng JSON trong yêu cầu

// Kết nối tới cơ sở dữ liệu MongoDB
mongoose.connect("mongodb://localhost:27017/artemis", {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

const userRoutes = require("./Routes/userRoute");
const friendRoutes = require("./Routes/friendRoute");

app.use("/user", userRoutes);
app.use("/friend", friendRoutes);
// Xử lý tuyến đường mặc định
app.get("/", (req, res) => {
  console.log("check");
  res.status(200).json({ message: "Server hoạt động" });
});
app.get("/data/chart/:id", (req, res) => {
  try {
    const { id } = req.params;
    const rawData = fs.readFileSync("../Data/DataChart/" + id + ".json");
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    console.error("Đã xảy ra lỗi:", error);
    res.status(500).json({ error: "Đã xảy ra lỗi khi lấy dữ liệu" });
  }
  // res.status(200).json({current:[[1,20],[2,21],[3,21],[4,22]],voltage:[[1,220],[2,221],[3,225],[4,221]],active:[[1,65],[2,66],[3,65],[4,68]],apparent:[[1,100],[2,102],[3,102],[4,100]]});
});
app.get("/data/parameter/:id", (req, res) => {
  try {
    const { id } = req.params;
    const rawData = fs.readFileSync("../Data/DataParameter/" + id + ".json");
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    console.error("Đã xảy ra lỗi:", error);
    res.status(500).json({ error: "Đã xảy ra lỗi khi lấy dữ liệu" });
  }
  // res.status(200).json({current:[[1,20],[2,21],[3,21],[4,22]],voltage:[[1,220],[2,221],[3,225],[4,221]],active:[[1,65],[2,66],[3,65],[4,68]],apparent:[[1,100],[2,102],[3,102],[4,100]]});
});
app.get("/data/hours/all", (req, res) => {
  console.log("all");
  try {
    const month = req.query.month;
    const year = req.query.year;
    const rawData = fs.readFileSync(
      "../Data/DataHours/" + month + "_" + year + ".json"
    );
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    res.status(200).json({ data: [] });
  }
  // Xử lý yêu cầu
  // Ví dụ: lấy dữ liệu từ cơ sở dữ liệu dựa trên month và year

  // Trả về kết quả
});
app.get("/data/kwh/all", (req, res) => {
  console.log("all");
  try {
    const month = req.query.month;
    const year = req.query.year;
    const rawData = fs.readFileSync(
      "../Data/DataKWh/" + month + "_" + year + ".json"
    );
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    res.status(200).json({ data: [] });
  }
  // Xử lý yêu cầu
  // Ví dụ: lấy dữ liệu từ cơ sở dữ liệu dựa trên month và year

  // Trả về kết quả
});
app.get("/data/kwh", (req, res) => {
  console.log("all");
  try {
    const id = req.query.id;
    const month = req.query.month;
    const year = req.query.year;
    const rawData = fs.readFileSync(
      "../Data/DataKWhDevice/" + month + "_" + year + "_" + id + ".json"
    );
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    res.status(200).json({ data: [] });
  }
  // Xử lý yêu cầu
  // Ví dụ: lấy dữ liệu từ cơ sở dữ liệu dựa trên month và year

  // Trả về kết quả
});
app.get("/data/hours", (req, res) => {
  console.log("all");
  try {
    const id = req.query.id;
    const month = req.query.month;
    const year = req.query.year;
    const rawData = fs.readFileSync(
      "../Data/DataHoursDevice/" + month + "_" + year + "_" + id + ".json"
    );
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    res.status(200).json({ data: [] });
  }
  // Xử lý yêu cầu
  // Ví dụ: lấy dữ liệu từ cơ sở dữ liệu dựa trên month và year

  // Trả về kết quả
});
app.get("/data/history", (req, res) => {
  try {
    const id = req.query.id;

    const rawData = fs.readFileSync("../Data/DataHistory/" + id + ".json");
    const data = JSON.parse(rawData);
    res.status(200).json(data);
  } catch (error) {
    console.error("Đã xảy ra lỗi:", error);
    res.status(500).json({ error: "Đã xảy ra lỗi khi lấy dữ liệu" });
  }
  // Xử lý yêu cầu
  // Ví dụ: lấy dữ liệu từ cơ sở dữ liệu dựa trên month và year

  // Trả về kết quả
});
// Khởi chạy server
const port = process.env.PORT || 5000;
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});

const mosca = require("mosca");
const { stringify } = require("querystring");
// Cấu hình MQTT server
const settings = {
  port: 1883, // Cổng mặc định của MQTT
};

// Khởi tạo MQTT server
const server = new mosca.Server(settings);

// Sự kiện khi MQTT server được khởi động
server.on("ready", function () {
  console.log("Mosca MQTT server đang chạy trên cổng " + settings.port);
});

// Sự kiện khi một client kết nối đến MQTT server
server.on("clientConnected", function (client) {
  console.log("Client kết nối:", client.id);
});

// Sự kiện khi một client ngắt kết nối từ MQTT server
server.on("clientDisconnected", function (client) {
  console.log("Client ngắt kết nối:", client.id);
});
// Sự kiện khi nhận được dữ liệu từ client
server.on("published", function (packet, client) {
  if (packet.topic == "device-status") {
    console.log(
      "-------------------------------------------------------------------------"
    );

    getDeviceById(packet.payload.toString(), function (device) {
      if (device) {
        console.log("Thông tin thiết bị:", device);
        server.publish({
          topic: "device-status-reply",
          payload: JSON.stringify(device),
        });
      } else {
        console.log(
          "Không tìm thấy thiết bị có ID:",
          packet.payload.toString()
        );
      }
      console.log(
        "-------------------------------------------------------------------------"
      );
    });
  }

  if (packet.topic == "smart-plug.relay") {
    console.log(
      "-------------------------------------------------------------------------"
    );
    try {
      // Chuyển đổi dữ liệu từ buffer sang chuỗi
      const dataString = packet.payload.toString();
      console.log(packet.payload.toString());
      // Phân tích chuỗi JSON để lấy thông tin
      const data = JSON.parse(dataString);
      // Kiểm tra xem dữ liệu có đúng định dạng hay không
      if (data.device_id && data.status) {
        console.log("Thông tin nhận được:", data);
        // Xử lý thông tin ở đây, ví dụ:
        console.log("ID:", data.device_id);
        console.log("Trạng thái:", data.status);
        server.publish({
          topic: "smart-plug.relay-reply",
          payload: JSON.stringify({
            device_id: data.device_id,
            status: data.status,
          }),
        });
        // Gửi thông báo hoặc xử lý tiếp theo tùy thuộc vào dữ liệu
      } else {
        console.log("Dữ liệu nhận được không hợp lệ:", dataString);
      }
    } catch (error) {
      console.error("Lỗi khi xử lý dữ liệu nhận được:", error);
    }
    console.log(
      "-------------------------------------------------------------------------"
    );
  }
  if (packet.topic == "smart-plug.limit") {
    console.log(
      "-------------------------------------------------------------------------"
    );
    console.log(packet.payload.toString());
    try {
      // Chuyển đổi dữ liệu từ buffer sang chuỗi
      const dataString = packet.payload.toString();
      // Phân tích chuỗi JSON để lấy thông tin
      const data = JSON.parse(dataString);
      // Kiểm tra xem dữ liệu có đúng định dạng hay không
      if (data.device_id && data.value_limit) {
        console.log("Thông tin nhận được:", data);
        // Xử lý thông tin ở đây, ví dụ:
        console.log("ID:", data.device_id);
        console.log("Trạng thái Limit:", data.state_limit);
        console.log("Giá trị Limit:", data.value_limit);
        const description =
          "Cập nhật giới hạn kWh là " +
          data.value_limit.toString() +
          " kWh, và ở chế độ " +
          (data.state_limit ? "bật" : "tắt");
        addHistory({
          id: data.id,
          name: "Update limit",
          description: description,
        });
        updateDeviceById(data.device_id, {
          value_limit: data.value_limit,
          state_limit: data.state_limit,
        });
        server.publish({
          topic: "smart-plug.limit-reply",
          payload: JSON.stringify({
            message: "success",
            device_id:data.device_id,
            value_limit: data.value_limit,
            state_limit: data.state_limit,
          }),
        });
        // Gửi thông báo hoặc xử lý tiếp theo tùy thuộc vào dữ liệu
      } else {
        console.log("Dữ liệu nhận được không hợp lệ:", dataString);
        server.publish({
          topic: "smart-plug.limit-reply",
          payload: JSON.stringify({ message: "failed" }),
        });
      }
    } catch (error) {
      console.error("Lỗi khi xử lý dữ liệu nhận được:", error);
      server.publish({
        topic: "smart-plug.limit-reply",
        payload: JSON.stringify({ message: "failed" }),
      });
    }
    console.log(
      "-------------------------------------------------------------------------"
    );
  }

  if (packet.topic == "smart-plug.relay-reply") {
    console.log(
      "-------------------------------------------------------------------------"
    );
    console.log(packet.payload.toString());
    try {
      // Chuyển đổi dữ liệu từ buffer sang chuỗi
      const dataString = packet.payload.toString();
      // Phân tích chuỗi JSON để lấy thông tin
      const data = JSON.parse(dataString);
      // Kiểm tra xem dữ liệu có đúng định dạng hay không
      if (data.device_id && data.status) {
        console.log("Thông tin nhận được:", data);
        // Xử lý thông tin ở đây, ví dụ:
        console.log("ID:", data.device_id);
        console.log("Trạng thái:", data.status);
        const description =
          "Cập nhật relay ở chế độ " + (data.status == "on" ? "bật" : "tắt");
        addHistory({
          id: data.device_id,
          name: "Update relay",
          description: description,
        });
        updateDeviceById(data.device_id, { relay_status: data.status });
        // Gửi thông báo hoặc xử lý tiếp theo tùy thuộc vào dữ liệu
      } else {
        console.log("Dữ liệu nhận được không hợp lệ:", dataString);
      }
    } catch (error) {
      console.error("Lỗi khi xử lý dữ liệu nhận được:", error);
    }
    console.log(
      "-------------------------------------------------------------------------"
    );
  }

  if (packet.topic == "smart-plug.all-data") {
    console.log("aa");
  }
  if (packet.topic == "smart-plug.device-name") {
    console.log(packet.payload.toString());
  }

  if (packet.topic == "smart-plug.add-device") {
    try {
      // Chuyển đổi dữ liệu từ buffer sang chuỗi
      const dataString = packet.payload.toString();
      // Phân tích chuỗi JSON để lấy thông tin
      const data = JSON.parse(dataString);
      // Kiểm tra xem dữ liệu có đúng định dạng hay không
      if (data.device_id && data.device_name && data.room_name && data.value_limit) {
        console.log("Thông tin nhận được:", data);
        // Xử lý thông tin ở đây, ví dụ:
        console.log("ID:", data.device_id);
        console.log("Name:", data.device_name);
        console.log("Room:", data.room_name);
        console.log("Value:", data.value_limit);
        addDevice({ device_id: data.device_id,device_name : data.device_name, room_name: data.room_name, value_limit: data.value_limit,state_limit:false,relay_status:'off'});
        server.publish({
          topic: "smart-plug.add-device-reply",
          payload: JSON.stringify({ message: "success",device_id: data.device_id }),
        });
        fs.writeFile(
          "../Data/DataChart/" + data.device_id + ".json",
          JSON.stringify({
            current: [0],
            voltage: [0],
            active: [0],
            apparent: [0],
          }),
          "utf8",
          (err) => {
            if (err) {
              console.error("Lỗi khi ghi vào tệp JSON:", err);
              return;
            }
            console.log("Dữ liệu đã được ghi vào tệp JSON thành công.");
          }
        );
        fs.writeFile(
          "../Data/DataParameter/" + data.device_id + ".json",
          JSON.stringify({
            current: 0,
            voltage: 0,
            active: 0,
            apparent: 0,
            predict: "Unknown",
          }),
          "utf8",
          (err) => {
            if (err) {
              console.error("Lỗi khi ghi vào tệp JSON:", err);
              return;
            }
            console.log("Dữ liệu đã được ghi vào tệp JSON thành công.");
          }
        );
        fs.writeFile(
          "../Data/DataHistory/" + data.device_id + ".json",
          JSON.stringify({
            histories: [],
          }),
          "utf8",
          (err) => {
            if (err) {
              console.error("Lỗi khi ghi vào tệp JSON:", err);
              return;
            }
            console.log("Dữ liệu đã được ghi vào tệp JSON thành công.");
          }
        );
        // Gửi thông báo hoặc xử lý tiếp theo tùy thuộc vào dữ liệu
      } else {
        server.publish({
          topic: "smart-plug.add-device-reply",
          payload: JSON.stringify({ message: "failed" }),
        });
        console.log("Dữ liệu nhận được không hợp lệ:", dataString);
      }
    } catch (error) {
      server.publish({
        topic: "smart-plug.add-device-reply",
        payload: JSON.stringify({ message: "failed" }),
      });
      console.error("Lỗi khi xử lý dữ liệu nhận được:", error);
    }
    console.log(
      "-------------------------------------------------------------------------"
    );
  }
  if (packet.topic == "smart-plug.delete-device") {
    try {
      // Chuyển đổi dữ liệu từ buffer sang chuỗi
      const dataString = packet.payload.toString();
      // Phân tích chuỗi JSON để lấy thông tin
      const data = JSON.parse(dataString);
      // Kiểm tra xem dữ liệu có đúng định dạng hay không
      if (data.id) {
        console.log("Thông tin nhận được:", data);
        // Xử lý thông tin ở đây, ví dụ:
        console.log("ID:", data.id);
        deleteDeviceById(data.id, (success) => {
          if (success) {
            console.log("Xóa thiết bị thành công.");
            server.publish({
              topic: "smart-plug.delete-device-reply",
              payload: JSON.stringify({ message: "success" }),
            });
          } else {
            console.log("Xóa thiết bị không thành công.");
            server.publish({
              topic: "smart-plug.delete-device-reply",
              payload: JSON.stringify({ message: "failed" }),
            });
          }
        });

        // Gửi thông báo hoặc xử lý tiếp theo tùy thuộc vào dữ liệu
      } else {
        server.publish({
          topic: "smart-plug.delete-device-reply",
          payload: JSON.stringify({ message: "failed" }),
        });
        console.log("Dữ liệu nhận được không hợp lệ:", dataString);
      }
    } catch (error) {
      server.publish({
        topic: "smart-plug.delete-device-reply",
        payload: JSON.stringify({ message: "failed" }),
      });
      console.error("Lỗi khi xử lý dữ liệu nhận được:", error);
    }
    console.log(
      "-------------------------------------------------------------------------"
    );
  }
  if (packet.topic == "smart-plug.async") {
    if (packet.payload.toString() == "async") {
      // Lấy tất cả scheduler
      getAllScheduler((schedulers) => {

        getAllDevices((devices) => {
        
          // Xử lý kết quả ở đây
          console.log("Lấy thành công tất cả scheduler thành công", schedulers);
          console.log("Lấy thành công tất cả device thành công", devices);

          // Gửi kết quả về client
          server.publish({
            topic: "smart-plug.async-reply",
            payload: JSON.stringify({
              message: "success",
              schedulers: schedulers,
              devices: devices,
            }),
          });
        });
      });
    }
  }
  if (packet.topic == "smart-plug.add-scheduler") {
    const dataString = packet.payload.toString();
    console.log(dataString);
    // Phân tích chuỗi JSON để lấy thông tin
    const data = JSON.parse(dataString);
    addScheduler(data);
    server.publish({
      topic: "smart-plug.add-scheduler-reply",
      payload: JSON.stringify({
        scheduler_id: data.scheduler_id,
        device_id: data.device_id,
        message: "success",
      }),
    });

    //   let description = (data.mode == "new" ? "Thêm hoạt động" : "Sửa hoạt động thành") + (data.event_type == 1 ? " bật" : " tắt") + " relay lúc " + data.time_selection + " ở chế độ "+ (data.repeat_type === "daily" ? "một lần":"hằng ngày vào")
    //   for (let i = 0; i < data.days_repeat.length; i++) {
    //     if (data.days_repeat[i] ==="Mon")
    //       description += " Thứ hai" ;
    //     if ((data.days_repeat[i] ==="Tue"))
    //       description += " Thứ ba" ;
    //       if ((data.days_repeat[i] ==="Wed"))
    //       description += " Thứ tư" ;
    //       if ((data.days_repeat[i] ==="Thu"))
    //       description += " Thứ năm" ;
    //       if ((data.days_repeat[i] ==="Fri"))
    //       description += " Thứ sáu" ;
    //       if ((data.days_repeat[i] ==="Sat"))
    //       description += " Thứ bảy" ;
    //       if ((data.days_repeat[i] ==="Sun"))
    //       description += " Chủ nhật" ;

    //
    //   addHistory({id :data.device_id,name:(data.mode == "new" ? "New scheduler" : "Update Scheduler"),description:description})
  }
  if (packet.topic == "smart-plug.update-scheduler") {
    const dataString = packet.payload.toString();
    console.log(dataString);
    const data = JSON.parse(dataString);
    updateSchedulerById(data);
    server.publish({
      topic: "smart-plug.update-scheduler-reply",
      payload: JSON.stringify({
        scheduler_id: data.scheduler_id,
        message: "success",
      }),
    });

    // server.publish({topic:"smart-plug.update-scheduler-reply",payload:JSON.stringify({scheduler_id:data.scheduler_id,device_id:data.device_id,message:"success"})})
  }
  if (packet.topic == "smart-plug.update-scheduler-status") {
    const dataString = packet.payload.toString();
    console.log(dataString);
    const data = JSON.parse(dataString);
    updateSchedulerStateById(data);
    server.publish({
      topic: "smart-plug.update-scheduler-status-reply",
      payload: JSON.stringify({
        scheduler_id: data.scheduler_id,
        status: data.status,
        message: "success",
      }),
    });

    // server.publish({topic:"smart-plug.update-scheduler-reply",payload:JSON.stringify({scheduler_id:data.scheduler_id,device_id:data.device_id,message:"success"})})
  }
  if (packet.topic == "smart-plug.delete-scheduler-id") {
    const dataString = packet.payload.toString();
    console.log(dataString);
  }
});

// const { exec } = require("child_process");

// // Đường dẫn đến file Python
// const pythonScript = "../model.py";
// const pythonScript1 = "../scheduler.py";
// // Chạy file Python
// exec(`python ${pythonScript}`, (error, stdout, stderr) => {
//   if (error) {
//     console.error(`Lỗi khi chạy file Python: ${error.message}`);
//     return;
//   }
//   if (stderr) {
//     console.error(`Lỗi tiêu chuẩn từ Python: ${stderr}`);
//     return;
//   }
//   console.log(`Kết quả từ Python: ${stdout}`);
// });
