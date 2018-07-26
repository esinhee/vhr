package org.sang.bean;

import java.io.Serializable;
import java.util.Date;

public class EventTask implements Serializable {
    private Integer id;

    private Integer eid;

    private Integer assignid;

    private String assignname;

    private Date assigntime;

    private Integer recorderid;

    private String recordername;

    private Date dealtime;

    private Date updatetime;

    private Integer bz;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getAssignid() {
        return assignid;
    }

    public void setAssignid(Integer assignid) {
        this.assignid = assignid;
    }

    public String getAssignname() {
        return assignname;
    }

    public void setAssignname(String assignname) {
        this.assignname = assignname == null ? null : assignname.trim();
    }

    public Date getAssigntime() {
        return assigntime;
    }

    public void setAssigntime(Date assigntime) {
        this.assigntime = assigntime;
    }

    public Integer getRecorderid() {
        return recorderid;
    }

    public void setRecorderid(Integer recorderid) {
        this.recorderid = recorderid;
    }

    public String getRecordername() {
        return recordername;
    }

    public void setRecordername(String recordername) {
        this.recordername = recordername == null ? null : recordername.trim();
    }

    public Date getDealtime() {
        return dealtime;
    }

    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getBz() {
        return bz;
    }

    public void setBz(Integer bz) {
        this.bz = bz;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", eid=").append(eid);
        sb.append(", assignid=").append(assignid);
        sb.append(", assignname=").append(assignname);
        sb.append(", assigntime=").append(assigntime);
        sb.append(", recorderid=").append(recorderid);
        sb.append(", recordername=").append(recordername);
        sb.append(", dealtime=").append(dealtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", bz=").append(bz);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}