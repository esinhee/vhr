package org.sang.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Event implements Serializable {
    private Integer eid;

    private Integer etid;

    private String etname;

    private Integer grade;

    private String kid;

    private String kname;

    private String reportid;

    private String reporter;

    private Date reporttime;

    private Integer recorderid;

    private String recorder;

    private Date recordetime;

    private Integer dealerid;

    private String dealer;

    private Date dealtime;

    private Date updatetime;

    private Integer dealflg;
    
    private String zname;

    private Integer relatedid;

    private Integer bz;

    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getEtid() {
        return etid;
    }

    public void setEtid(Integer etid) {
        this.etid = etid;
    }

    public String getEtname() {
        return etname;
    }

    public void setEtname(String etname) {
        this.etname = etname == null ? null : etname.trim();
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid == null ? null : kid.trim();
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname == null ? null : kname.trim();
    }

    public String getReportid() {
        return reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid == null ? null : reportid.trim();
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter == null ? null : reporter.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getReporttime() {
        return reporttime;
    }

    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }

    public Integer getRecorderid() {
        return recorderid;
    }

    public void setRecorderid(Integer recorderid) {
        this.recorderid = recorderid;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder == null ? null : recorder.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getRecordetime() {
        return recordetime;
    }

    public void setRecordetime(Date recordetime) {
        this.recordetime = recordetime;
    }

    public Integer getDealerid() {
        return dealerid;
    }

    public void setDealerid(Integer dealerid) {
        this.dealerid = dealerid;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer == null ? null : dealer.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    public Integer getDealflg() {
        return dealflg;
    }

    public void setDealflg(Integer dealflg) {
        this.dealflg = dealflg;
    }

    public String getZname() {
		return zname;
	}

	public void setZname(String zname) {
		this.zname = zname;
	}

	public Integer getRelatedid() {
        return relatedid;
    }

    public void setRelatedid(Integer relatedid) {
        this.relatedid = relatedid;
    }

    public Integer getBz() {
        return bz;
    }

    public void setBz(Integer bz) {
        this.bz = bz;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eid=").append(eid);
        sb.append(", etid=").append(etid);
        sb.append(", etname=").append(etname);
        sb.append(", grade=").append(grade);
        sb.append(", kid=").append(kid);
        sb.append(", kname=").append(kname);
        sb.append(", reportid=").append(reportid);
        sb.append(", reporter=").append(reporter);
        sb.append(", reporttime=").append(reporttime);
        sb.append(", recorderid=").append(recorderid);
        sb.append(", recorder=").append(recorder);
        sb.append(", recordetime=").append(recordetime);
        sb.append(", dealerid=").append(dealerid);
        sb.append(", dealer=").append(dealer);
        sb.append(", dealtime=").append(dealtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", dealflg=").append(dealflg);
        sb.append(", zname=").append(zname);
        sb.append(", relatedid=").append(relatedid);
        sb.append(", bz=").append(bz);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}