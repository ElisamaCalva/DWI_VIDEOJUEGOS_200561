const mongoose =require('mongoose');
mongoose.connect("mongodb+srv://A1:Jeheova123@cluster0.uhma6f4.mongodb.net/videojuegosdb?retryWrites=true&w=majority")
.then(db=>console.log("Mongodb connected..."))
.catch(err=>console.error(err));