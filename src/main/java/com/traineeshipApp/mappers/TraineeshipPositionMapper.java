package com.traineeshipApp.mappers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;

@Repository
public interface TraineeshipPositionMapper extends JpaRepository<TraineeshipPosition, Integer>{
	
	Optional<TraineeshipPosition> findFirstByStudentAndCompletedFalseOrderByFromDateDesc(Student student);

	List<TraineeshipPosition> findByStudent(Student student);


	List<TraineeshipPosition> findBySupervisor(Professor professor);
	
	List<TraineeshipPosition> findByCommitteeMember(CommitteeMember committeeMember);
	
	List<TraineeshipPosition> findByIsAssignedFalse();
	
	@Query("SELECT p FROM TraineeshipPosition p WHERE p.isAssigned = false AND :student NOT MEMBER OF p.applicants")
	List<TraineeshipPosition> findAvailableForStudent(@Param("student") Student student);

	List<TraineeshipPosition> findByApplicantsContaining(Student student);
	
	List<TraineeshipPosition> findByCompanyAndIsAssignedTrue(Company company);
	
	List<TraineeshipPosition> findByCompanyCompanyLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndIsAssignedFalse(
			String preferredLocation, String skills);

	List<TraineeshipPosition> findByTopicsContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndIsAssignedFalse(
			String interests, String skills);
	
	List<TraineeshipPosition> findByTopicsContainingIgnoreCaseAndCompanyCompanyLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndIsAssignedFalse(String topics, String location, String skills);

	List<TraineeshipPosition> findByIsAssignedTrueAndSupervisorIsNotNull();


	List<TraineeshipPosition> findByStudentIsNotNull();

	@Query("SELECT p FROM TraineeshipPosition p WHERE p.isAssigned = false AND :student NOT MEMBER OF p.applicants")
	List<TraineeshipPosition> findAvailableExcludingApplicant(@Param("student") Student student);

	@Query("SELECT p FROM TraineeshipPosition p WHERE :student MEMBER OF p.applicants")
	List<TraineeshipPosition> findByStudentApplications(@Param("student") Student student);

	List<TraineeshipPosition> findByCompletedTrue();
	
	List<TraineeshipPosition> findByCompanyAndIsAssignedTrueAndCompletedFalse(Company company);

	@Query("SELECT p FROM TraineeshipPosition p WHERE p.company = :company AND (p.completed = false)")
	List<TraineeshipPosition> findActiveOrAvailableByCompany(@Param("company") Company company);

	List<TraineeshipPosition> findBySupervisorAndCompletedFalse(Professor professor);
	
	// Assigned or Pending (Still in consideration)
	@Query("SELECT p FROM TraineeshipPosition p WHERE :student MEMBER OF p.applicants OR p.student = :student AND p.completed = false")
	List<TraineeshipPosition> findCurrentApplicationsByStudent(@Param("student") Student student);

	
	@Query("SELECT p FROM TraineeshipPosition p WHERE p.student = :student AND p.completed = true")
	List<TraineeshipPosition> findCompletedByStudent(@Param("student") Student student);
	
	@Query("SELECT t FROM TraineeshipPosition t WHERE t.isAssigned = true AND t.student IS NOT NULL AND t.supervisor IS NULL")
	List<TraineeshipPosition> findAssignedWithoutSupervisor();

	List<TraineeshipPosition> findByIsAssignedTrueAndCompletedFalse();

	List<TraineeshipPosition> findByStudentOrderByCompletedDesc(Student student);

	List<TraineeshipPosition> findByAssignedStudentAMOrderByToDateDesc(String am);


	
}
