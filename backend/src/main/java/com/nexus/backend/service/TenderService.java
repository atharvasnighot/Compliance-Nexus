package com.nexus.backend.service;

import com.nexus.backend.entity.Tender;
import com.nexus.backend.entity.TenderCompliance;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import com.nexus.backend.repository.*;
import com.nexus.backend.specification.TenderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TenderService {

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private TenderComplianceRepository complianceRepository;

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StateRepository stateRepository;

    public Tender createAct(Tender newAct, Integer userId) {
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

        newAct = tenderRepository.save(newAct);

        List<TenderCompliance> complianceList = newAct.getComplianceSet();
        for (TenderCompliance compliance : complianceList) {
            compliance.setTender(newAct);
        }

        complianceRepository.saveAll(complianceList);

        newAct.setTotalCompliance(complianceList.size());

        return tenderRepository.save(newAct);
    }

    public void deleteAct(Integer actId) {

        if (tenderRepository.existsById(actId)) {
            tenderRepository.deleteById(actId);
        } else {
            try {
                throw new Exception("Act not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Tender> searchActs(String searchString) {
        Specification<Tender> titleOrDescriptionSpecification = TenderSpecification.titleOrDescriptionContains(searchString);

        Specification<Tender> finalSpecification = Specification.where(titleOrDescriptionSpecification);

        return tenderRepository.findAll(finalSpecification);
    }

}
