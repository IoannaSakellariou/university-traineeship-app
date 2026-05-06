package com.traineeshipApp.integration;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.services.TraineeshipPositionService;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TraineeshipAssignmentIntegrationTest {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TraineeshipPositionMapper positionMapper;

    @Autowired
    private TraineeshipPositionService positionService;

    @Test
    public void testAssignAndHistory() {
  
        Student student = new Student();
        student.setUsername("john123");
        student.setStudentName("John Doe");
        student.setAM("2021001");
        student = studentMapper.save(student);

        // Δημιουργία 1ης θέσης, την κάνουμε completed και ΑΠΟΣΥΝΔΕΟΥΜΕ τον φοιτητή
        TraineeshipPosition completedPos = new TraineeshipPosition();
        completedPos.setTitle("Old Internship");
        completedPos.setCompleted(true);
        completedPos.setAssigned(true);
        completedPos.setAssignedStudentAM(student.getAM()); // κρατάμε ιστορικό
        completedPos = positionMapper.save(completedPos); // αποθήκευση ΧΩΡΙΣ student

        // Δημιουργία νέας διαθέσιμης θέσης
        TraineeshipPosition newPos = new TraineeshipPosition();
        newPos.setTitle("New Internship");
        newPos = positionMapper.save(newPos);

        // Ανάθεση στον φοιτητή
        positionService.assignStudentToPosition(newPos.getId(), student.getId());

        // Φόρτωσε τα δεδομένα από τη βάση
        Student refreshed = studentMapper.findById(student.getId()).orElseThrow();
        TraineeshipPosition assigned = positionMapper.findById(newPos.getId()).orElseThrow();

        //Έλεγχοι
        assertNotNull(refreshed.getAssignedTraineeship());
        assertEquals("New Internship", refreshed.getAssignedTraineeship().getTitle());
        assertTrue(assigned.isAssigned());
        assertEquals(student.getId(), assigned.getStudent().getId());

        // Ολοκληρωμένη παλιά πρακτική εξακολουθεί να υπάρχει και ΔΕΝ έχει student
        TraineeshipPosition refreshedOld = positionMapper.findById(completedPos.getId()).orElseThrow();
        assertTrue(refreshedOld.isCompleted());
        assertNull(refreshedOld.getStudent());
        assertEquals("2021001", refreshedOld.getAssignedStudentAM());

        //  Συνολικά 2 πρακτικές του φοιτητή (μία assigned, μία με AM ιστορικό)
        List<TraineeshipPosition> all = positionMapper.findAll(); // or use custom query
        Integer studentId = student.getId();
        
        assertEquals(student.getAM(), completedPos.getAssignedStudentAM());

         long countForThisStudent = all.stream()
                .filter(p -> p.getStudent() != null && p.getStudent().getId().equals(studentId))
                .count();
        assertEquals(1, countForThisStudent, "Student should be assigned to exactly one position");
        }
    
	    @AfterEach
	    void cleanUp() {
	        positionMapper.deleteAll();
	        studentMapper.deleteAll();
	    }
}