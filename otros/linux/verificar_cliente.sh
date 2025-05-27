#!/bin/bash
cd /usr/bin
#
if [ $( jps -l | pgrep -f controlador.jar ) ]; then
	echo "Proceso está corriendo..."
else
	echo "Proceso no está corriendo..."
	cd /'ruta_Controlador.jar'
	java -jar Controlador.jar
fi