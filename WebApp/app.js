var express = require("express"),
	app 	= express(),
	bodyParser 	= require("body-parser"),
	mongoose 	= require("mongoose"),
	passport	= require("passport"),
	LocalStrategy = require("passport-local"),
	User 	= require('./models/user'),
	Expense = require('./models/expense'),
	nodemailer = require('nodemailer'),
	smtpTransport = require('nodemailer-smtp-transport');

var budget;
var diff;

mongoose.connect("mongodb://localhost/smartspense");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.set("view engine","ejs");
app.use(express.static(__dirname + "/public"));

//Passport Configuration
app.use(require("express-session")({
	secret: "Hermione is my Favourite",
	resave: false,
	saveUninitialized: false
}));
app.use(passport.initialize());
app.use(passport.session());
passport.use(new LocalStrategy(User.authenticate()));
passport.serializeUser(User.serializeUser());
passport.deserializeUser(User.deserializeUser());

app.use(function(req, res, next){
	res.locals.currentUser = req.user;
	next();
});

// app.post('/register', function(req, res){
// 	var newUser = new User({username: req.body.username});
// 	User.register(newUser, req.body.password, function(err, user){
// 		if(err){
// 			res.status(404).json({
// 				message: "Yo man !! Ak here ! Some Error here"
// 			});
// 		}
// 		else{
// 			res.status(200).json({
// 				message: "Yo man !! Successfull Registration"
// 			});
// 		}
// 	});
// });

// app.post('/login', passport.authenticate("local"), function(req, res){
// 	res.status(200).json({
// 		message: "Okay"
// 	});
// });

app.get('/', function(req, res){
	res.render('index', {user: req.user});
});

//middleware
isLoggedIn = function(req, res, next){
	if(req.isAuthenticated()){
		return next();
	}
	// req.flash("error", "You need to be logged in!!");
	res.redirect("/");
}

//AUTH Routes
// app.get("/register", function(req, res){
// 	res.render("register");
// });

app.post("/register", function(req, res){
	var newUser = new User({username: req.body.username});
	User.register(newUser, req.body.password, function(err, user){
		if(err){
			// req.flash("error", err.message);
			// return res.render("register");
		}
		passport.authenticate("local")(req, res, function(){
			// req.flash("success", "Welcome to YelpCamp " + user.username + "!!");
			res.redirect("/");
		});
	});
});

// app.get("/login", function(req, res){
// 	res.render("login");
// });

app.post("/login", passport.authenticate("local", 
	{
		successRedirect: "/", 
	 	failureRedirect: "/"
	}),
	function(req, res){
		// res.send("Hello");		
});

//logout route
app.get("/logout", function(req, res){
	req.logout();
	// req.flash("success", "Logged you out!!");
	res.redirect("/");
});

//Other Routes

app.get('/addExp/:id', isLoggedIn, function(req, res){
	res.render('addExpense', {userid: req.params.id});
});

app.get('/addExp', isLoggedIn, function(req, res){

});

app.get('/viewTransac/:id', isLoggedIn, function(req, res){
	User.findById(req.params.id).populate("expenses").exec(function(err, foundUser){
		if(err){

		}
		else{
			res.render('addTransaction', {data: foundUser});
		}
	});
});

app.get('/viewTransac', isLoggedIn, function(req, res){

});

app.get('/profile', isLoggedIn, function(req, res){

});

app.get('/profile/:id', isLoggedIn, function(req, res){
	res.render('profile');	
});


app.post('/addExp/:id', isLoggedIn, function(req, res){
	var data = {item_name: req.body.name, price: req.body.price, category: req.body.category, payment_mode: req.body.mode_of_payment, date: req.body.date, note: req.body.note};
	User.findById(req.params.id, function(err, user){
		if(err){
			console.log(err);
		}
		else{
			Expense.create(data, function(err, expense){
				if(err){

				}
				else{
					user.expenses.push(expense);
					user.save();
					res.redirect('/');	
				}
			});
		}
	});
});

// app.get('/:id/profile', isLoggedIn, function(req, res){
// 	res.render('profile');
// });

app.post('/viewExpense', function(req ,res){
	Expense.find({}, function(err, data){
		if(err){
			console.log(err);
		}
		else{
			console.log(data);
			res.send(data);
		}
	});
});
	
app.post('/addExpense', function(req, res){
	var obj = req.body;	
	// var data = new Expense();


	console.log(obj);
	var data = {item_name: obj.itemName, category: obj.itemCategory, price: obj.price, payment_mode: obj.modeOfPayment, date: obj.paymentDate, note: obj.extraNote, email: obj.emailAddress};
	Expense.create(data, function(err, newData){
		if(err){

		}
		else{
			// console.log(newData);
			var budget = obj.budget;
			var date1 = Date.parse(obj.paymentDate);
			User.findById('5c5682401aae631d6ce3ea04').populate("expenses").exec(function(err, foundUser){
				if(err){
					console.log(err);
				}
				else{
					foundUser.expenses.push(newData);
					foundUser.save();
					
					var total = 0;
					foundUser.expenses.forEach(function(expense){
						total+=expense.price;
					});
					diff = parseInt(budget) - total;
					console.log(diff);
					// res.send("You still haven't outdone yourself " + diff);
					// var date1 = obj.paymentDate;
				}
			});
			res.status(200).json({
				message: diff
			});
		}
	});
});

app.post('', function(req, res){

});

//preparing data for charts
// app.get('/viewCharts', function(){
// 	Expense.aggregate([
// 		{
// 			$group: {
// 				_id: '$category',
// 				count: {$sum: 50}
// 			}
// 		}
// 	], function(err, result){
// 		if(err){
// 			console.log(err);
// 		}
// 		else{
// 			console.log(result);
// 		}
// 	});
// });								

app.listen(3000, function(){
	console.log('Server Started');
});
