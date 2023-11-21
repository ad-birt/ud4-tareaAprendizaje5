package ejercicios;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import entidades.Course;
import entidades.Student;
import entidades.Tuition;
import entidades.University;

public class Ud4TareaAprendizaje5Ejercicio1 {

    public static void main(String[] args) {
		// crea sessionFactory y session
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
			    .configure( "hibernate.cfg.xml" )
			    .build();

		Metadata metadata = new MetadataSources( standardRegistry )
			    .addAnnotatedClass( Student.class )
			    .addAnnotatedClass( Tuition.class )
			    .addAnnotatedClass( University.class )
			    .addAnnotatedClass( Course.class )
			    .getMetadataBuilder()
			    .build();

		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
			    .build();    
		
		Session session = sessionFactory.openSession();
		

        try {
            session.beginTransaction();

            // Consulta 1: Obtener todos los estudiantes
            Query<Student> query1 = session.createQuery("SELECT s FROM Student s", Student.class);
            List<Student> students = query1.list();
            for (Student student : students) {
                System.out.println(student.getFirstName() + " " + student.getLastName());
            }

            // Consulta 2: Obtener estudiantes con un determinado apellido
            Query<Student> query2 = session.createQuery("SELECT s FROM Student s WHERE s.lastName = :lastName", Student.class);
            query2.setParameter("lastName", "García");
            List<Student> studentsGarcia = query2.list();
            for (Student student : studentsGarcia) {
                System.out.println(student.getFirstName() + " " + student.getLastName());
            }
            
            // Consulta 3: Contar el número de estudiantes en una universidad específica
            String universityName = "EHU";
            Query<Long> query3 = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.university.name = :universityName", Long.class);
            query3.setParameter("universityName", universityName);
            long studentCount = query3.uniqueResult();
            System.out.println("Número de estudiantes en " + universityName + ": " + studentCount);


            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}