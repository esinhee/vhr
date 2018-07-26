package org.sang.bean;

import java.io.Serializable;

public class TicketStatus implements Serializable {
    private Integer zid;

    private String zname;

    private Boolean bz;

    private static final long serialVersionUID = 1L;

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public String getZname() {
        return zname;
    }

    public void setZname(String zname) {
        this.zname = zname == null ? null : zname.trim();
    }

    public Boolean getBz() {
        return bz;
    }

    public void setBz(Boolean bz) {
        this.bz = bz;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", zid=").append(zid);
        sb.append(", zname=").append(zname);
        sb.append(", bz=").append(bz);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}