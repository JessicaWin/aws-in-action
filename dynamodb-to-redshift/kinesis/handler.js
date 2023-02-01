module.exports.transformData = async (event, context, callback) => {
  try {
    console.log("event: " + JSON.stringify(event));
    if (!event.records) {
      console.log("Empty Records");
      return;
    }

    const output = event.records.map((record) => {
      if (!record.recordId) {
        return;
      }

      console.log("record: " + JSON.stringify(record));
      const payload = JSON.parse(Buffer.from(record.data, "base64").toString());
      console.log("payload: " + JSON.stringify(payload));

      // only support insert, as update will introduce duplcate data
      // if your application allow duplcate data, you can support update here
      if (payload.eventName !== "INSERT") {
        return;
      }

      const dbRecord = payload.dynamodb;
      console.log("dbRecord: " + JSON.stringify(dbRecord));

      const attributeKeys = Object.keys(dbRecord.NewImage);
      const item = {};
      attributeKeys.forEach((attributeKey) => {
        const attributeObj = dbRecord.NewImage[attributeKey];
        const attributeKeyType = Object.keys(attributeObj).toString();
        const attributeValue =
          dbRecord.NewImage[attributeKey][attributeKeyType];
        // Return empty if null
        if (attributeKeyType === "NULL") {
          item[attributeKey] = null;
        } else {
          item[attributeKey] = attributeValue;
        }
      });

      console.log(`Item: ${JSON.stringify(item)}`);

      return {
        recordId: record.recordId,
        result: "Ok",
        data: Buffer.from(JSON.stringify(item)).toString("base64"),
      };
    });

    console.log(`Processing completed.  Successful records ${output.length}.`);
    callback(null, { records: output });
  } catch (err) {
    console.log("Error" + err);
    return;
  }
};
