const multer = require("multer");
const multerS3 = require("multer-s3");
const AWS = require("aws-sdk");
const crypto = require("crypto");
const path = require("path");

AWS.config.update({
  accessKeyId: process.env.ACCESS_KEY_ID,
  secretAccessKey: process.env.SECRET_ACCESS_KEY,
  region: process.env.REGION,
});

const s3 = new AWS.S3();

const fileValidate = (req, file, cb) => {
  const regex = /^image\/*/;
  if (regex.test(file.mimetype)) {
    cb(null, true);
  } else {
    //console.log("fileError");
    cb({ errorCode: 1 }, false); //1 : 파일 확장자 오류
  }
};

const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: "gonggubucket",
    key: (req, file, cb) => {
      req.tempFileName = `${crypto
        .randomBytes(6)
        .readUIntLE(0, 6)
        .toString(36)}${path.extname(file.originalname)}`;
      cb(null, req.tempFileName);
    },
    //acl: "public-read-write",
  }),
  fileFilter: fileValidate,
  limits: { fileSize: 5 * 1024 * 1024 },
});

module.exports = { upload };
