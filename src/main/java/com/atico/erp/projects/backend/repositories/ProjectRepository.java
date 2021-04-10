package com.atico.erp.projects.backend.repositories;

import com.atico.erp.projects.backend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT COUNT(p) FROM Project p WHERE parent_project_id IS NULL")
    public Integer countAllProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE parent_project_id=:parent_project_id")
    public Integer countAllSubprojects(@Param("parent_project_id") Long parentProjectId);

    @Query("SELECT p FROM Project p WHERE id=:id")
    public Project getById(@Param("id") Long id);

    @Query("SELECT p FROM Project p WHERE parent_project_id IS NULL AND status=:status")
    public List<Project> getAllProjectsByStatus(@Param("status") Project.Status status);

    @Query("SELECT p FROM Project p WHERE parent_project_id IS NULL AND is_deleted=False AND status=:status")
    public List<Project> getNotDeletedProjectsByStatus(@Param("status") Project.Status status);

    @Query("SELECT p FROM Project p WHERE parent_project_id IS NOT NULL AND parent_project_id=:parent_project_id")
    public List<Project> getAllSubprojects(@Param("parent_project_id") Long parentProjectId);

    @Query("SELECT p FROM Project p WHERE parent_project_id=:parent_project_id AND is_deleted=False")
    public List<Project> getNotDeletedSubprojects(@Param("parent_project_id") Long parentProjectId);

    @Modifying
    @Query("UPDATE Project p SET is_deleted=true WHERE id=:project_id")
    public void safeDelete(@Param("project_id") Long projectId);
}
