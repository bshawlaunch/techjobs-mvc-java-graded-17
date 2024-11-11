package org.launchcode.techjobsmvc.controllers;

import org.launchcode.techjobsmvc.models.Job;
import org.launchcode.techjobsmvc.models.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

import static org.launchcode.techjobsmvc.controllers.ListController.columnChoices;


/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @GetMapping(value = "")
    public String search(Model model) {
        model.addAttribute("columns", columnChoices);
        return "search";
    }

    // TODO #3 - Create a handler to process a search request and render the updated search view.
    @PostMapping(value = "results")
    public String displaySearchResults(Model model, @RequestParam(required = false) String searchType, @RequestParam String searchTerm) {
        ArrayList<Job> jobs = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        columns.add("name");
        columns.add("employer");
        columns.add("location");
        columns.add("positionType");
        columns.add("coreCompetency");

        if ((searchType == null || searchType.equals("all")) && searchTerm.isBlank()) {
            jobs = JobData.findAll();
            model.addAttribute("title", "All Jobs");
        } else if ((searchType == null || searchType.equals("all")) && !searchTerm.isBlank()) {
            for (String column : columns) {
                for (Job job : JobData.findByColumnAndValue(column, searchTerm)) {
                    if (!jobs.contains(job)) {
                        jobs.add(job);
                    }
                }
            }

            model.addAttribute("title", "Jobs with: " + searchTerm);
        } else {
            jobs = JobData.findByColumnAndValue(searchType, searchTerm);
            model.addAttribute("title", "Jobs with " + columnChoices.get(searchType) + ": " + searchTerm);
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("columns", columnChoices);
        return "search";
    }
}

