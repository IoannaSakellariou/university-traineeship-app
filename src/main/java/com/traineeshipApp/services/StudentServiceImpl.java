package com.traineeshipApp.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.mappers.UserMapper;


@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TraineeshipPositionMapper traineeshipPositionMapper;
	
	@Override
    public Student save(Student student) {
        return studentMapper.save(student); 
    }
	

	@Override
	public Student findByUsername(String username) {
	    User user = userMapper.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    return studentMapper.findByUser(user)
	                .orElseThrow(() -> new RuntimeException("Student not found for user"));
	}

	public Student findById(Integer id) {
        Optional<Student> studentOptional = studentMapper.findById(id);
        return studentOptional.orElseThrow(() -> new RuntimeException("Ο φοιτητής με ID " + id + " δεν βρέθηκε."));
    }
	public Student getCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userMapper.findByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return studentMapper.findByUser(user)
        		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
      
    }
	
	public void updateCurrentStudent(Student studentForm) {
	    Student student = getCurrentStudent();  
	    studentForm.setUser(student.getUser());
	    

	    student.setStudentName(studentForm.getStudentName());
	    student.setAM(studentForm.getAM());
	    student.setAssignedTraineeship(studentForm.getAssignedTraineeship());
	    student.setAvgGrade(studentForm.getAvgGrade());
	    student.setInterests(studentForm.getInterests());
	    student.setLookingForTraineeship(studentForm.isLookingForTraineeship());
	    student.setPreferredLocation(studentForm.getPreferredLocation());
	    student.setSkills(studentForm.getSkills());


	    studentMapper.save(studentForm); 
	}

	
	
	@Override
	public Student findByUser(User user) {
		Optional<Student> optionalStudent = studentMapper.findByUser(user);
		return optionalStudent.orElse(null);
	}

	@Override
	public void applyForPosition(String username, Integer positionId) {
	    Student student = findByUsername(username);
	    TraineeshipPosition position = traineeshipPositionMapper.findById(positionId)
	        .orElseThrow(() -> new RuntimeException("Θέση πρακτικής δεν βρέθηκε"));

	    if (!position.getApplicants().contains(student)) {
	        position.getApplicants().add(student);
	        traineeshipPositionMapper.save(position);
	    }
	}
	
	@Override
	public List<TraineeshipPosition> getAvailablePositionsForCurrentStudent() {
	    Student student = getCurrentStudent();
	    return traineeshipPositionMapper.findAvailableExcludingApplicant(student);
	}
	
	@Override
	public void appendToLogbook(String logbookText) {
	    Student student = getCurrentStudent();
	    List<TraineeshipPosition> positions = traineeshipPositionMapper.findByStudent(student);
	    
	    TraineeshipPosition active = positions.stream()
	        .filter(p -> !p.isCompleted())
	        .findFirst()
	        .orElseThrow(() -> new RuntimeException("No ongoing traineeship was found"));

	    String currentLog = active.getStudentLogBook();
	    String timestampedEntry = "[" + LocalDate.now() + "] " + logbookText + "\n";
	    String updatedLog = (currentLog == null ? "" : currentLog + "\n") + timestampedEntry;

	    active.setStudentLogBook(updatedLog);
	    traineeshipPositionMapper.save(active);
	}

	public List<TraineeshipPosition> getApplicationsForCurrentStudent() {
	    Student student = getCurrentStudent();
	    return traineeshipPositionMapper.findCurrentApplicationsByStudent(student);
	}

	
	public List<Student> findStudentsWithoutAssignedPosition() {
	    return studentMapper.findByAssignedTraineeshipIsNull();
	}
	
	@Override
	public List<TraineeshipPosition> getCompletedTraineeships() {
	    String am = getCurrentStudent().getAM();
	    return traineeshipPositionMapper.findByAssignedStudentAMOrderByToDateDesc(am);
	}


}
