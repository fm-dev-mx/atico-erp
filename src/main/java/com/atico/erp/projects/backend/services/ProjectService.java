package com.atico.erp.projects.backend.services;

import com.atico.erp.projects.backend.entities.Project;
import com.atico.erp.projects.backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findById(Long id) {
        return projectRepository.getById(id);
    }

    public List<Project> findAllProjectsByStatus(Project.Status status) {
        return projectRepository.getAllProjectsByStatus(status);
    }

    public List<Project> findNotDeletedProjectsByStatus(Project.Status status) {
        return projectRepository.getNotDeletedProjectsByStatus(status);
    }

    public List<Project> findAllSubprojects(Project parentProject) {
        return projectRepository.getAllSubprojects(parentProject.getId());
    }

    public List<Project> findNotDeletedSubprojects(Project parentProject) {
        return projectRepository.getNotDeletedSubprojects(parentProject.getId());
    }

    public Project insertProject(Project project) {

        // force project name to uppercase only before saving into database
        project.setName(project.getName().toUpperCase());

        // insert project
        projectRepository.save(project);

        // will assign a unique ID (project_index) for the new project, which will be equal to the count of all existing
        // projects in database (deleted or not)
        project.setProjectIndex(projectRepository.countAllProjects());

        // update project
        return projectRepository.save(project);
    }

    public void insertSubproject(Project subproject) {

        // force project name to uppercase only before saving into database
        subproject.setName(subproject.getName().toUpperCase());

        // insert subproject
        projectRepository.save(subproject);

        // will assign a unique ID (subproject_index) for the new subproject, which will be equal to the count of all
        // existing subprojects in database for the main project (deleted or not)
        Integer subprojectsInParentProject =
                projectRepository.countAllSubprojects(subproject.getParentProject().getId());

        subproject.setSubprojectIndex(subprojectsInParentProject);

        // update subproject
        projectRepository.save(subproject);
    }

    public void safeDelete(Project project) {
        // first safe delete all the subprojects linked with this project (if any)
        findAllSubprojects(project).forEach(subProject -> {
            projectRepository.safeDelete(subProject.getId());
        });

        // now safe delete the parent project itself
        projectRepository.safeDelete(project.getId());
    }

}
