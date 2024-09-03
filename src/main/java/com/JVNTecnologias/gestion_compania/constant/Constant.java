package com.JVNTecnologias.gestion_compania.constant;

public class Constant {
    public class Message{
        public static String OPERACION_EXITO = "La operacion se realizado de manera correcta";
        public static String COMPANIA_GUARDADA = "Se ha guardado el compañia";
        public static String COMPANIA_ACTUALIZADA = "Se ha actualizado la compañia";
        public static  String ERROR_CREANDO = "Se ha presentado un error al crear la compañia.";
        public static  String ERROR_CONSULTADO = "Se ha presentado un error al consultar %s.";
        public static  String CONSULTA_EXITOSA= "Se ha consultado de manera correcta.";
        public static  String NO_DATA= "No se ha encontrado informacion";
        public static  String ERROR_DATA= "Error: la información suministrada no coincide.";

        public static String NO_EXISTE_COMPANIA = "La compañia con el ID ingresado no existe";
        public static String INACTIVA_COMPANIA = "La compañia se encuentra es estado inactiva y no se le puede asociar ninguna sucursal";

        public static String NO_CAMBIAR = "No se puede cambiar de compañia una sucursal";
        public static String SUCURSAL_ASIGNADA = "No se puede eliminar la compañía porque tiene sucursales asignadas.";
    }

}
