const{Schema,model}=require('mongoose');
const videojuegosShema=new Schema({
    codigobarras:{
        type:String,
        require:true,
        unique:true
    },
    name:String,
    estudio:String,
    genero:String,
    precio:Number,
    existencias:Number

},{
    versionKey:false,
    timestamps:true
});
module.exports=model('videojuegos',videojuegosShema);