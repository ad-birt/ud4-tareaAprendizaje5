package ejercicios;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import entidades.Address;
import entidades.Student;

public class Ud4TareaAprendizaje3cEjercicio1 {

	/**
	 * 1. Crea un nuevo objeto Student con su dirección,
	 * teléfonos y fecha de nacimiento y guárdalo en la base de datos.
	 * Utiliza la fecha de nacimiento para crear el atributo calculado age
	 * con la anotación @Formula
	 */
	public static void main(String[] args) {

		// crea sessionFactory y session
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
			    .configure( "hibernate.cfg.xml" )
			    .build();

		Metadata metadata = new MetadataSources( standardRegistry )
			    .addAnnotatedClass( Student.class )
			    .getMetadataBuilder()
			    .build();

		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
			    .build();    
		
		Session session = sessionFactory.openSession();
		
		try {			
			// crea un objeto Student
			System.out.println("Creando un nuevo objeto Student con su dirección...");
			Student student = createStudent();	
					
			// comienza la transacción
			session.beginTransaction();
			
			// guarda el objeto Student
			System.out.println("Guardando el estudiante...");
			session.persist(student);
			
			// hace commit de la transaccion
			session.getTransaction().commit();
			
			// es necesario refresh para hacer una select y así obtener el valor del campo calculado
			session.refresh(student);
			System.out.println(student.getAge());
			
			System.out.println("Hecho!");
		}
		catch ( Exception e ) {
			// rollback ante alguna excepción
			System.out.println("Realizando Rollback");
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
			sessionFactory.close();
		}
	}
	private static Student createStudent() {
		Student tempStudent = new Student();
		Address tempAddress = new Address();
		tempStudent.setFirstName("Mikel");
		tempStudent.setLastName("Unamuno");
		tempStudent.setEmail("gunamuno@birt.eus");
		tempStudent.getPhones().add("612123456");
		tempStudent.getPhones().add("612123457");
		tempStudent.setBirthdate(LocalDate.parse("1989-04-04"));
		tempAddress.setAddressLine1("Kale Nagusia 10");
		tempAddress.setAddressLine2("3B");
		tempAddress.setCity("Donostia");
		tempAddress.setZipCode("20003");
		tempStudent.setAddress(tempAddress);
		return tempStudent;		
	}
}

