const mysql2 = require("mysql2");
const path = require("path");
const { cb_func } = require("promisify");

require("dotenv").config({ path: path.join(__dirname, "../.env") });

const db = mysql2
  .createPool({
    host: process.env.RDS_DB_HOST,
    user: process.env.RDS_DB_USER,
    password: process.env.RDS_DB_PASSWORD,
    database: process.env.RDS_DB_DATABASE,
  })
  .promise();

module.exports = db;
