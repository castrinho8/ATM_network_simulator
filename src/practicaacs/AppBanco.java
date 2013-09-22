/***************************************************************************
 *	ATM Network Simulator ACS. FIC. UDC. 2012/2013									*
 *	Copyright (C) 2013 by Pablo Castro and Marcos Chavarria 						*
 * <pablo.castro1@udc.es>, <marcos.chavarria@udc.es> 								*
 * 																								*
 * This program is free software; you can redistribute it and/or modify 	*
 * it under the terms of the GNU General Public License as published by 	*
 * the Free Software Foundation; either version 2 of the License, or 		*
 * (at your option) any later version. 												*
 ***************************************************************************/
package practicaacs;

import javax.swing.UnsupportedLookAndFeelException;

import practicaacs.banco.Banco;

/**
 * Clase que ejecuta un Banco
 */
public class AppBanco {

public static void main(String[] args) {
	
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("GTK+".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e1){
    	System.err.println("Error establecendo GTK+ como estilo da interface.");
	}
	
	if(args.length == 1){
		System.out.println("Cargando configuracion...");
		new Banco(args[0]);
	}else{
		//Seleccionar la ruta local en la que se encuentra el fichero de configuracion
		new Banco("/home/castrinho8/Escritorio/UNI/ACS/res/banco1.properties");		
	}
}

}
