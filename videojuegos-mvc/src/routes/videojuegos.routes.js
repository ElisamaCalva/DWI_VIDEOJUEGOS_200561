const {Router}=require('express');
const videojuegosController=require('../controllers/videojuegos.controller');

const router=Router();
router.get('/',videojuegosController.obtenerVideojuegos); 
router.get('/:cb',videojuegosController.obtenerVideojuego);
router.post('/insert',videojuegosController.insertarVideojuego);
router.put('/actualizar/:cb',videojuegosController.actualizarVideojuego);
router.delete('/borrar/:cb',videojuegosController.eliminarVideojuego);
module.exports=router; 