import "dotenv/config";
const express = require("express");
const morgan = require("morgan");
const cors = require("cors");
const { upload } = require("./tools/multer");

const app = express();

app.use(cors());
app.use(morgan("dev"));
app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  // update to match the domain you will make the request from
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  next();
});

const doUpload = upload.array("image_upload");

app.post("/image", (req, res) => {
  doUpload(req, res, (err) => {
    if (err) {
      console.log(err);
      return res.status(400).json({ success: false, err });
    } else return res.json({ success: true, fileName: req.tempFileName });
  });
});

app.get("/", (req, res) => {
  res.send("Hello world");
});

app.listen(8081, () => {
  console.log("✅ Image server running on 8081");
});
