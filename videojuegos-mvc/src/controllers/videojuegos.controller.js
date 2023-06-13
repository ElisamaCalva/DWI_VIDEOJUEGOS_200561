const Videojuego=require('../models/Videojuego.model');
const videojuegosController={};


videojuegosController.obtenerVideojuegos=async(req,res)=>{
    const videojuegos=await Videojuego.find();
    res.json(videojuegos);
};
videojuegosController.obtenerVideojuego=async(req,res)=>{
    const videojuego=await Videojuego.findOne({codigobarras:req.params.cb});
    res.json(videojuego);    
};
videojuegosController.insertarVideojuego=async(req,res)=>{
    const videoJuegoInsertado=new Videojuego(req.body);
    await videoJuegoInsertado.save();
    res.json({
        status:"Video Juego insertado"
    });
};
videojuegosController.actualizarVideojuego=async(req,res)=>{
    const resp = await Videojuego.findOneAndUpdate({codigobarras:req.params.cb},req.body);
    /*res.json({
        status:"Video Juego actualizado"
    });*/
    if(res!=null)
        res.json({status: "Videojuego actualizado"});
    else
        res.json({status: "No encontrado"});
};
videojuegosController.eliminarVideojuego=async(req,res)=>{
    const resp = await Videojuego.findOneAndDelete({codigobarras:req.params.cb});
    /*res.json({
        status:"Video Juego eliminado"
    });*/
    if(res!=null)
        res.json({status: "Videojuego eliminado"});
    else
        res.json({status: "No encontrado"});
};
module.exports=videojuegosController;