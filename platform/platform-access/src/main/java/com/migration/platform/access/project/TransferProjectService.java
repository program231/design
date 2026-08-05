package com.migration.platform.access.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图3-2 服务接入层：传输项目管理.
 */
public class TransferProjectService {

    private final Map<String, TransferProject> projects = new ConcurrentHashMap<String, TransferProject>();

    public TransferProject create(String name, String sourceDsId, String targetDsId) {
        TransferProject project = new TransferProject();
        project.setProjectId(UUID.randomUUID().toString());
        project.setName(name);
        project.setSourceDatasourceId(sourceDsId);
        project.setTargetDatasourceId(targetDsId);
        project.setState("CREATED");
        projects.put(project.getProjectId(), project);
        return project;
    }

    public TransferProject get(String projectId) {
        return projects.get(projectId);
    }

    public List<TransferProject> list() {
        return Collections.unmodifiableList(new ArrayList<TransferProject>(projects.values()));
    }

    public static class TransferProject {
        private String projectId;
        private String name;
        private String sourceDatasourceId;
        private String targetDatasourceId;
        private String state;

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSourceDatasourceId() {
            return sourceDatasourceId;
        }

        public void setSourceDatasourceId(String sourceDatasourceId) {
            this.sourceDatasourceId = sourceDatasourceId;
        }

        public String getTargetDatasourceId() {
            return targetDatasourceId;
        }

        public void setTargetDatasourceId(String targetDatasourceId) {
            this.targetDatasourceId = targetDatasourceId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
