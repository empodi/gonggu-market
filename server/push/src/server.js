import "dotenv/config";
import express from "express";
import morgan from "morgan";
import cors from "cors";
import { runKafka } from "./tools/kafka";

const app = express();

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan("dev"));
app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  // update to match the domain you will make the request from
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  res.header("Access-Control-Max-Age", 600);
  next();
});

runKafka().catch(console.error);

const dummyRouter = express.Router();

dummyRouter.get("/", (req, res) => {
  return res.status(200).json({ ok: true });
});

app.use("/device", dummyRouter);

const handleListen = () =>
  console.log(`🎸 Server listening on PORT ${process.env.SERVER_PORT}`);

app.listen(process.env.SERVER_PORT, handleListen);
