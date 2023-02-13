'use strict'
const AWS = require('aws-sdk');

module.exports.sendDataToKinesisDataStream = async (event, context) => {
    const kinesis = new AWS.Kinesis();
    var params = {
        Data: `${JSON.stringify(event)}`,
        PartitionKey: 'test',
        StreamName: process.env.STREAM_NAME
    };
    let data = await kinesis.putRecord(params).promise();
    console.log(`${JSON.stringify(data)}`);
}

module.exports.consumeDataFromKinesisDataStream = (event, context) => {
    console.log(`${JSON.stringify(event)}`);
    const eventBody = JSON.parse(event.body);
    for (let record of eventBody.records) {
        const data = JSON.parse(Buffer.from(record.data, 'base64').toString("utf8"));
        console.log(data);
    }
    const response = {};
    response.requestId = eventBody.requestId;
    response.timestamp = eventBody.timestamp;
    context.succeed(response);
}