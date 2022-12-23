const webpush = require("web-push");
const { Kafka, Partitioners } = require("kafkajs");
import db from "./db";

const kafka = new Kafka({
  clientId: "kafka-my-app",
  brokers: ["13.209.40.90:9092"],
});

const consumer = kafka.consumer({ groupId: "gonggu" });

const initKafka = async () => {
  await consumer.connect();
};

initKafka();

webpush.setVapidDetails(
  "mailto:2022capstone16.5@gmail.com",
  process.env.VAPID_PUBLIC,
  process.env.VAPID_PRIVATE
);

const pushHandler = (result, pTitle, pMsg) => {
  result.forEach((device) => {
    let pushConfig = {
      endpoint: device.endpoint,
      keys: {
        auth: device.auth,
        p256dh: device.p256dh,
      },
    };
    //console.log("✅ device:", device);
    webpush
      .sendNotification(
        pushConfig,
        JSON.stringify({
          pTitle: pTitle,
          pMsg: pMsg,
        })
      )
      .catch((err) => {
        console.log(err);
      });
  });
};

export const runKafka = async () => {
  await consumer.subscribe({
    topics: ["chatMessage", "dealJoin", "dealComplete", "dealDelete"],
    fromBeginning: true,
  });
  await consumer.run({
    eachMessage: async ({ topic, partition, message, messages }) => {
      try {
        console.log("⭐️ Kafka - ", {
          topic,
          value: message.value.toString(),
        });

        if (String(topic) === "dealJoin") {
          const info = JSON.parse(message.value.toString());
          //const str = info.nickname.join();
          const str = info.nickname.map((d) => `'${d}'`).join(",");
          str.slice(0, -1);
          //console.log(str);
          const [result] = await db.query(
            `SELECT * FROM device WHERE nickname IN (${str});`
          );
          pushHandler(
            result,
            "✅ 새로운 공구 희망자",
            `"${info.title}"에 대한 공구 희망자가 생겼습니다.`
          );
        } else if (topic === "dealComplete") {
          const info = JSON.parse(message.value.toString());
          const str = info.nickname.map((d) => `'${d}'`).join(",");
          str.slice(0, -1);
          //console.log(str);
          const [result] = await db.query(
            `SELECT * FROM device WHERE nickname IN (${str});`
          );
          pushHandler(
            result,
            "✅ 공구 모집 완료",
            `"${info.title}"에 대한 공구가 완료되었습니다.`
          );
        } else if (topic === "dealDelete") {
          const info = JSON.parse(message.value.toString());
          const str = info.nickname.map((d) => `'${d}'`).join(",");
          str.slice(0, -1);
          //console.log(str);
          const [result] = await db.query(
            `SELECT * FROM device WHERE nickname IN (${str});`
          );
          pushHandler(
            result,
            "❗️ 공구 취소",
            `"${info.title}"에 대한 공구가 취소되었습니다.`
          );
        } else if (topic === "chatMessage") {
          const info = JSON.parse(message.value.toString());
          //console.log(info);
          const [result] = await db.query(
            `WITH TBL AS (SELECT DISTINCT nickname FROM deal_member WHERE deal_id = '${info.roomId}' and deal_member.nickname <> '${info.sender}')
            SELECT * FROM device NATURAL JOIN TBL WHERE TBL.nickname = device.nickname;`
          );
          //console.log(result);
          pushHandler(
            result,
            "✅ 새로운 채팅",
            `'${info.title}'방에서 새 메세지가 왔습니다.`
          );
        }
      } catch (err) {
        console.log("❌ Kafka err", err);
      }
    },
  });
};
