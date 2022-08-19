const { Kafka } = require("kafkajs")

const client = "TRADE_GROUP"

const brokers = ["localhost:9092"]

const topic = "TRADE_FEED_IN"

const kafka = new Kafka({client, brokers})

const producer = kafka.producer()

const produce = async () => {
    await producer.connect()
    let i = 0

    setInterval(async () => {
       try {
            await producer.send({
                topic,
                messages: [
                    {
                        key: String(i),
                        value: "TRADE_DATA_{i}"                    }
                ]
            })

            // console.log("Writes: " + i)
            i++
        } catch(err) {
            console.error("Error producing message " + err)
        }  
    }, 1)
}

module.exports = produce