package com.nexus.backend.service;

import com.nexus.backend.entity.Act;
import com.nexus.backend.entity.Compliance;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import com.nexus.backend.repository.*;
import com.nexus.backend.specification.ActSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActsService {

    @Autowired
    private ActRepository actRepository;

    @Autowired
    private ComplianceRepository complianceRepository;

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StateRepository stateRepository;

    public Act createAct(Act newAct, Integer userId) {
        newAct.setDate(LocalDateTime.now());
        newAct.setUploaderId(userId);

        if (newAct.getMinistry() != null) {
            Ministry ministry = newAct.getMinistry().getId() != null
                    ? ministryRepository.findById(newAct.getMinistry().getId()).orElse(null)
                    : null;
            newAct.setMinistry(ministry);
        }

        if (newAct.getIndustry() != null) {
            Industry industry = newAct.getIndustry().getId() != null
                    ? industryRepository.findById(newAct.getIndustry().getId()).orElse(null)
                    : null;
            newAct.setIndustry(industry);
        }

        if (newAct.getCategory() != null) {
            Category category = newAct.getCategory().getId() != null
                    ? categoryRepository.findById(newAct.getCategory().getId()).orElse(null)
                    : null;
            newAct.setCategory(category);
        }

        if (newAct.getState() != null) {
            State state = newAct.getState().getId() != null
                    ? stateRepository.findById(newAct.getState().getId()).orElse(null)
                    : null;
            newAct.setState(state);
        }

        newAct = actRepository.save(newAct);

        List<Compliance> complianceList = newAct.getComplianceSet();
        for (Compliance compliance : complianceList) {
            compliance.setAct(newAct);
        }

        complianceRepository.saveAll(complianceList);

        newAct.setTotalCompliance(complianceList.size());

        return actRepository.save(newAct);
    }

    public void deleteAct(Integer actId) {

        if (actRepository.existsById(actId)) {
            actRepository.deleteById(actId);
        } else {
            try {
                throw new Exception("Act not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Act> searchActs(String searchString) {
        Specification<Act> titleOrDescriptionSpecification = ActSpecification.titleOrDescriptionContains(searchString);

        Specification<Act> finalSpecification = Specification.where(titleOrDescriptionSpecification);

        return actRepository.findAll(finalSpecification);
    }

}

