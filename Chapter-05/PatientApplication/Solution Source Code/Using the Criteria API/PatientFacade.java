package packt;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

@Stateless
public class PatientFacade extends AbstractFacade<Patient> {

    @PersistenceContext(unitName = "PatientApplication-ejbPU")
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public PatientFacade() {
        super(Patient.class);
    }

    @Override
    public List<Patient> findAll() {
        Query query = entityManager.createQuery("select p FROM Patient p");
        List<Patient> list = query.getResultList();
        return list;
    }

    public void processAllPatients(PrintWriter out) {
        int querySize = 5;
        int beginIndex = 0;
        while(true) {
            Query query = entityManager.createQuery("SELECT p FROM Patient p");
            query.setMaxResults(querySize);
            query.setFirstResult(beginIndex);
            List<Patient> list = query.getResultList();
				// Process list
            if (list.isEmpty()) {
                break;
            } else {
                // Process
                for (Patient patient : list) {
                    out.println("<h5>"+patient.getFirstName()+"</h5>");
                }
                entityManager.clear();
                beginIndex = beginIndex + list.size();
            }
        }
    }

    public int delete(String firstName, String lastName) {
        Query query = entityManager.createQuery("DELETE FROM Patient p WHERE p.firstName = '"
                + firstName + "' AND p.lastName = '" + lastName + "'");
        int numberDeleted = query.executeUpdate();
        return numberDeleted;
    }

    public void findAllMales(PrintWriter out) {
        CriteriaBuilder criteriaBuilder;
        criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Patient> criteriaQuery = criteriaBuilder.createQuery(Patient.class);
        Root<Patient> patientRoot = criteriaQuery.from(Patient.class);

        criteriaQuery.where(criteriaBuilder.equal(
                patientRoot.get("sex"),"M"));
        List<Patient> patients = getEntityManager().createQuery(criteriaQuery).getResultList();
        for (Patient p : patients) {
            out.println("<h5>" + p.getFirstName() + "</h5>");
        }

}
