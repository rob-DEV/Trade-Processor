const produce = require("./producer")

console.log("Trade Producer")

produce().catch((err) => {
	console.error("error in producer: ", err)
})
