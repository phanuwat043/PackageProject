package com.catelecom.billing.controller;

import com.catelecom.billing.model.FilterPackage;
import com.catelecom.billing.model.Package;
import com.catelecom.billing.repository.PackageRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PackageController {

    Logger logger = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private PackageRepository packageRepository;

    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/secure/package")
    public String getPackage(Package packageModel) {
        logger.info("*** getPackage ***", PackageController.class);
        return "add-package";
    }

    @PostMapping("/secure/filterPackage")
    public String filterPackage(FilterPackage filter, Model model) {
        logger.info("*** filterPackage ***", PackageController.class);

        List<Package> packages = null;

        if (filter.getPackage_name().equals("")) {
            filter.setPackage_name("-");
        }
        if (filter.getCreate_date().equals("")) {
            filter.setCreate_date("-");
        }

        if (filter.getPackage_id() != null) {
            logger.info("*** filterPackageById ***", PackageController.class);
            packages = packageRepository.filterPackageById(filter.getPackage_id());
        } else if (filter.getAmount() != null) {
            logger.info("*** filterPackageByAmount ***", PackageController.class);
            packages = packageRepository.filterPackageByAmount(filter.getAmount());
        } else if (!filter.getCreate_date().equals("-")) {
            logger.info("*** filterPackageByDate ***", PackageController.class);
            packages = packageRepository.filterPackageByDate(filter.getCreate_date());
        } else if (!filter.getPackage_name().equals("-")) {
            logger.info("*** filterPackageByName ***", PackageController.class);
            packages = packageRepository.filterPackageByName(filter.getPackage_name());
        } else {
            logger.info("*** findAll ***", PackageController.class);
            packages = packageRepository.findAll();
        }

        if (filter.getPackage_id() != null && filter.getAmount() != null && !filter.getPackage_name().equals("-")) {
            logger.info("*** filterPackageAll ***", PackageController.class);
            packages = packageRepository.filterPackageAll(filter.getPackage_id(), filter.getPackage_name(), filter.getAmount());
        }

        model.addAttribute("packages", packages);
        return "list-package";
    }

    @PostMapping("/secure/addPackage")
    public String addPackage(@Valid Package packageModel, BindingResult result, Model model) {
        logger.info("*** addPackage ***", PackageController.class);

        if (result.hasErrors()) {
            return "add-package";
        }

        packageRepository.save(packageModel);
        model.addAttribute("packages", packageRepository.findAll());
        return "list-package";
    }

    @GetMapping("/secure/deletePackage/{id}")
    public String deletePackage(@PathVariable("id") int id, Model model) {
        logger.info("*** deletePackage ***", PackageController.class);

        Package package_ = packageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Package Id : " + id));
        packageRepository.delete(package_);
        model.addAttribute("packages", packageRepository.findAll());
        return "list-package";
    }

    // filter with code
    public Specification<Package> findWithPredicate(Integer package_id, String package_name, Integer amount, Date date) {
        return (Root<Package> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList();
            if (package_id != null) {
                predicates.add(cb.equal(root.get("package_id"), package_id));
            }
            if (StringUtils.hasText(package_name)) {
                predicates.add(cb.like(cb.lower(root.get("package_name")), "%" + package_name.toLowerCase() + "%"));
            }
            if (amount != null) {
                predicates.add(cb.equal(root.get("amount"), amount));
            }
            if (date != null) {
                predicates.add(cb.lessThanOrEqualTo(root.<Date>get("create_date"), date));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
