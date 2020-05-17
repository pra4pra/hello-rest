package com.tential.hellorest.model;

import javax.persistence.*;

@Entity
@Table(name = "department")
public class Department extends AuditColumnsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "EMPLOYEE_COUNT")
    private Integer employeeCount;

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", employeeCount=" + employeeCount +
                "} " + super.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        final Department that = (Department) o;

        return this.departmentId.equals(that.departmentId);
    }

    @Override
    public int hashCode() {
        return this.departmentId.hashCode();
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getEmployeeCount() {
        return this.employeeCount;
    }

    public void setEmployeeCount(final Integer employeeCount) {
        this.employeeCount = employeeCount;
    }
}
