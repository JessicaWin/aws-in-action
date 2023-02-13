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
