package com.suplementos.erp.jsf;

import com.suplementos.erp.model.AuditLog;
import com.suplementos.erp.repository.AuditLogRepository;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class AuditLogBean implements Serializable {
    private List<AuditLog> logs;
    private AuditLogRepository auditLogRepository;

    @PostConstruct
    public void init() {
        auditLogRepository = new AuditLogRepository();
        this.logs = auditLogRepository.buscarTodos();
    }

    public List<AuditLog> getLogs() {
        return logs;
    }
}