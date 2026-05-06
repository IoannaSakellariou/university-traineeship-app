package com.traineeshipApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.CompanyMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.mappers.UserMapper;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired 
    private CompanyMapper companyMapper;
    
    @Autowired 
    private UserMapper userMapper;
    
    @Autowired 
    private TraineeshipPositionMapper positionMapper;
    
    
    @Override
    public Company getCurrentCompany() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return companyMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found"));
    }
    
    @Override
    public void createPositionForCurrentCompany(TraineeshipPosition position, CommitteeMember cm) {
        Company company = getCurrentCompany();
        position.setCompany(company);
        position.setAssigned(false);
        position.setCommitteeMember(cm);
        positionMapper.save(position);
    }


    @Override
    public Company findByUsername(String username) {
        return companyMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found"));
    }

    @Override
    public Company findByUser(User user) {
        return companyMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found"));
    }

    @Override
    public Company save(Company company) {
        return companyMapper.save(company);
    }

    @Override
    public void updateCurrentCompany(Company companyForm) {
        Company company = getCurrentCompany();
        company.setCompanyName(companyForm.getCompanyName());
        company.setCompanyLocation(companyForm.getCompanyLocation());
        company.setPositions(companyForm.getPositions());
        companyMapper.save(company);
    }

    @Override
    public List<TraineeshipPosition> retrieveAvailablePositions() {
        Company company = getCurrentCompany();
        return positionMapper.findActiveOrAvailableByCompany(company);
    }
    
    

}
