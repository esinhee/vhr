package org.sang.bean;

import java.io.Serializable;
import java.util.Date;

public class Attach implements Serializable {
    private Integer atid;

    private String atname;

    private String atuuid;

    private String atsize;

    private String attype;

    private String atpath;

    private Integer uploadid;

    private String uploadby;

    private Date uploaddate;

    private Integer releatid;

    private Integer cid;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    private static final long serialVersionUID = 1L;

    public Integer getAtid() {
        return atid;
    }

    public void setAtid(Integer atid) {
        this.atid = atid;
    }

    public String getAtname() {
        return atname;
    }

    public void setAtname(String atname) {
        this.atname = atname == null ? null : atname.trim();
    }

    public String getAtuuid() {
        return atuuid;
    }

    public void setAtuuid(String atuuid) {
        this.atuuid = atuuid == null ? null : atuuid.trim();
    }

    public String getAtsize() {
        return atsize;
    }

    public void setAtsize(String atsize) {
        this.atsize = atsize == null ? null : atsize.trim();
    }

    public String getAttype() {
        return attype;
    }

    public void setAttype(String attype) {
        this.attype = attype == null ? null : attype.trim();
    }

    public String getAtpath() {
        return atpath;
    }

    public void setAtpath(String atpath) {
        this.atpath = atpath == null ? null : atpath.trim();
    }

    public Integer getUploadid() {
        return uploadid;
    }

    public void setUploadid(Integer uploadid) {
        this.uploadid = uploadid;
    }

    public String getUploadby() {
        return uploadby;
    }

    public void setUploadby(String uploadby) {
        this.uploadby = uploadby == null ? null : uploadby.trim();
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    public Integer getReleatid() {
        return releatid;
    }

    public void setReleatid(Integer releatid) {
        this.releatid = releatid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", atid=").append(atid);
        sb.append(", atname=").append(atname);
        sb.append(", atuuid=").append(atuuid);
        sb.append(", atsize=").append(atsize);
        sb.append(", attype=").append(attype);
        sb.append(", atpath=").append(atpath);
        sb.append(", uploadid=").append(uploadid);
        sb.append(", uploadby=").append(uploadby);
        sb.append(", uploaddate=").append(uploaddate);
        sb.append(", releatid=").append(releatid);
        sb.append(", cid=").append(cid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}