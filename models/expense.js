var mongoose = require("mongoose");

var ExpenseSchema = new mongoose.Schema({
	item_name: String,
	category: String,
	price: Number,
	payment_mode: String,
	date: String,
	note: String,
	email: String
});

module.exports = mongoose.model("Expense", ExpenseSchema);