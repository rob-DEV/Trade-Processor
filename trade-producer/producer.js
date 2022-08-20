const { Kafka } = require("kafkajs")

const client = "TRADE_GROUP"

const brokers = ["localhost:9092"]

const topic = "TRADE_FEED_IN"

const kafka = new Kafka({client, brokers})

const producer = kafka.producer()

const produce = async () => {

    // Read JSON trade data
    const fs = require('fs')
    const path = require('path')

    const jsonPath = path.join(__dirname, '..', 'common', 'mock_trades.json')
    const trade_data = JSON.parse(fs.readFileSync(jsonPath))
    
    await producer.connect()
    let index = 0
    while(true) {
        const randomTradeIndex = Math.floor(Math.random() * (trade_data.length))
        const trade = trade_data[randomTradeIndex]

        await producer.send({
            topic,
            messages: [
                {
                    key: String(index),
                    value: JSON.stringify(trade)                
                }
            ]
        })
        index++;
        console.log(index)
    }
}

module.exports = produce