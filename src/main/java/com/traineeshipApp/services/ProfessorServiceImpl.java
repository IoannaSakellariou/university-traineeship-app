package com.traineeshipApp.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.ProfessorMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.UserMapper;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired 
    private UserMapper userMapper;
    
    @Autowired 
    private ProfessorMapper professorMapper;
    
    @Autowired
    TraineeshipPositionMapper traineeshipPositionMapper;

    @Override
    public Professor getCurrentProfessor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return professorMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Professor not found"));
    }

    @Override
    public Professor findByUsername(String username) {
        return professorMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Professor not found"));
    }

    @Override
    public Professor findByUser(User user) {
        return professorMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Professor not found"));
    }

    @Override
    public void updateCurrentProfessor(Professor form) {
        Professor professor = getCurrentProfessor();
        professor.setProfessorName(form.getProfessorName());
        professor.setInterests(form.getInterests());
        professor.setSupervisedPositions(form.getSupervisedPositions());
        professorMapper.save(professor);
    }


    @Override
    public Professor findById(Integer id) {
        return professorMapper.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor with id " + id + " not found"));
    }
    
    @Override
    public List<TraineeshipPosition> getSupervisedPositions() {
        Professor professor = getCurrentProfessor();
        return traineeshipPositionMapper.findBySupervisorAndCompletedFalse(professor);
    }






}

