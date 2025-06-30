package Metodos;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import javax.swing.JOptionPane;

public class ValidadorLicencia {
    
    // URL del servicio - Ajusta según tu configuración
    private static final String URL_SERVICIO = "http://110.238.64.52:8082/LicenciasService/api/v1/licencias/validar";
    
    /**
     * Obtiene la dirección MAC de la máquina local
     * @return Dirección MAC en formato XX-XX-XX-XX-XX-XX o null si no se encuentra
     */
    public static String obtenerMacLocal() {
        try {
            // Intentar obtener todas las interfaces de red
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                
                // Saltar interfaces virtuales, loopback o que no estén activas
                if (network.isLoopback() || network.isVirtual() || !network.isUp()) {
                    continue;
                }
                
                // Verificar si es una interfaz virtual
                String nombre = network.getName().toLowerCase();
                String displayName = network.getDisplayName().toLowerCase();
                
                if (nombre.contains("vmware") || nombre.contains("virtualbox") || 
                    nombre.contains("vbox") || nombre.contains("virtual") ||
                    displayName.contains("vmware") || displayName.contains("virtualbox") ||
                    displayName.contains("vbox") || displayName.contains("virtual")) {
                    continue;
                }
                
                byte[] mac = network.getHardwareAddress();
                
                if (mac != null && mac.length > 0 && !esVirtualMac(mac)) {
                    // Formatear MAC address
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
            
            // Si no encontramos una interfaz física válida, intentar con la IP local
            InetAddress localAddress = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(localAddress);
            
            if (network != null) {
                byte[] mac = network.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error obteniendo MAC address: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Verifica si una MAC address pertenece a una máquina virtual
     */
    private static boolean esVirtualMac(byte[] mac) {
        if (mac == null || mac.length < 3) return false;
        
        // MACs conocidas de máquinas virtuales
        byte[][] virtualMacs = {
            {0x00, 0x05, 0x69},         // VMWare
            {0x00, 0x1C, 0x14},         // VMWare
            {0x00, 0x0C, 0x29},         // VMWare
            {0x00, 0x50, 0x56},         // VMWare
            {0x08, 0x00, 0x27},         // VirtualBox
            {0x0A, 0x00, 0x27},         // VirtualBox
            {0x00, 0x03, (byte)0xFF},   // Virtual-PC
            {0x00, 0x15, 0x5D}          // Hyper-V
        };
        
        for (byte[] virtualMac : virtualMacs) {
            if (mac[0] == virtualMac[0] && mac[1] == virtualMac[1] && mac[2] == virtualMac[2]) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Valida la licencia contra el servicio web usando GET
     * @param mac Dirección MAC a validar
     * @return true si la licencia es válida, false en caso contrario
     */
    public static boolean validarLicencia(String mac) {
        HttpURLConnection conexion = null;
        
        try {
            System.out.println("=== DEBUG: VALIDANDO LICENCIA ===");
            System.out.println("MAC a validar: " + mac);
            
            // Usar el endpoint GET que ya tienes definido
            String urlConParametro = URL_SERVICIO + "/" + URLEncoder.encode(mac, "UTF-8");
            System.out.println("URL completa: " + urlConParametro);
            
            URL url = new URL(urlConParametro);
            conexion = (HttpURLConnection) url.openConnection();
            
            // Configurar la conexión
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setConnectTimeout(30000); // 30 segundos timeout
            conexion.setReadTimeout(30000);
            
            // Leer la respuesta
            int responseCode = conexion.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                
                // Parsear la respuesta manualmente (sin librería JSON)
                String jsonResponse = response.toString();
                System.out.println("JSON completo recibido: " + jsonResponse);
                
                // SOLUCIÓN: Buscar el campo "valido" considerando espacios
                // Normalizar el JSON removiendo espacios innecesarios para el parsing
                String jsonNormalizado = jsonResponse.replaceAll("\\s+", " ");
                System.out.println("JSON normalizado: " + jsonNormalizado);
                
                // Buscar diferentes variaciones del campo valido
                boolean esValido = false;
                
                // Variación 1: "valido":true
                if (jsonNormalizado.contains("\"valido\":true")) {
                    esValido = true;
                    System.out.println("Encontrado patrón: \"valido\":true");
                }
                // Variación 2: "valido": true
                else if (jsonNormalizado.contains("\"valido\": true")) {
                    esValido = true;
                    System.out.println("Encontrado patrón: \"valido\": true");
                }
                // Variación 3: "valido" : true
                else if (jsonNormalizado.contains("\"valido\" : true")) {
                    esValido = true;
                    System.out.println("Encontrado patrón: \"valido\" : true");
                }
                // Método más robusto: usar regex o buscar por partes
                else {
                    // Buscar "valido" y luego buscar "true" cerca
                    int validoIndex = jsonNormalizado.indexOf("\"valido\"");
                    if (validoIndex != -1) {
                        // Buscar "true" en los siguientes 20 caracteres
                        String substring = jsonNormalizado.substring(validoIndex, 
                            Math.min(validoIndex + 20, jsonNormalizado.length()));
                        System.out.println("Substring para análisis: " + substring);
                        if (substring.contains("true")) {
                            esValido = true;
                            System.out.println("Encontrado 'true' cerca de 'valido'");
                        }
                    }
                }
                
                System.out.println("Resultado de validación: " + esValido);
                
                if (!esValido) {
                    // Extraer el mensaje de error usando método más robusto
                    String mensaje = extraerCampoJson(jsonResponse, "mensaje");
                    String estado = extraerCampoJson(jsonResponse, "estado");
                    
                    System.out.println("Mensaje extraído: " + mensaje);
                    System.out.println("Estado extraído: " + estado);
                    
                    // Mostrar mensaje de error
                    String mensajeError = mensaje.isEmpty() ? "Licencia no válida" : mensaje;
                    if (!estado.isEmpty()) {
                        mensajeError += "\nEstado: " + estado;
                    }
                    
                    JOptionPane.showMessageDialog(null, mensajeError, 
                        "Error de Licencia", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("¡LICENCIA VÁLIDA!");
                }
                
                return esValido;
                
            } else {
                // Error en la respuesta
                System.err.println("Error HTTP: " + responseCode);
                
                if (responseCode == 404) {
                    JOptionPane.showMessageDialog(null, 
                        "Licencia no registrada para esta MAC: " + mac,
                        "Licencia No Encontrada", JOptionPane.ERROR_MESSAGE);
                } else if (responseCode == 500) {
                    JOptionPane.showMessageDialog(null, 
                        "Error interno del servidor de licencias",
                        "Error del Servidor", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Error al validar licencia. Código: " + responseCode,
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                return false;
            }
            
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, 
                "No se puede conectar con el servidor de licencias.\n" +
                "Verifique su conexión a internet y que el servicio esté activo.\n" +
                "URL: " + URL_SERVICIO,
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SocketTimeoutException e) {
            JOptionPane.showMessageDialog(null, 
                "Tiempo de espera agotado al conectar con el servidor de licencias.",
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al validar la licencia: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }
    
    /**
     * Extrae un campo del JSON de forma más robusta
     * @param json JSON string
     * @param campo nombre del campo a extraer
     * @return valor del campo o string vacío si no se encuentra
     */
    private static String extraerCampoJson(String json, String campo) {
        try {
            String patron = "\"" + campo + "\"";
            int index = json.indexOf(patron);
            if (index == -1) return "";
            
            // Buscar el inicio del valor (después de los dos puntos)
            int startValue = json.indexOf(":", index);
            if (startValue == -1) return "";
            
            // Buscar el inicio de la comilla del valor
            int startQuote = json.indexOf("\"", startValue);
            if (startQuote == -1) return "";
            
            // Buscar el final de la comilla del valor
            int endQuote = json.indexOf("\"", startQuote + 1);
            if (endQuote == -1) return "";
            
            return json.substring(startQuote + 1, endQuote);
        } catch (Exception e) {
            System.err.println("Error extrayendo campo " + campo + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Escribe un log de error
     */
    public static void escribirLog(String mensaje) {
        System.out.println("LOG: " + mensaje);
        
        File logFile = new File(System.getProperty("user.home") + File.separator + 
                               "SSigner" + File.separator + "log" + File.separator + 
                               "licencia_validacion.log");
        
        try {
            // Crear directorio si no existe
            logFile.getParentFile().mkdirs();
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
                bw.write("[" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "] " + mensaje);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}