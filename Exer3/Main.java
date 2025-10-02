import java.util.*;

// Enumeraciones según el diagrama
enum Materiales {
    BRONCE, HIERRO, MARMOL
}

enum TipoPintura {
    OLEO, PASTEL, ACUARELA
}

enum Estilos {
    NEOCLASICO, GRECORROMANO, CUBISTA
}

// Clase Autor
class Autor {
    private String nombre;
    private String nacionalidad;

    public Autor(String nombre, String nacionalidad) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
}

// Clase Obra (superclase)
abstract class Obra {
    protected String titulo;
    protected Autor autor;

    public Obra(String titulo, Autor autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public abstract void mostrarInfo();
}

// Clase Pintura (subclase de Obra)
class Pintura extends Obra {
    private TipoPintura tipo;
    private String formato;

    public Pintura(String titulo, Autor autor, TipoPintura tipo, String formato) {
        super(titulo, autor);
        this.tipo = tipo;
        this.formato = formato;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Pintura: " + titulo + " (" + tipo + ", Formato: " + formato + ")");
        System.out.println("Autor: " + autor.getNombre() + " (" + autor.getNacionalidad() + ")");
    }
}

// Clase Escultura (subclase de Obra)
class Escultura extends Obra {
    private Materiales material;
    private Estilos estilo;

    public Escultura(String titulo, Autor autor, Materiales material, Estilos estilo) {
        super(titulo, autor);
        this.material = material;
        this.estilo = estilo;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Escultura: " + titulo + " (Material: " + material + ", Estilo: " + estilo + ")");
        System.out.println("Autor: " + autor.getNombre() + " (" + autor.getNacionalidad() + ")");
    }
}

// Clase Sala
class Sala {
    private String nombre;
    private List<Obra> obras;

    public Sala(String nombre) {
        this.nombre = nombre;
        this.obras = new ArrayList<>();
    }

    public void agregarObra(Obra obra) {
        obras.add(obra);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Obra> getObras() {
        return obras;
    }
}

// Clase Museo
class Museo {
    private String nombre;
    private String direccion;
    private String ciudad;
    private String pais;
    private List<Sala> salas;

    public Museo(String nombre, String direccion, String ciudad, String pais) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.salas = new ArrayList<>();
    }

    public void agregarSala(Sala sala) {
        salas.add(sala);
    }

    public void mostrarMuseo() {
        System.out.println("\n=== Museo: " + nombre + " ===");
        System.out.println(direccion + ", " + ciudad + ", " + pais);

        for (Sala sala : salas) {
            System.out.println("\nSala: " + sala.getNombre());
            for (Obra obra : sala.getObras()) {
                obra.mostrarInfo();
                System.out.println("-------------------");
            }
        }
    }
}

// Clase principal con ejemplo de uso
public class Main {
    public static void main(String[] args) {
        Autor autor1 = new Autor("Picasso", "Español");
        Autor autor2 = new Autor("Miguel Ángel", "Italiano");

        Pintura pintura1 = new Pintura("Guernica", autor1, TipoPintura.OLEO, "Lienzo");
        Escultura escultura1 = new Escultura("David", autor2, Materiales.MARMOL, Estilos.GRECORROMANO);

        Sala sala1 = new Sala("Sala Principal");
        sala1.agregarObra(pintura1);
        sala1.agregarObra(escultura1);

        Museo museo = new Museo("Museo del Arte", "Calle Mayor 123", "Madrid", "España");
        museo.agregarSala(sala1);

        museo.mostrarMuseo();
    }
}

