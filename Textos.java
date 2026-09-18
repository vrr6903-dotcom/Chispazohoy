import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Carga el archivo de textos del sitio (strings.properties) según el idioma
 * configurado, y da acceso a cada texto por su clave.
 *
 * Cada lotería/idioma tiene su propio archivo, en la misma carpeta que su
 * fantasy.properties (ver estructura de carpetas: C:\Loterias\Florida\español\,
 * C:\Loterias\Florida\ingles\, etc.). El nombre del archivo es siempre
 * "strings.properties" — lo que cambia es la carpeta donde vive.
 *
 * Uso:
 *   textos.get("boton.buscar")                     -> "Buscar"
 *   textos.get("tabla.pares.titulo", 5)             -> "Combinaciones de dos números: Frecuencia > 5"
 *   textos.get("aviso.secundario", "Florida")       -> "...Lotería de Florida."
 */
public class Textos {

    private final Properties prop;

    private Textos(Properties prop) {
        this.prop = prop;
    }

    /** Carga strings.properties desde la ruta indicada (forzando UTF-8, igual que fantasy.properties). */
    public static Textos cargar(String rutaStringsProperties) throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaStringsProperties);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            prop.load(reader);
        }
        return new Textos(prop);
    }

    /** Devuelve el texto tal cual, sin sustituir variables. */
    public String get(String clave) {
        String valor = prop.getProperty(clave);
        if (valor == null) {
            System.err.println("⚠️ Falta la clave \"" + clave + "\" en strings.properties");
            return "[[" + clave + "]]"; // visible a propósito, para detectar claves faltantes en el sitio
        }
        return valor;
    }

    /** Devuelve el texto sustituyendo {0}, {1}... con los valores dados (java.text.MessageFormat). */
    public String get(String clave, Object... valores) {
        return MessageFormat.format(get(clave), valores);
    }
}
