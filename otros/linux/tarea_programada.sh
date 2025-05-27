#!/bin/bash
sed -i '$a 0,5,10,15,20,25,30,35,40,45,50,55 * * * * 'usuario' /'ruta_shell_verificar_cliente.sh' /etc/crontab